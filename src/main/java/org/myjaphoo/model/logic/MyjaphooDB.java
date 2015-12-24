/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic;

import org.hibernate.stat.Statistics;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.logic.dbhandling.TransactionBoundaryDelegator.LoaderBlock;
import org.myjaphoo.model.logic.dbhandling.WmDatabaseOpener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
/**
 * Singleton holder for the db connection.
 *
 * @author lang
 */
public class MyjaphooDB {


    private static final Logger LOGGER = LoggerFactory.getLogger(MyjaphooDB.class.getName());

    private DBConnection dbConnection = null;

    private static MyjaphooDB wmdb = null;

    private MyjaphooDB() {
        dbConnection = WmDatabaseOpener.openDatabase();
        dbConnection.open();
    }

    public static MyjaphooDB singleInstance() {
        if (wmdb == null) {
            wmdb = new MyjaphooDB();
        }
        return wmdb;
    }

    public static synchronized void changeToOtherDatabase(String configName) {
        if (wmdb != null) {
            try {
                wmdb.closeDB();
            } catch (Exception e) {
                LOGGER.error("error closing db!", e);
            }
        }
        wmdb = null; //reset singleton
        WmDatabaseOpener.setDatabaseDefinition(configName);

    }

    public static synchronized void changeToDefaultDatabase() {
        changeToOtherDatabase(null);
    }

    public DatabaseConfiguration getConfiguration() {
        return dbConnection.getConfiguration();
    }

    /**
     * Für spezielle Zwecke kann ein weiterer Entity Manager erzeugt werden.
     * Das sollte aber mit Vorsicht benutzt werden!!!
     *
     * @return
     */
    public EntityManager createEntityManager() {
        return dbConnection.createEntityManager();
    }

    public EntityManagerFactory getEmf() {
        return dbConnection.getEmf();
    }

    public void emClear() {
        LOGGER.info("clearing entity manager");
        dbConnection.clear();
    }

    public MovieEntry ensureObjIsAttached(final MovieEntry obj) {
        return dbConnection.load(new LoaderBlock<MovieEntry>() {

            @Override
            public MovieEntry runLoadBlock(EntityManager em) {
                return smartAttach(em, obj);
            }
        });
    }

    private static MovieEntry smartAttach(EntityManager em, MovieEntry obj) {
        if (!em.contains(obj)) {
            // rescue the transient tokenset:
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("finding movie entry " + obj.getName() + " in database");
            }
            MovieEntry attached = em.find(MovieEntry.class, obj.getId());
            return attached;
        } else {
            return obj;
        }
    }

    public Token ensureObjIsAttached(final Token obj) {
        return dbConnection.load(new LoaderBlock<Token>() {

            @Override
            public Token runLoadBlock(EntityManager em) {
                return smartAttach(em, obj);
            }
        });
    }

    public static Token smartAttach(EntityManager em, Token obj) {
        if (!em.contains(obj)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("finding token entry " + obj.getName() + " in database");
            }
            Token attached = em.find(Token.class, obj.getId());
            return attached;
        }
        return obj;
    }

    public static MetaToken smartAttach(EntityManager em, MetaToken obj) {
        if (!em.contains(obj)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("finding meta token " + obj.getName() + " in database");
            }
            return em.find(MetaToken.class, obj.getId());
        }
        return obj;
    }

    public void closeDB() {
        dbConnection.close();
        wmdb = null;
    }

    public DBConnection getConnection() {
        return dbConnection;
    }

    public void printStatistics() {
        getStatistics().logSummary();

    }

    public Statistics getStatistics() {
        return dbConnection.getStatistics();
    }
}
