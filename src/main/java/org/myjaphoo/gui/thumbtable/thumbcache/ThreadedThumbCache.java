/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable.thumbcache;

import groovyx.gpars.actor.DynamicDispatchActor;
import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.gui.ThumbTypeDisplayMode;
import org.myjaphoo.gui.util.CachingHashMap;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.logic.MyjaphooDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Ein Thumb-Cache, der das Laden verzögert.
 * Wenn der Thumb nicht im Cache ist, dann wird das Laden durch einen parallelen
 * Actor durchgeführt, und erst mal null returniert.
 * Wenn der Thumb bereits im Cache ist, dann wird dieser geliefert.
 * Der Actor lädt im Hintergrund die Thumbs (durch separaten Entity-Manager) u.
 * liefert diesen an einen Zwischen-actor zurück. Dieser läuft auch parallel u.
 * macht folgendes:
 * er benachrichtigt alle callbacks u. benachrichtigt dann auch diesen Cache(er ist auch ein Actor, der allerdings nicht in
 * einem separaten Thread läuft). Dieser Cache trägt den ihn dann in seinem Cache ein.
 *
 * @author mla
 */
public class ThreadedThumbCache extends DynamicDispatchActor {

    private static Logger logger = LoggerFactory.getLogger(ThreadedThumbCache.class);
    /**
     * max. cache grösse.
     */
    private static final int maxCacheSize = MyjaphooAppPrefs.PRF_THUMBCACHE_CACHESIZE.getVal();
    private static boolean prefetchThumbs = MyjaphooAppPrefs.PRF_THUMBCACHE_PREFETCHTHUMBS.getVal();
    /**
     * der eigentliche cache
     */
    private CachingHashMap<CacheKey, ImageIcon> cache;
    /**
     * spezieller marker, der zeigt, dass der thumb gerade geladen wird.
     */
    private static final ImageIcon ISLOADING_MARKER = new ImageIcon();
    /**
     * spezieller marker, der zeigt, dass im cache null ist. (also es gibt kein thumb).
     */
    private static final ImageIcon NULL_MARKER = new ImageIcon();
    private PredictStructure predictStructure = null;
    /**
     * list of worker threads for thumb loading.
     */
    private List<ThumbLoadActor> loadActors = new ArrayList<>();

    private int loadActorsRoundRobin = 0;
    /**
     * Einen weiteren ThumbLoadActor verwenden wir für predicted Thumbs.
     * Das hat den Vorteil, dass wir den "Haupt" Loader nicht vollstopfen mit
     * Lade-Vorgängen für Thumbs, die erst mal sowiso nicht benötigt werden.
     */
    private ThumbLoadActor loadActorForPredictedThumbs;
    /**
     * Actor to inform any callbacks about finished thumb loads.
     */
    private NotifyLoadedActor loadResponseActor;
    private static ThreadedThumbCache instance = null;

    public static ThreadedThumbCache getInstance() {
        if (instance == null) {
            instance = new ThreadedThumbCache(MyjaphooDB.singleInstance().getConnection());
            instance.start();
        }
        return instance;
    }

    public ThreadedThumbCache(DBConnection dBConnection) {
        cache = new CachingHashMap<>(maxCacheSize);
        loadResponseActor = new NotifyLoadedActor();

        for (int i=0; i< Runtime.getRuntime().availableProcessors(); i++) {
            loadActors.add(new ThumbLoadActor(dBConnection));
        }
        loadActorForPredictedThumbs = new ThumbLoadActor(dBConnection);

        loadResponseActor.start();
        for (ThumbLoadActor loadActor : loadActors) {
            loadActor.start();
        }
        loadActorForPredictedThumbs.start();
    }

    public ImageIcon getThumb(MovieEntry entry, int column) {
        return getThumb(entry, column, false, null, ThumbTypeDisplayMode.NORMAL, null);
    }

