/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import org.hibernate.jpa.QueryHints;
import org.myjaphoo.gui.ThumbTypeDisplayMode;
import org.myjaphoo.model.db.ThumbType;
import org.myjaphoo.model.db.Thumbnail;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.logic.dbhandling.ThreadLocalTransactionBoundaryHandler;
import org.myjaphoo.model.logic.dbhandling.TransactionBoundaryDelegator;
import org.myjaphoo.model.registry.ComponentRegistry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * a dao class for plain loading of thumbnails from database.
 * handles its own database connection as internal state (incl. first level cache).
 * Loads within a transaction.
 *
 * @author lang
 */
public class ThumbLoader {

    private int counter = 0;
    // cache clear after n acts:
    private static final int CLEAR_LIMIT = 200;
    private TransactionBoundaryDelegator transactionHandler;


    public ThumbLoader(DBConnection dBConnection) {
        transactionHandler = new ThreadLocalTransactionBoundaryHandler(dBConnection);
    }


    public BufferedImage load(final long movieId, final int column) {
        return load(movieId, column, ThumbTypeDisplayMode.NORMAL);
    }

/* @wiki:ScriptingExtensions
  ===== ConfigurableThumbLoader =====
  This is an extension to plug in additional thumb loader logic.
  Multiple such loaders could be registered in the application.
  They are then tried out in sequence till the first one returns a requested thumb.

  ==== Interface ====

  The following interface must be implemented:

  ${f.listing(org.myjaphoo.gui.util.ConfigurableThumbLoader.class)}
 */

    public BufferedImage load(final long movieId, final int column, final ThumbTypeDisplayMode mode) {
        List<Thumbnail> thumbs = loadThumbs(movieId);
        Thumbnail thumb = selectThumb(thumbs, column, mode);
        if (thumb != null) {
            // toolkit uses a "faster" image instance
            //Image i = Toolkit.getDefaultToolkit().createImage(thumbs.get(column).getThumbnail());
            // copy it to a "faster" bufferd image which uses rgb model:

            //BufferedImage bi = Picture.toBufferedImage(i);
            BufferedImage bi = ThumbImageCreation.createBIThumbImage(thumb);
            return bi;
        }

        Collection<ConfigurableThumbLoader> loaders = ComponentRegistry.registry.getEntryCollection(ConfigurableThumbLoader.class);
        for (ConfigurableThumbLoader configurableThumbLoader: loaders) {
            BufferedImage bi = configurableThumbLoader.load(movieId, column, mode);
            if (bi != null) {
                return bi;
            }
        }

        return null;
    }

    private Thumbnail selectThumb(List<Thumbnail> thumbs, int column, ThumbTypeDisplayMode mode) {
        if (mode == ThumbTypeDisplayMode.NORMAL) {
            if (thumbs != null && column < thumbs.size()) {
                return thumbs.get(column);
            }
        } else {
            thumbs = filterThumbs(thumbs, mode);
            if (thumbs != null && column < thumbs.size()) {
                return thumbs.get(column);
            }
        }
        return null;
    }

    private List<Thumbnail> filterThumbs(List<Thumbnail> thumbs, ThumbTypeDisplayMode mode) {
        List<Thumbnail> result = new ArrayList<Thumbnail>();
        for (Thumbnail t : thumbs) {
            switch (mode) {
                case COVER_FRONT:
                    if (t.getType() == ThumbType.COVER_FRONT) {
                        result.add(t);
                    }
                    break;
                case COVER_BACK:
                    if (t.getType() == ThumbType.COVER_BACK) {
                        result.add(t);
                    }
                    break;
                case THUMB_LISTS:
                    if (t.getType() == ThumbType.THUMBLIST) {
                        result.add(t);
                    }
                    break;
            }
        }
        return result;
    }

    private List<Thumbnail> loadThumbs(final long movieId) {
        return transactionHandler.doLoading(new TransactionBoundaryDelegator.LoaderBlock<List<Thumbnail>>() {

            @Override
            public List<Thumbnail> runLoadBlock(EntityManager em) {
                counter++;
                if (counter % CLEAR_LIMIT == 0) {
                    em.clear();
                }
                Query query = em.createQuery("from Thumbnail t where t.movieEntry.id = :movId");
                query.setParameter("movId", movieId);
                query.setHint(QueryHints.HINT_READONLY, true);
                List<Thumbnail> thumbs = query.getResultList();
                return thumbs;

            }
        });
    }

    public int numOfThumbs(final long movieId) {
        return loadThumbs(movieId).size();
    }
}
