/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.logic;


import org.myjaphoo.model.db.PathMapping;
import org.myjaphoo.model.dbconfig.Commit;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.dbconfig.Loader;
import org.myjaphoo.model.logic.exceptions.NonexistentEntityException;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.util.List;


/**
 *
 * @author lang
 */
public class PathMappingJpaController extends AbstractDao{


    public PathMappingJpaController() {

        super(null);

    }


    public PathMappingJpaController(DBConnection dbConn) {
        super(dbConn);
    }

    public void create(final PathMapping pathMapping) {

        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                em.persist(pathMapping);
            }
        };
    }

    public void edit(final PathMapping pathMapping) throws NonexistentEntityException, Exception {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                em.merge(pathMapping);
            }
        };
    }

    public void destroy(final Long id) throws NonexistentEntityException {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                PathMapping pathMapping;
                try {
                    pathMapping = em.getReference(PathMapping.class, id);
                    pathMapping.getId();
                } catch (EntityNotFoundException enfe) {
                    throw new RuntimeException("The pathMapping with id " + id + " no longer exists.", enfe);
                }
                em.remove(pathMapping);
            }
        };
    }

    public List<PathMapping> findPathMappingEntities() {
        return findPathMappingEntities(true, -1, -1);
    }

    public List<PathMapping> findPathMappingEntities(int maxResults, int firstResult) {
        return findPathMappingEntities(false, maxResults, firstResult);
    }

    private List<PathMapping> findPathMappingEntities(final boolean all, final int maxResults, final int firstResult) {
        return new Loader<List<PathMapping>>(getDbConn()) {
            @Override
            protected List<PathMapping> run(EntityManager em) {
                Query q = em.createQuery("select object(o) from PathMapping as o order by id");
                if (!all) {
                    q.setMaxResults(maxResults);
                    q.setFirstResult(firstResult);
                }
                return q.getResultList();
            }
        }.execute();
    }

    public PathMapping findPathMapping(final Long id) {
        return new Loader<PathMapping>(getDbConn()) {
            @Override
            protected PathMapping run(EntityManager em) {
                return em.find(PathMapping.class, id);
            }
        }.execute();
    }
}
