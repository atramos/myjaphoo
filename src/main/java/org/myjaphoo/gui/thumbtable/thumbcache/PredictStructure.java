/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable.thumbcache;

import java.util.Collection;

import org.myjaphoo.gui.ThumbTypeDisplayMode;

import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.model.db.MovieEntry;


/**
 *
 * @author mla
 */
public class PredictStructure {

    private ThreadedThumbCache cache;
    private static int DEFAULTMAXPREFETCH = MyjaphooAppPrefs.PRF_THUMBCACHE_MAXPREFETCH.getVal();
    private static int DEFAULTFETCHAROUNDDIST = MyjaphooAppPrefs.PRF_THUMBCACHE_FETCHAROUNDSIZE.getVal();
    private int MAXPREFETCH = DEFAULTMAXPREFETCH;
    private int FETCHAROUNDDIST = DEFAULTFETCHAROUNDDIST;
    private long[] orderedPredictedEntries;
    private boolean allThumbsOfEntry;
    
    /** max. cache grösse. */
    private static final int maxCacheSize = MyjaphooAppPrefs.PRF_THUMBCACHE_CACHESIZE.getVal();

    PredictStructure(ThreadedThumbCache cache, Collection<MovieEntry> predictedEntries, boolean allThumbsOfEntry) {
        this.cache = cache;
        this.allThumbsOfEntry = allThumbsOfEntry;
        orderedPredictedEntries = new long[predictedEntries.size()];
        int count = 0;
        for (MovieEntry entry : predictedEntries) {
            orderedPredictedEntries[count] = entry.getId();
            count++;
        }
        // set the fetch parameters:
        if (MAXPREFETCH >= maxCacheSize) {
            MAXPREFETCH = maxCacheSize / 5;
        }
        if (FETCHAROUNDDIST >= maxCacheSize) {
            FETCHAROUNDDIST = maxCacheSize / 10;
        }
        if (allThumbsOfEntry && FETCHAROUNDDIST * 5 >= maxCacheSize) {
            FETCHAROUNDDIST = maxCacheSize / 10;
        }
    }

    void preFetchThumbs() {
        for (int i = 0; i < MAXPREFETCH; i++) {
            prefetchEntry(i);
        }
    }

    /**
     * prefetch thumbs "around" or "in the near" of this entry.
     */
    void fetchAround(long id) {
        int indexPos = findPos(id);
        if (indexPos >= 0) {
            // fetch  n past this entry:
            for (int i = indexPos; i < indexPos + FETCHAROUNDDIST; i++) {
                prefetchEntry(i);
            }
        }
    }

    private int findPos(long id) {
        for (int i = 0; i < orderedPredictedEntries.length; i++) {
            if (orderedPredictedEntries[i] == id) {
                return i;
            }
        }
        return -1;
    }

    private void prefetchEntry(int i) {
        if (i < 0) {
            i = orderedPredictedEntries.length + i;
        }
        if (i > orderedPredictedEntries.length) {
            i = i - orderedPredictedEntries.length;
        }
        if (i >= 0 && i < orderedPredictedEntries.length) {
            cache.preLoadPredictedThumb(orderedPredictedEntries[i], 0, false, null, ThumbTypeDisplayMode.NORMAL);
            if (allThumbsOfEntry) {
                cache.preLoadPredictedThumb(orderedPredictedEntries[i], 1, false, null, ThumbTypeDisplayMode.NORMAL);
                cache.preLoadPredictedThumb(orderedPredictedEntries[i], 2, false, null, ThumbTypeDisplayMode.NORMAL);
                cache.preLoadPredictedThumb(orderedPredictedEntries[i], 3, false, null, ThumbTypeDisplayMode.NORMAL);
                cache.preLoadPredictedThumb(orderedPredictedEntries[i], 4, false, null, ThumbTypeDisplayMode.NORMAL);
            }
        }
    }
}
