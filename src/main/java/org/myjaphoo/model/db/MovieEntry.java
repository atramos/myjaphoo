/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;
import org.myjaphoo.model.db.exif.ExifData;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

//import org.myjaphoo.gui.picmode.Picture;

/**
 * Main DB class for the movies.
 * @author lang
 */
@Entity
@org.hibernate.annotations.Cache(usage =
org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MovieEntry implements Serializable, CacheableEntity, AttributedEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="table-hilo-generator")
    private Long id;
    private String name;
    @Column(length = 1024)
    private String canonicalDir;
    @OneToMany(mappedBy = "movieEntry", fetch = FetchType.LAZY)
    @org.hibernate.annotations.Cache(usage =
    org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Thumbnail> thumbnails = new ArrayList<Thumbnail>();

    @Transient
    transient private Set<Token> tokens = new HashSet<Token>();
    @Index(name = "filIndex")
    private long fileLength;
    private Long checksumCRC32;
    @Enumerated(EnumType.ORDINAL)
    private Rating rating;
    @Embedded
    private MovieAttrs movieAttrs = new MovieAttrs();
    private String title;
    @Column(length = 4000)
    private String comment;
    
    @Embedded
    private ExifData exifData = new ExifData();

    @ElementCollection(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    @Column(name = "attributes", length = 4000)
    private Map<String,String> attributes = new HashMap<>();

    public Long getId() {
        return id;
    }

    /**
     * Liefert den ersten  thumbnail oder null, falls keiner existiert:
     * @return der erste thumbnail
     */
    public byte[] getThumbnail1() {
        if (thumbnails != null && thumbnails.size() > 0) {
            return thumbnails.get(0).getThumbnail();
        } else {
            return null;
        }
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
        if (!(object instanceof MovieEntry)) {
            return false;
        }
        MovieEntry other = (MovieEntry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MovieEntry[id=" + id + "]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = StringPool.pooledString(name);
    }

    /**
     * @return the canonicalPath
     */
    public String getCanonicalPath() {
        return canonicalDir + "/" + name;
    }

    /**
     * @return the canonicalPath
     */
    public String getCanonicalDir() {
        return canonicalDir;
    }

    /**
     * @param canonicalDir the canonicalPath to set
     */
    public void setCanonicalDir(String canonicalDir) {
        this.canonicalDir = StringPool.pooledString(canonicalDir);
    }

    /**
     * @return the fileLength
     */
    public long getFileLength() {
        return fileLength;
    }

    /**
     * @param fileLength the fileLength to set
     */
    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    /**
     * @return the thumbnails
     */
    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    /**
     * @return the tokens
     */
    public Set<Token> getTokens() {
        return tokens;
    }

    /**
     * @return the checksumCRC32
     */
    public Long getChecksumCRC32() {
        return checksumCRC32;
    }

    /**
     * @param checksumCRC32 the checksumCRC32 to set
     */
    public void setChecksumCRC32(Long checksumCRC32) {
        this.checksumCRC32 = checksumCRC32;
    }

    /**
     * @return the rating
     */
    public Rating getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(Rating rating) {
        this.rating = rating;
    }

    /**
     * Sets all string values to pooled string values.
     */
    @PostLoad
    public void poolStringValues() {
        canonicalDir = StringPool.pooledString(canonicalDir);
        name = StringPool.pooledString(name);
    }

    /**
     * @return the movieAttrs
     */
    public MovieAttrs getMovieAttrs() {
        if (movieAttrs == null) {
            setMovieAttrs(new MovieAttrs());
        }
        return movieAttrs;
    }

    /**
     * @param movieAttrs the movieAttrs to set
     */
    public void setMovieAttrs(MovieAttrs movieAttrs) {
        this.movieAttrs = movieAttrs;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * makes a shallow copy of this object.
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public Object partialClone() {
        try {
            MovieEntry copy = (MovieEntry) clone();
            if (movieAttrs != null) {
                copy.movieAttrs = (MovieAttrs) movieAttrs.clone();
            }
            if (exifData != null) {
                copy.exifData = (ExifData) exifData.clone();
            }
            
            // shallow copy the reference lists:
            //copy.thumbnails = new ArrayList<Thumbnail>(thumbnails);
            // tokens sind transient: nur neues leeres set anlegen:
            copy.tokens = new TreeSet<Token>();
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return the exifData
     */
    public ExifData getExifData() {
        if (exifData == null) {
            exifData = new ExifData();
        }
        return exifData;
    }

    /**
     * @param exifData the exifData to set
     */
    public void setExifData(ExifData exifData) {
        this.exifData = exifData;
    }

    /**
     * @param thumbnails the thumbnails to set
     */
    public void setThumbnails(List<Thumbnail> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Set<? extends AttributedEntity> getReferences() {
        return getTokens();
    }
}
