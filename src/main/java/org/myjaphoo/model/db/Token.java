/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.mlsoft.structures.TreeStructure;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author mla
 */
@Entity
@org.hibernate.annotations.Cache(usage =
org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Token implements Serializable, Comparable<Token>, CacheableEntity, TreeStructure<Token>, AttributedEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="table-hilo-generator")
    private Long id;
    @Column(unique = true)
    private String name;
    @Enumerated(EnumType.ORDINAL)
    private TokenType tokentype = TokenType.UNBESTIMMT;
    @Column(length = 2024)
    private String description;

    /*
    wg. geschwindigkeit sind die children alle transient, u. werden
     * von uns selbst beim laden zugeordnet.
    )
     */
    @Transient
    transient private SortedSet<Token> children = new TreeSet<Token>();
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SELECT)
    @ManyToOne(fetch = FetchType.LAZY)
    private Token parent;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TOKEN_MOVIEENTRY",
    joinColumns = {
        @JoinColumn(name = "TOKENS_ID", referencedColumnName = "ID")},
    inverseJoinColumns = {
        @JoinColumn(name = "MOVIEENTRIES_ID", referencedColumnName = "ID")})
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    @org.hibernate.annotations.Cache(usage =
    org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
    private Set<MovieEntry> movieEntries = new HashSet<MovieEntry>();
    @Transient
    transient private Set<MetaToken> assignedMetaTokens = new HashSet<MetaToken>();

    @ElementCollection(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    @Column(name = "attributes", length = 4000)
    private Map<String,String> attributes = new HashMap<>();

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
        if (!(object instanceof Token)) {
            return false;
        }
        Token other = (Token) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public String getComment() {
        return description;
    }

    @Override
    public void setComment(String str) {
        this.description = str;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the movieEntries
     */
    public Set<MovieEntry> getMovieEntries() {
        return movieEntries;
    }

    /**
     * @return the meta tokens
     */
    public Set<MetaToken> getAssignedMetaTokens() {
        return assignedMetaTokens;
    }

    /**
     * @return the children
     */
    public SortedSet<Token> getChildren() {
        return children;
    }

    /**
     * @return the parent
     */
    public Token getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Token parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(Token o) {
        return this.getName().compareTo(o.getName());
    }

    public long sizeOfMovies() {
        long size = 0;
        for (MovieEntry entry : movieEntries) {
            size += entry.getFileLength();
        }
        return size;
    }

    /**
     * @return the tokentype
     */
    public TokenType getTokentype() {
        return tokentype;
    }

    /**
     * @param tokentype the tokentype to set
     */
    public void setTokentype(TokenType tokentype) {
        this.tokentype = tokentype;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
            Token copy = (Token) clone();
            // transiente referenzlisten auf leer setzten:
            copy.children = new TreeSet<Token>();
            copy.assignedMetaTokens = new HashSet<MetaToken>();
            // shallow copy the reference lists:
            copy.setMovieEntries(new HashSet<MovieEntry>(movieEntries));
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @param movieEntries the movieEntries to set
     */
    public void setMovieEntries(Set<MovieEntry> movieEntries) {
        this.movieEntries = movieEntries;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Set<? extends AttributedEntity> getReferences() {
        return getAssignedMetaTokens();
    }

}
