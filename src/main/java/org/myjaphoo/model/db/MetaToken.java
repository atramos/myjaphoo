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
 * Metatoken, die zu Tokens zugeordnet werden können.
 * @author mla
 */
@Entity
@org.hibernate.annotations.Cache(usage =
org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class MetaToken implements Serializable, Comparable<MetaToken>, CacheableEntity, TreeStructure<MetaToken>, AttributedEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="table-hilo-generator")
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(length = 2024)
    private String description;

    /*
    zuordnung zu kindern machen wir selbst. sie ist deshalb transient
    )
     */
    @Transient
    transient private SortedSet<MetaToken> children = new TreeSet<MetaToken>();
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SELECT)
    @ManyToOne(fetch = FetchType.LAZY)
    private MetaToken parent;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TOKEN_METATOKEN",
    joinColumns = {
        @JoinColumn(name = "METATOKENS_ID", referencedColumnName = "ID")},
    inverseJoinColumns = {
        @JoinColumn(name = "TOKEN_ID", referencedColumnName = "ID")})
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    @org.hibernate.annotations.Cache(usage =
    org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
    private Set<Token> assignedTokens = new HashSet<Token>();

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
        if (!(object instanceof MetaToken)) {
            return false;
        }
        MetaToken other = (MetaToken) object;
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
    public Set<Token> getAssignedTokens() {
        return assignedTokens;
    }

    /**
     * @return the children
     */
    public SortedSet<MetaToken> getChildren() {
        return children;
    }

    /**
     * @return the parent
     */
    public MetaToken getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(MetaToken parent) {
        this.parent = parent;
    }

    @Override
    public int compareTo(MetaToken o) {
        return this.getName().compareTo(o.getName());
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
            MetaToken copy = (MetaToken) clone();
            // transiente listen auf leere listen setzen:
            copy.children = new TreeSet<MetaToken>();
            // shallow copy the reference lists:
            
            copy.setAssignedTokens(new TreeSet<Token>(assignedTokens));
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @param assignedTokens the assignedTokens to set
     */
    public void setAssignedTokens(Set<Token> assignedTokens) {
        this.assignedTokens = assignedTokens;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Set<? extends AttributedEntity> getReferences() {
        return Collections.EMPTY_SET;
    }
}
