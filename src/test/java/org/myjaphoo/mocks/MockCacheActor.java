/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.mocks;

import org.myjaphoo.model.WmEntitySet;
import org.myjaphoo.model.cache.EntityCacheActor;
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
public class MockCacheActor implements EntityCacheActor{

    private WmEntitySet set ;

    public MockCacheActor(List<MovieEntry> movies, List<Token> tokens, List<MetaToken> metaTokens) {
        set = new WmEntitySet(movies, tokens, metaTokens);
    }
    

    @Override
    public WmEntitySet getImmutableModel() {
        return set;
    }

    @Override
    public void assignMetaTokenToToken(MetaToken metaToken, Token token) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void assignToken2MovieEntry(Token token, List<MovieEntry> movies) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public void editMetaToken(MetaToken mt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void editMovie(MovieEntry entry) throws NonexistentEntityException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void editToken(Token token) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void moveMetaTokens(MetaToken tokenParent, MetaToken token2Move) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void moveToken(Token newParent, Token token2Move) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeMetaToken(MetaToken mt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeToken(Token currentSelectedToken) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unAssignMetaTokenFromToken(MetaToken currMetaToken, List<Token> toks2Remove) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unassignTokenFromMovies(Token token, Collection<MovieEntry> movies) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetImmutableCopy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetInternalCache() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void accumulateEvents() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireAllAccumulatedEvents() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createMetaToken(MetaToken mt, MetaToken parentToken) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createToken(Token token, Token parentToken) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeMovieEntry(MovieEntry movieEntry) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void newMovie(MovieEntry movieEntry) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
