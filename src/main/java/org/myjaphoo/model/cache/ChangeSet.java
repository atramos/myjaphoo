/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache;

import java.util.Collection;
import org.myjaphoo.model.EntitySet;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;

/**
 * base class for all change events.
 * @author lang
 */
public class ChangeSet {

    private EntitySet<MovieEntry> movieEntrySet = new EntitySet<MovieEntry>();
    private EntitySet<Token> tokenSet = new EntitySet<Token>();
    private EntitySet<MetaToken> metaTokenSet = new EntitySet<MetaToken>();

    /**
     * @return the movieEntrySet
     */
    public EntitySet<MovieEntry> getMovieEntrySet() {
        return movieEntrySet;
    }

    /**
     * @return the tokenSet
     */
    public EntitySet<Token> getTokenSet() {
        return tokenSet;
    }

    /**
     * @return the metaTokenSet
     */
    public EntitySet<MetaToken> getMetaTokenSet() {
        return metaTokenSet;
    }

    public ChangeSet() {
    }

    public ChangeSet add(MovieEntry... entries) {
        getMovieEntrySet().addAll(entries);
        return this;
    }
    
    public ChangeSet addList(Collection<MovieEntry> entries) {
        add(entries.toArray(new MovieEntry[entries.size()]));
        return this;
    }

    public ChangeSet add(Token... tokens) {
        getTokenSet().addAll(tokens);
        return this;
    }

    public ChangeSet addTags(Collection<Token> tokens) {
        add(tokens.toArray(new Token[tokens.size()]));
        return this;
    }    
    
    public ChangeSet add(MetaToken... tokens) {
        getMetaTokenSet().addAll(tokens);
        return this;
    }

    public boolean contains(MovieEntry entry) {
        return getMovieEntrySet().find(entry) != null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + (movieEntrySet.size() > 0 ? movieEntrySet.asList(): "")
                + (tokenSet.size() > 0? tokenSet.asList() : "") 
                + (metaTokenSet.size() > 0? metaTokenSet.asList() : "");
    }
    
    
}