    public ImageIcon getThumb(MovieEntry entry, int column, boolean center, Integer size, ThumbTypeDisplayMode mode, ThumbIsLoadedCallback loadCallBack) {
        ImageIcon icon = null;
        try {
            icon = internalgetThumb(entry.getId(), column, center, size, mode, loadCallBack);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (predictStructure != null) {
            predictStructure.fetchAround(entry.getId());
        }
        return icon;
    }

    private ImageIcon internalgetThumb(long id, int column, boolean center, Integer size, ThumbTypeDisplayMode mode, ThumbIsLoadedCallback loadCallBack) throws InterruptedException {
        // jetzt die aktuelle thumb abfrage behandeln:
        loadActorsRoundRobin += 1;
        loadActorsRoundRobin = loadActorsRoundRobin % loadActors.size();
        ImageIcon cachedImage = this.sendAndWait(new LoadMsg(id, column, center, size, mode, loadCallBack, loadActors.get(loadActorsRoundRobin)));
        return cachedImage;
    }

    private void onMessage(LoadMsg msg) {
        try {
            // jetzt die aktuelle thumb abfrage behandeln:
            CacheKey key = new CacheKey(msg.id, msg.column, msg.center, msg.size, msg.mode);
            ImageIcon cachedImage = cache.get(key);
            if (cachedImage == null) {
                msg.loadActorToUse.send(new ThumbLoadMsg(msg.id, msg.column, key, msg.center, msg.size, msg.mode, msg.loadCallBack));
                cache.put(key, ISLOADING_MARKER);
                replyIfExists(null);

            } else {
                if (cachedImage == ISLOADING_MARKER || cachedImage == NULL_MARKER) {
                    // wird gerade geladen, oder aber es gibt kein thumb hierfür: do nothing and return:
                    replyIfExists(null);
                } else {
                    replyIfExists(cachedImage);
                }
            }
        } catch (Exception e) {
            logger.error("error handling thumb load actor message!", e);
        }
    }

    protected void preLoadPredictedThumb(long id, int column, boolean center, Integer size, ThumbTypeDisplayMode mode) {
        send(new LoadMsg(id, column, center, size, mode, null, loadActorForPredictedThumbs));
    }

    /**
     * Fülle den cache im Hintergrund mit den gegebenen Movie entry thumbs.
     * Dies ist nützlich, wenn schon vorhersehbar ist, welche
     * thumbs womöglich demnächst gebraucht werden.
     * Die GUI kann dies z.b. nach dem Aufbau einens Thumbviews aufrufen.
     * Die Thumbs werden im Hintergrund mit "niedrigerer" prio geladen.
     * Das kann den Bildlauf erheblich ruhiger machen, da sonst immer alle Thumbs
     * verzögert geladen werden.
     */
    public void predict(Collection<MovieEntry> predictedEntries, boolean allThumbsOfEntry) {
        if (prefetchThumbs) {
            logger.info("predict " + predictedEntries.size() + " movies"); //NOI18N
            // collect them all in the ordered predict structure:
            predictStructure = new PredictStructure(this, predictedEntries, allThumbsOfEntry);

            // prefetch first n thumbs of the predict structure:
            predictStructure.preFetchThumbs();
        }
    }

    /**
     * private void delayLoad(CacheKey key, long id, int column, boolean center, Integer size) {
     * loadActor.sendMsg(new ThumbLoadMsg(id, column, key, loadResponseActor, center, size));
     * }
     */
    public void clearCache() {
        cache.clear();
    }

    public static void resetCache() {
        instance = null;
    }


    public void onMessage(ThumbNowLoadedMsg msg) {
        try {
            BufferedImage bi = msg.getBi();
            ImageIcon ii = null;
            // für null spezielle kennzeichnung verwenden:
            if (bi == null) {
                ii = NULL_MARKER;
            } else {
                ii = new ImageIcon(bi);
            }
            cache.put(msg.getKey(), ii);

            if (msg.getLoadCallBack() != null) {
                loadResponseActor.onMessage(msg);
            }
        } catch (Exception e) {
            logger.error("error processing thumb now loaded message!", e);
        }

    }


}
