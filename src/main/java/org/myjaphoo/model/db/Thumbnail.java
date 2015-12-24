/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author mla
 */
@Entity
@org.hibernate.annotations.Cache(usage =
    org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
)
public class Thumbnail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="table-hilo-generator")
    private Long id;

    @Lob
    @Column(length=1000000000)
    private byte[] thumbnail;

    private int w;

    private int h;
    
    private ThumbType type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private MovieEntry movieEntry;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Thumbnail)) {
            return false;
        }
        Thumbnail other = (Thumbnail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "wankman.model.db.Thumbnail[id=" + id + "]";
    }

    /**
     * @return the thumbnail
     */
    public byte[] getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * @return the w
     */
    public int getW() {
        return w;
    }

    /**
     * @param w the w to set
     */
    public void setW(int w) {
        this.w = w;
    }

    /**
     * @return the h
     */
    public int getH() {
        return h;
    }

    /**
     * @param h the h to set
     */
    public void setH(int h) {
        this.h = h;
    }

    /**
     * @return the movieEntry
     */
    public MovieEntry getMovieEntry() {
        return movieEntry;
    }

    /**
     * @param movieEntry the movieEntry to set
     */
    public void setMovieEntry(MovieEntry movieEntry) {
        this.movieEntry = movieEntry;
    }

    /**
     * @return the type
     */
    public ThumbType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ThumbType type) {
        this.type = type;
    }

}
