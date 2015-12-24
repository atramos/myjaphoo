/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic;

import org.myjaphoo.model.dbconfig.Commit;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.dbconfig.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 * dao class for preferences saved in the database
 *
 * @author lang
 */
public class PreferencesDao extends AbstractDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreferencesDao.class);


    public PreferencesDao(DBConnection dbConn) {
        super(dbConn);
    }

    public PreferencesDao() {
        super(null);
    }

    public <T> void create(final T entity) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                em.persist(entity);
            }
        };
    }


    public <T> void edit(final T entity) throws Exception {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                em.merge(entity);
            }
        };
    }

    public <T> T find(final String key, final Class<T> clazz) {
        return new Loader<T>(getDbConn()) {
            @Override
            protected T run(EntityManager em) {
                return em.find(clazz, key);
            }
        }.execute();
    }

    public void remove(final String key) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                Query query = em.createQuery("delete from Prefs where id = :id");
                query.setParameter("id", key);
                query.executeUpdate();
            }
        };
    }
}
