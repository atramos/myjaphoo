/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.logic;

import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Thumbnail;
import org.myjaphoo.model.dbconfig.Commit;
import org.myjaphoo.model.dbconfig.DBConnection;

import javax.persistence.EntityManager;


/**
 *
 * @author mla
 */
public class ThumbnailJpaController extends AbstractDao{

    
    public ThumbnailJpaController(DBConnection dbConn) {
        super(dbConn);
    }

    public ThumbnailJpaController() {
        super(null);
    }
    

    public void create(final Thumbnail thumbnail) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                MovieEntry movieEntry = thumbnail.getMovieEntry();
                if (movieEntry != null) {
                    movieEntry = em.getReference(movieEntry.getClass(), movieEntry.getId());
                    thumbnail.setMovieEntry(movieEntry);
                }
                em.persist(thumbnail);
                if (movieEntry != null) {
                    movieEntry.getThumbnails().add(thumbnail);
                    movieEntry = em.merge(movieEntry);
                }
            }
        };
    }

    public void removeThumb(final Thumbnail tn) {
        new Commit(getDbConn()) {

            @Override
            protected void run(EntityManager em) {
                Thumbnail tnm = em.merge(tn);
                em.remove(tnm);
                tnm.getMovieEntry().getThumbnails().remove(tnm);
            }
        };
    }

}
