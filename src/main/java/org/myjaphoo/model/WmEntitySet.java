/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model;

import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Beinhaltet alle Wm Entities inclusive ihrer Relationen.
 * Also ein Graph aller verknüpften entities.
 * 
 * @author mla
 */
public class WmEntitySet implements Cloneable {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmEntitySet.class);
    private EntitySet<MovieEntry> movieEntrySet;
    private EntitySet<Token> tokenSet;
    private EntitySet<MetaToken> metaTokenSet;
    private DuplicateHashMap dupHashMap = null;


    private Token rootToken;

    private MetaToken rootMetaToken;

    public WmEntitySet(List<MovieEntry> movies, List<Token> toks, List<MetaToken> mtoks) {
        movieEntrySet = new EntitySet<MovieEntry>(movies);
        tokenSet = new EntitySet<Token>(toks);
        metaTokenSet = new EntitySet<MetaToken>(mtoks);
        dupHashMap = new DuplicateHashMap(movies);
    }

    public List<MovieEntry> getMovies() {
        return getMovieEntrySet().asList();
    }

    public void buildTransientRelations() {

        Collection<Token> tokens = getTokenSet().asList();
        LOGGER.info("* init transient movie.token relation...");
        // init vom transienten field tokens in den movies:
        for (Token token : tokens) {
            for (MovieEntry entry : token.getMovieEntries()) {
                entry.getTokens().add(token);
            }
        }
        rootToken = buildTree(tokens);
        rootMetaToken = buildMetaTokenTree(getMetaTokenSet().asList());
        lazyLoadTokenRelation(getRootMetaToken());
    }

    private Token buildTree(Collection<Token> allTokens) {
        // children attribut ist transient: hier wird es von allen tokens aus dem parent gesetzt:
        Token root = null;
        for (Token token : allTokens) {
            if (token.getParent() != null) {
                token.getParent().getChildren().add(token);
            } else {
                root = token;
            }
        }
        return root;
    }

    private MetaToken buildMetaTokenTree(Collection<MetaToken> allMetaTokens) {
        MetaToken root = null;
        for (MetaToken metaToken : allMetaTokens) {
            if (metaToken.getParent() != null) {
                metaToken.getParent().getChildren().add(metaToken);
            } else {
                root = metaToken;
            }
        }
        return root;
    }

    private void lazyLoadTokenRelation(MetaToken metaToken) {
        for (Token token : metaToken.getAssignedTokens()) {
            token.getAssignedMetaTokens().add(metaToken);
        }
        if (metaToken.getChildren().size() > 0) {
            for (MetaToken child : metaToken.getChildren()) {
                lazyLoadTokenRelation(child);
            }
        }
    }

    /**
     * makes a shallow copy of this object.
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    /**
     * Erstellt eine tiefe komplette Kopie des gesamten Objekt-Grapfhen:
     * Alle Objekte werden kopiert, alle Referenzen werden nachgezogen,
     * dass sie auf den kopierten Graphen verweisen.
     * Messungen: zeit für >300000 movies: ca. <=200ms; speicherverbrauch ca. 200MB
     */
    public WmEntitySet deepCopyClone() {
        LOGGER.info("create deep copy, refreshing immutable entity model");
        try {
            WmEntitySet copy = (WmEntitySet) clone();
            // make deep copies of the sets:
            copy.movieEntrySet = getMovieEntrySet().partialCopyClone();
            copy.tokenSet = getTokenSet().partialCopyClone();
            copy.metaTokenSet = getMetaTokenSet().partialCopyClone();
            copy.dupHashMap = new DuplicateHashMap(copy.movieEntrySet.asList());
            // the entity objects itself are only shallow cloned,
            // we have now to fix all the references to all the cloned objects:
            for (Token token : copy.getTokenSet().asList()) {
                copy.fixReferences(token);
            }
            for (MetaToken mt : copy.getMetaTokenSet().asList()) {
                copy.fixReferences(mt);
            }

            // build up all transient relations based on the main relations:
            copy.buildTransientRelations();
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Biegt alle (Haupt)-Referenzen dieses Tokens auf Objekte aus diesem Graphen
     * (aus diesem WmEntitySet) um. Transiente Referenzen werden nicht geändert.
     * @param token
     */
    private void fixReferences(Token token) {
        // parent referenz umbiegen:
        token.setParent(getTokenSet().find(token.getParent()));
        // movie referenzen umbiegen:
        Set<MovieEntry> movies = new HashSet<MovieEntry>();
        for (MovieEntry entry : token.getMovieEntries()) {
            movies.add(getMovieEntrySet().find(entry));
        }
        token.setMovieEntries(movies);
    }

    /**
     * Biegt alle (Haupt)-Referenzen dieses MetaTokens auf Objekte aus diesem Graphen
     * (aus diesem WmEntitySet) um. Transiente Referenzen werden nicht geändert.
     * @param token
     */
    private void fixReferences(MetaToken mt) {
        // parent:
        mt.setParent(getMetaTokenSet().find(mt.getParent()));
        // assigned tokens:
        Set<Token> tokens = new HashSet<Token>();
        for (Token token : mt.getAssignedTokens()) {
            tokens.add(getTokenSet().find(token));
        }
        mt.setAssignedTokens(tokens);
    }

    /**
     * @return the rootToken
     */
    public Token getRootToken() {
        return rootToken;
    }

    /**
     * @return the rootMetaToken
     */
    public MetaToken getRootMetaToken() {
        return rootMetaToken;
    }

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

    /**
     * @return the dupHashMap
     */
    public DuplicateHashMap getDupHashMap() {
        return dupHashMap;
    }
}
