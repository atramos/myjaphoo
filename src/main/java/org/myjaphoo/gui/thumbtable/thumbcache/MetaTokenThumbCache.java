package org.myjaphoo.gui.thumbtable.thumbcache;

import org.myjaphoo.gui.ThumbTypeDisplayMode;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;

import javax.swing.*;
import java.util.HashMap;

/**
 * Cache to get thumbs for meta tags.
 * Delegates to the regular thumb cache, but ensures, that always the same thumb is returned for a meta tag.
 */
public class MetaTokenThumbCache {
    /** mapped ein token immer auf den gleichen movie...*/
    private HashMap<Long, MovieEntry> tokMap = new HashMap<Long, MovieEntry>();
    private static MetaTokenThumbCache instance = null;

    public static MetaTokenThumbCache getInstance() {
        if (instance == null) {
            instance = new MetaTokenThumbCache();
        }
        return instance;
    }

    private MetaTokenThumbCache() {
    }

    public Icon loadImageForToken(MetaToken token, int actualRowHeight, ThumbIsLoadedCallback loadedCallBack) {
        MovieEntry entry = mapEntry(token);
        if (entry == null) {
            return null;
        }
        return ThreadedThumbCache.getInstance().getThumb(entry, 0, true, actualRowHeight, ThumbTypeDisplayMode.NORMAL, loadedCallBack);
    }
    private MovieEntry mapEntry(MetaToken token) {
        MovieEntry entry = tokMap.get(token.getId());
        if (entry != null) {
            return entry;
        }
        entry = findNextBestEntry(token);
        if (entry != null) {
            tokMap.put(token.getId(), entry);
            return entry;
        } else {
            return null;
        }
    }

    private MovieEntry findNextBestEntry(MetaToken metatoken) {
        for (Token token : metatoken.getAssignedTokens()) {
            if (token.getMovieEntries().size() > 0) {
                return token.getMovieEntries().iterator().next();
            }
        }
        return null;
    }

}
