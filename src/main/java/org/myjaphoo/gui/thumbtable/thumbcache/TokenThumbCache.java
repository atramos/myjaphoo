/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable.thumbcache;

import org.myjaphoo.gui.ThumbTypeDisplayMode;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;


/**
 * Spezieller Cache für Token thumbs. Dieser delegiert letztendlich nur
 * and den ThreadedthumbCache.
 *
 * @author mla
 */
public class TokenThumbCache {

    /**
     * mapped ein token immer auf den gleichen movie...
     */
    private HashMap<Long, MovieEntry> tokMap = new HashMap<Long, MovieEntry>();

    private HashMap<String, MovieEntry> tokByNameMap = new HashMap<>();

    private static TokenThumbCache instance = null;

    public static TokenThumbCache getInstance() {
        if (instance == null) {
            instance = new TokenThumbCache();
        }
        return instance;
    }

    private TokenThumbCache() {
    }

    public ImageIcon loadImageForToken(Token token, int actualRowHeight, ThumbIsLoadedCallback loadedCallBack) {
        MovieEntry entry = mapEntry(token);
        if (entry == null) {
            return null;
        }
        return ThreadedThumbCache.getInstance().getThumb(entry, 0, true, actualRowHeight, ThumbTypeDisplayMode.NORMAL, loadedCallBack);
    }

    public ImageIcon loadImageForToken(String tokenName, int actualRowHeight, ThumbIsLoadedCallback loadedCallBack) {
        MovieEntry entry = mapEntry(tokenName);
        if (entry == null) {
            return null;
        }
        return ThreadedThumbCache.getInstance().getThumb(entry, 0, true, actualRowHeight, ThumbTypeDisplayMode.NORMAL, loadedCallBack);
    }

    private MovieEntry mapEntry(Token token) {
        MovieEntry entry = tokMap.get(token.getId());
        if (entry != null) {
            return entry;
        }
        if (token.getMovieEntries().size() > 0) {
            entry = token.getMovieEntries().iterator().next();
            tokMap.put(token.getId(), entry);
            tokByNameMap.put(token.getName(), entry);
            return entry;
        } else {
            return null;
        }
    }

    private MovieEntry mapEntry(String tokenName) {
        MovieEntry entry = tokByNameMap.get(tokenName);
        if (entry != null) {
            return entry;
        }
        List<Token> tokList = CacheManager.getCacheActor().getImmutableModel().getTokenSet().asList();
        for (Token tok : tokList) {
            if (tokenName.equals(tok.getName())) {
                return mapEntry(tok);
            }
        }
        return null;
    }
}
