/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable.thumbcache;

import java.awt.image.BufferedImage;

/**
 *
 * @author mla
 */
public class ThumbNowLoadedMsg {

    private CacheKey key;
    private BufferedImage bi;
    private long movieentry_id;
    private ThumbIsLoadedCallback loadCallBack;

    ThumbNowLoadedMsg(CacheKey key, BufferedImage bi, long movieentry_id, ThumbIsLoadedCallback loadCallBack) {
        this.key = key;
        this.bi = bi;
        this.movieentry_id = movieentry_id;
        this.loadCallBack = loadCallBack;
    }

    /**
     * @return the key
     */
    public CacheKey getKey() {
        return key;
    }

    /**
     * @return the bi
     */
    public BufferedImage getBi() {
        return bi;
    }

    /**
     * @return the movieentry_id
     */
    public long getMovieentry_id() {
        return movieentry_id;
    }

    /**
     * @return the loadCallBack
     */
    public ThumbIsLoadedCallback getLoadCallBack() {
        return loadCallBack;
    }


}
