/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache;

import org.myjaphoo.model.WmEntitySet;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.logic.exceptions.NonexistentEntityException;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author mla
 */
public interface EntityCacheActor {

    /** verzögere events, bis zum aufruf von fireAllAccumulatedEvents. */
    public void accumulateEvents();
    
    /** feuere alle aufgelaufenen events u. beende das anhäufen von events. */
    public void fireAllAccumulatedEvents();
    /**
     * Liefert eine immutables Modell der Entites.
     * Für dieses wird garantiert, dass es durch diese Caching-Klasse nicht
     * mehr geändert wird.
     * Gui-Komponenten können damit Threadsave darauf zugreifen/arbeiten.
     */
    public WmEntitySet getImmutableModel();



    void assignMetaTokenToToken(MetaToken metaToken, Token token);

    void assignToken2MovieEntry(final Token token, List<MovieEntry> movies);

    void createMetaToken(MetaToken mt, MetaToken parentToken);

    void createToken(Token token, Token parentToken);

    void editMetaToken(MetaToken mt);

    void editMovie(MovieEntry entry) throws NonexistentEntityException, Exception;

    void editToken(Token token);

    void moveMetaTokens(MetaToken tokenParent, MetaToken token2Move);

    void moveToken(final Token newParent, final Token token2Move);

    void removeMetaToken(MetaToken mt);

    void removeToken(final Token currentSelectedToken);

    void unAssignMetaTokenFromToken(MetaToken currMetaToken, List<Token> toks2Remove);

    void unassignTokenFromMovies(final Token token, final Collection<MovieEntry> movies);

    void resetImmutableCopy();

    public void resetInternalCache();

    public void removeMovieEntry(MovieEntry movieEntry);

    void newMovie(MovieEntry movieEntry);
}
