/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable.thumbcache;

import org.myjaphoo.gui.ThumbTypeDisplayMode;


/**
 *
 * @author mla
 */
class ThumbLoadMsg {

    /** movie id.*/
    private Long id;
    /** column (index of thumb to load) */
    private int column;
    private CacheKey key;
    private boolean centerThumb = false;
    private Integer size = null;

    private ThumbIsLoadedCallback loadCallBack;
    private final ThumbTypeDisplayMode mode;

    ThumbLoadMsg(Long id, int column, CacheKey key, boolean centerThumb, Integer size, ThumbTypeDisplayMode mode, ThumbIsLoadedCallback loadCallBack) {
        this.id = id;
        this.column = column;
        this.key = key;
        this.centerThumb = centerThumb;
        this.size = size;
        this.loadCallBack = loadCallBack;
        this.mode = mode;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * @return the key
     */
    public CacheKey getKey() {
        return key;
    }

    /**
     * @return the centerThumb
     */
    public boolean isCenterThumb() {
        return centerThumb;
    }

    /**
     * @return the size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * @return the loadCallBack
     */
    public ThumbIsLoadedCallback getLoadCallBack() {
        return loadCallBack;
    }

    /**
     * @return the mode
     */
    public ThumbTypeDisplayMode getMode() {
        return mode;
    }


}
