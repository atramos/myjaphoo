/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.impl;

import org.myjaphoo.model.EntitySet;
import org.myjaphoo.model.WmEntitySet;
import org.myjaphoo.model.cache.AbstractCache;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.logic.EntityRelations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;


/**
 * Dies ist der HauptCache der Anwendung.
 * Intern wird ein Set aller Entites gehalten. Dieses interne Set wird bei Änderungen
 * manipuliert.
 * Dieser Cache liefert an die Controller u. GUI aber nur immer eine Kopie dieses Sets.
 * Diese Kopie kann somit als "immutable" betrachtet werden. Parallele Änderungen
 * wirken sich nicht auf dieses Set aus. Die GUI muss sich also ggf. nach
 * Änderung durch erneute Anfrage an diesen Cache eine aktualisierte Fassung
 * des Sets holen.
 * Damit kann garantiert werden, dass bei Änderungen keine nicht-deterministische
 * Fehler an den beteiligten Threads vorkommen. (idr. laufen Änderungen dieses Caches
 * in Hintergrundthreads, während die GUI bzw. Controller im EventDispatch Thread
 * laufen und ein Entity-Modell für die Anzeige benötigen).
 * 
 *
 * 
 *
 * @author mla
 */
public class EntityCache extends AbstractCache<WmEntitySet> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityCache.class);
    /** interner cache, der die daten hält. nach aussen liefern wir nur immer eine kopie dieser daten. (damit diese immer "immutable" für andere threads sind).
     *  Das verwalten dieser Kopie übernimmt diese Klasse.
     */
    private AbstractCache<WmEntitySet> internalCache = null;
    private int tokenassignments = 0;

    public AbstractCache<WmEntitySet> getInternalCache() {
        return internalCache;
    }

    public EntityCache(DBConnection dbConn) {
        internalCache = new InternalCache(dbConn);
    }

    /**
     * constructor for mocking.
     */
    public EntityCache(AbstractCache<WmEntitySet> internalCache) {
        this.internalCache = internalCache;
    }

    /**
     * Liefert eine immutables Modell der Entites.
     * Für dieses wird garantiert, dass es durch diese Caching-Klasse nicht
     * mehr geändert wird.
     * Gui-Komponenten können damit Threadsave darauf zugreifen/arbeiten.
     */
    public WmEntitySet getImmutableModel() {
        return getCachedObject();
    }

    /**
     * Zurücksetzen der (immutablen) kopie des caches. Nötig, nach jeder
     * Änderung am internen gecachten Datenmodell.
     */
    @Override
    public void resetCache() {
        LOGGER.info("**** reset immutable EntityCache!");
        super.resetCache();
    }

    /**
     * Dies liefert die kopie des internal caches. Diese Kopie wird
     * über diesen Cache gehalten u. verwaltet.
     * Sobald also Daten am internen cache geändert werden,
     * muss dieser Cache zurückgesetzt werden.
     * 
     */
    @Override
    protected WmEntitySet createCachedObject() {
        LOGGER.info("**** creating WmEnttiySet copy!");
        return internalCache.getCachedObject().deepCopyClone();
    }

    public void editMovie(MovieEntry movie) {
        final EntitySet<MovieEntry> entrySet = getInternalCache().getCachedObject().getMovieEntrySet();
        MovieEntry oldOne = entrySet.find(movie);
        // todo copy the attributes via jexl, see ComparisonContext.java
        oldOne.setName(movie.getName());
        oldOne.setCanonicalDir(movie.getCanonicalDir());
        oldOne.setChecksumCRC32(movie.getChecksumCRC32());
        oldOne.setComment(movie.getComment());
        oldOne.setFileLength(movie.getFileLength());
        oldOne.setRating(movie.getRating());
        oldOne.setTitle(movie.getTitle());
        oldOne.getMovieAttrs().setBitrate(movie.getMovieAttrs().getBitrate());
        oldOne.getMovieAttrs().setFormat(movie.getMovieAttrs().getFormat());
        oldOne.getMovieAttrs().setFps(movie.getMovieAttrs().getFps());
        oldOne.getMovieAttrs().setHeight(movie.getMovieAttrs().getHeight());
        oldOne.getMovieAttrs().setLength(movie.getMovieAttrs().getLength());
        oldOne.getMovieAttrs().setWidth(movie.getMovieAttrs().getWidth());
        oldOne.setAttributes(new HashMap<>(movie.getAttributes()));
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }

    public void removeToken(Token tok2) {
        final EntitySet<Token> tokenSet = getInternalCache().getCachedObject().getTokenSet();
        Token tok = tokenSet.find(tok2);
        EntityRelations.unlinkToken(tok);
        // aus liste entfernen:
        tokenSet.remove(tok);
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }

    public void unassignTokenFromMovies(Token token, Collection<MovieEntry> movies) {
        Token mergedToken = getInternalCache().getCachedObject().getTokenSet().find(token);
        for (MovieEntry entry : movies) {
            MovieEntry mergedEntry = getInternalCache().getCachedObject().getMovieEntrySet().find(entry);
            mergedEntry.getTokens().remove(mergedToken);
            mergedToken.getMovieEntries().remove(mergedEntry);
        }
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }

    public void assignToken2MovieEntry(Token token, List<MovieEntry> movies) {
        Token mergedToken = getInternalCache().getCachedObject().getTokenSet().find(token);
        for (MovieEntry entry : movies) {
            MovieEntry mergedEntry = getInternalCache().getCachedObject().getMovieEntrySet().find(entry);
            mergedEntry.getTokens().add(mergedToken);
            mergedToken.getMovieEntries().add(mergedEntry);
        }
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }

    public void createToken(Token token, Token parent) {
        final EntitySet<Token> tokenSet = getInternalCache().getCachedObject().getTokenSet();
        // add the token:
        tokenSet.add(token);
        // order the list:
        //Collections.sort(allTokens);
        // hang it under the root token:
        if (parent == null) {
            // use root:
            parent = getInternalCache().getCachedObject().getRootToken();
        } else {
            // get "merged" one from the current internal model
            parent = getInternalCache().getCachedObject().getTokenSet().find(parent);
        }
        parent.getChildren().add(token);
        token.setParent(parent);
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }

    public void editToken(Token token) {
        final EntitySet<Token> tokenSet = getInternalCache().getCachedObject().getTokenSet();
        Token oldOne = tokenSet.find(token);
        oldOne.setDescription(token.getDescription());
        oldOne.setName(token.getName());
        oldOne.setTokentype(token.getTokentype());
        oldOne.setAttributes(new HashMap<>(token.getAttributes()));
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }

    public void moveToken(Token newParent, Token token2Move) {
        final EntitySet<Token> tokenSet = getInternalCache().getCachedObject().getTokenSet();
        Token newParentInCache = tokenSet.find(newParent);
        Token token2MoveInCache = tokenSet.find(token2Move);
        Token oldParent = token2MoveInCache.getParent();
        oldParent.getChildren().remove(token2MoveInCache);
        token2MoveInCache.setParent(newParentInCache);
        newParentInCache.getChildren().add(token2MoveInCache);
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }

    public void removeMetaToken(MetaToken mt) {
        EntitySet<MetaToken> mtSet = getInternalCache().getCachedObject().getMetaTokenSet();
        MetaToken tokDel = mtSet.find(mt);
        EntityRelations.unlinkMetaToken(tokDel);
        mtSet.remove(tokDel);
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }

    public void removeMovieEntry(MovieEntry movieEntry) {
        EntitySet<MovieEntry> movieEntrySet = getInternalCache().getCachedObject().getMovieEntrySet();
        MovieEntry movToDel = movieEntrySet.find(movieEntry);
        EntityRelations.unlinkMovieEntry(movToDel);
        movieEntrySet.remove(movToDel);
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }    
    
    public void assignMetaTokenToToken(MetaToken metaToken, Token token) {
        final EntitySet<Token> tokenSet = getInternalCache().getCachedObject().getTokenSet();
        EntitySet<MetaToken> mtSet = getInternalCache().getCachedObject().getMetaTokenSet();
        Token mergedTok = tokenSet.find(token);
        MetaToken mergedMetaToken = mtSet.find(metaToken);
        EntityRelations.linkTokenToMetatoken(mergedTok, mergedMetaToken);
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }

    public void createMetaToken(MetaToken mt, MetaToken parent) {
        EntitySet<MetaToken> mtSet = getInternalCache().getCachedObject().getMetaTokenSet();
        mtSet.add(mt);
        //Collections.sort(allMetaTokens);
        if (parent == null) {
            parent = getInternalCache().getCachedObject().getRootMetaToken();
        } else {
            parent = getInternalCache().getCachedObject().getMetaTokenSet().find(parent);
        }
        parent.getChildren().add(mt);
        mt.setParent(parent);
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }

    public void editMetaToken(MetaToken mt) {
        EntitySet<MetaToken> mtSet = getInternalCache().getCachedObject().getMetaTokenSet();
        MetaToken oldOne = mtSet.find(mt);
        oldOne.setDescription(mt.getDescription());
        oldOne.setName(mt.getName());
        oldOne.setAttributes(new HashMap<>(mt.getAttributes()));
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }

    public void moveMetaTokens(MetaToken tokenParent, MetaToken token2Move) {
        EntitySet<MetaToken> mtSet = getInternalCache().getCachedObject().getMetaTokenSet();
        MetaToken mergedtokenParent = mtSet.find(tokenParent);
        MetaToken mergedtoken2Move = mtSet.find(token2Move);
        EntityRelations.move(mergedtoken2Move, mergedtokenParent);
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }

    public void unAssignMetaTokenFromToken(MetaToken metaToken, List<Token> toks2Remove) {
        final EntitySet<Token> tokenSet = getInternalCache().getCachedObject().getTokenSet();
        EntitySet<MetaToken> mtSet = getInternalCache().getCachedObject().getMetaTokenSet();
        MetaToken mergedMetaTok = mtSet.find(metaToken);
        for (Token token : toks2Remove) {
            Token mergedTok = tokenSet.find(token);
            EntityRelations.unlinkTokenFromMetatoken(mergedTok, mergedMetaTok);
        }
        // immutable copie des datenmodells zurücksetzen:
        resetCache();
    }


    public void newMovie(MovieEntry movieEntry) {
        getInternalCache().getCachedObject().getMovieEntrySet().add(movieEntry);
        resetCache();
    }
}
