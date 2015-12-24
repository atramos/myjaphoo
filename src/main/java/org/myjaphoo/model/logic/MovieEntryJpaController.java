/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic;

import org.hibernate.jpa.QueryHints;
import org.myjaphoo.model.db.*;
import org.myjaphoo.model.dbconfig.Commit;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.dbconfig.Loader;
import org.myjaphoo.model.filterparser.Substitution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.QueryHint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * dao class for movie entries.
 *
 * @author lang
 */
public class MovieEntryJpaController extends AbstractDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieEntryJpaController.class);


    public MovieEntryJpaController(DBConnection dbConn) {
        super(dbConn);
    }

    public MovieEntryJpaController() {
        super(null);
    }

    public void create(final ChangeLog changeLog) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                em.persist(changeLog);
            }
        };
    }

    public void create(final ChronicEntry movieEntry) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                em.persist(movieEntry);
            }
        };
    }

    public void create(final BookMark bookmark) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                em.persist(bookmark);
            }
        };
    }

    public void create(final MovieEntry movieEntry) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                em.persist(movieEntry);
            }
        };
    }

    public void edit(final MovieEntry movieEntry) throws Exception {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                em.merge(movieEntry);
            }
        };
    }

    public void edit(final BookMark bookmark) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                em.merge(bookmark);
            }
        };
    }

    public void removeMovieEntry(final MovieEntry entry) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                MovieEntry mentry = em.merge(entry);
                for (Thumbnail tn : mentry.getThumbnails()) {
                    //tn.setMovieEntry(null);
                    em.remove(tn);
                }
                mentry.getThumbnails().clear();
                Query query = em.createNativeQuery("delete from Token_MovieEntry e where movieentries_id = :mid");
                query.setParameter("mid", mentry.getId());
                query.executeUpdate();
                // remove token-relations:
                for (Token token : mentry.getTokens()) {
                    token.getMovieEntries().remove(mentry);
                }
                mentry.getTokens().clear();


                em.remove(mentry);
            }
        };
    }

    public List<MovieEntry> fetchAll() {
        return new Loader<List<MovieEntry>>(getDbConn()) {
            @Override
            protected List<MovieEntry> run(EntityManager em) {
                LOGGER.debug("find/load all movie entries");
                Query q = em.createQuery("select o from MovieEntry o left join fetch o.attributes");
                q.setHint(QueryHints.HINT_READONLY, true);
                return q.getResultList();
            }
        }.execute();
    }

    public List<ChronicEntry> findChronicEntryEntities() {
        return findChronicEntryEntities(true, -1, -1);
    }

    public List<ChronicEntry> findChronicEntryEntities(int maxResults, int firstResult) {
        return findChronicEntryEntities(false, maxResults, firstResult);
    }

    private List<ChronicEntry> findChronicEntryEntities(final boolean all, final int maxResults, final int firstResult) {
        return new Loader<List<ChronicEntry>>(getDbConn()) {
            @Override
            protected List<ChronicEntry> run(EntityManager em) {
                Query q = em.createQuery("select object(o) from ChronicEntry as o order by id desc");
                if (!all) {
                    q.setMaxResults(maxResults);
                    q.setFirstResult(firstResult);
                }
                return q.getResultList();
            }
        }.execute();
    }

    public List<ChangeLog> findChangeLogEntities() {
        return findChangeLogEntities(true, -1, -1);
    }

    private List<ChangeLog> findChangeLogEntities(final boolean all, final int maxResults, final int firstResult) {

        return new Loader<List<ChangeLog>>(getDbConn()) {
            @Override
            protected List<ChangeLog> run(EntityManager em) {
                Query q = em.createQuery("select object(o) from ChangeLog as o order by id desc");
                if (!all) {
                    q.setMaxResults(maxResults);
                    q.setFirstResult(firstResult);
                }
                return q.getResultList();
            }
        }.execute();
    }

    public static Map<String, Substitution> createSubst(List<BookMark> bookmarks) {
        HashMap<String, Substitution> result = new HashMap<String, Substitution>();
        for (BookMark bm : bookmarks) {
            Substitution subst = new Substitution(bm.getName(), bm.getView().getCombinedFilterExpression());
            result.put(bm.getName(), subst);
        }
        return result;
    }

    public List<BookMark> findBookMarkEntities() {
        return findBookMarkEntities(true, -1, -1);
    }

    private List<BookMark> findBookMarkEntities(final boolean all, final int maxResults, final int firstResult) {
        return new Loader<List<BookMark>>(getDbConn()) {
            @Override
            protected List<BookMark> run(EntityManager em) {
                Query q = em.createQuery("select object(o) from BookMark as o order by name");
                if (!all) {
                    q.setMaxResults(maxResults);
                    q.setFirstResult(firstResult);
                }
                return q.getResultList();
            }
        }.execute();
    }

    public void removeBookMark(final BookMark bm) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                BookMark bmmerged = em.merge(bm);
                em.remove(bmmerged);
            }
        };
    }
}
