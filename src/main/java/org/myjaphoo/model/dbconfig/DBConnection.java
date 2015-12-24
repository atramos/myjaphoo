/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.dbconfig;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.hibernate.stat.Statistics;
import org.myjaphoo.model.db.DatabaseVersion;
import org.myjaphoo.model.db.upgrades.DBUpgrade;
import org.myjaphoo.model.db.upgrades.DatabaseVersionDao;
import org.myjaphoo.model.db.upgrades.Upgrader;
import org.myjaphoo.model.logic.dbhandling.ThreadLocalTransactionBoundaryHandler;
import org.myjaphoo.model.logic.dbhandling.TransactionBoundaryDelegator;
import org.myjaphoo.model.logic.dbhandling.TransactionBoundaryDelegator.CommitBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.Map;

/**
 * Handles a database connection in myjaphoo database. The parameters are
 * encapsulated in a Connection parameter class.
 *
 * @author mla
 */
public class DBConnection {

    public static final Logger LOGGER = LoggerFactory.getLogger(DBConnection.class.getName());
    
    private DatabaseConfiguration configuration;
    private EntityManagerFactory emf = null;
    private TransactionBoundaryDelegator tr = null;

    public DBConnection(DatabaseConfiguration configuration) {
        this.configuration = (DatabaseConfiguration) configuration.clone();
    }

    public void open() {
        emf = createEMF();
        validateDatabaseUpgrades(emf);
        tr = new ThreadLocalTransactionBoundaryHandler(this);
    }

    public void close() {
        tr.close();
        emf.close();
        // reset variables.
        tr = null;
        emf = null;
    }

    public void clear() {
        tr.clear();
    }

    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public Statistics getStatistics() {
        SessionFactory sf = ((EntityManagerFactoryImpl) emf).getSessionFactory();
        return sf.getStatistics();
    }

    protected TransactionBoundaryDelegator getTransactionHandler() {
        return tr;
    }

    public void commit(TransactionBoundaryDelegator.CommitBlock block) {
        tr.doInNewTransaction(block);
    }

    public <T> T load(TransactionBoundaryDelegator.LoaderBlock<T> block) {
        return tr.doLoading(block);
    }

    private EntityManagerFactory createEMF() {

        Map properties = getConfiguration().createPropetyMap();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("WankmanPU", properties);

        return emf;
    }

    /**
     * @return the configuration
     */
    public DatabaseConfiguration getConfiguration() {
        return configuration;
    }

    private void validateDatabaseUpgrades(EntityManagerFactory emf) {
        LOGGER.info("validatin database upgrading");

        ThreadLocalTransactionBoundaryHandler tr = new ThreadLocalTransactionBoundaryHandler(this);
        try {
            final DatabaseVersionDao dao = new DatabaseVersionDao(tr);
            DBUpgrade currentOne = dao.getLatestUpgrade();
            LOGGER.info("current database state is {0}", currentOne);
            
            DBUpgrade nextOne = currentOne.getNextOne();
            // try upgrading until the latest version:
            while (nextOne != null) {
                checkAndUpgrade(tr, nextOne);
                nextOne = nextOne.getNextOne();
            }
        } finally {
            tr.close();
        }
    }

    private void checkAndUpgrade(TransactionBoundaryDelegator tr, final DBUpgrade upgrade) {

        tr.doInNewTransaction(new CommitBlock() {

            @Override
            public void runCommitBlock(EntityManager em) throws Exception {
                LOGGER.info("upgrading to {0}", upgrade);
                Upgrader upgrader = upgrade.getUpgrader();
                if (upgrader != null) {
                    upgrader.upgrade(em);
                }
                DatabaseVersion v = new DatabaseVersion();
                v.setDbversion(upgrade);
                v.setUpgradedAt(new Date());
                em.persist(v);
                LOGGER.info("upgrading to {0} finished!", upgrade);
            }
        });
    }
}
