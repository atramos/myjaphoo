/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic;

import org.myjaphoo.model.db.SavedGroovyScript;
import org.myjaphoo.model.dbconfig.Commit;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.dbconfig.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


/**
 * dao class for scripts.
 *
 * @author lang
 */
public class ScriptJpaController extends AbstractDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptJpaController.class);


    public ScriptJpaController(DBConnection dbConn) {
        super(dbConn);
    }

    public ScriptJpaController() {
        super(null);
    }

    public void create(final SavedGroovyScript script) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                em.persist(script);
            }
        };
    }


    public void edit(final SavedGroovyScript script) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                em.merge(script);
            }
        };
    }


    public List<SavedGroovyScript> findScriptEntities() {
        return new Loader<List<SavedGroovyScript>>(getDbConn()) {
            @Override
            protected List<SavedGroovyScript> run(EntityManager em) {
                Query q = em.createQuery("select object(o) from SavedGroovyScript as o order by name");
                return q.getResultList();
            }
        }.execute();
    }


    public void removeScript(final SavedGroovyScript script) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                SavedGroovyScript mergedScript = em.merge(script);
                em.remove(mergedScript);
            }
        };
    }
}
