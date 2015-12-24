/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable.thumbcache;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.myjaphoo.gui.ThumbTypeDisplayMode;


/**
 * Cache key: eindeutig für eine movie id, column, zentriert oder nicht u. der size.
 * @author mla
 */
public class CacheKey {

    private long id;
    private int column;
    private boolean center;
    private int size;
    private int precalculatedHashcode;
    private ThumbTypeDisplayMode mode;

    public CacheKey(long id, int column, boolean center, Integer isize, ThumbTypeDisplayMode mode) {
        this.id = id;
        this.column = column;
        this.center = center;
        this.mode = mode;
        // keine size-angabe (default) auf -1000 mappen, damit hashcode + equals einfacher sind
        this.size = isize != null ? isize.intValue() : -1000;
        precalculatedHashcode = new HashCodeBuilder().append(id).append(column).append(center).append(size).append(mode).toHashCode();
    }

    @Override
    public int hashCode() {
        return precalculatedHashcode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CacheKey) {
            CacheKey other = (CacheKey) obj;
            return id == other.id && column == other.column && center == other.center && size == other.size && mode == other.mode;

        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("column", column).append("center", center).append("size", size).append("mode", mode).toString(); //NOI18N
    }
}
