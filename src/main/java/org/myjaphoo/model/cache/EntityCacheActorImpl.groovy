/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache

import groovy.transform.CompileStatic
import groovyx.gpars.activeobject.ActiveMethod
import groovyx.gpars.activeobject.ActiveObject;
import org.mlsoft.eventbus.DelayableEventBus;
import org.mlsoft.eventbus.EventBus;
import org.myjaphoo.model.WmEntitySet;
import org.myjaphoo.model.cache.events.*;
import org.myjaphoo.model.cache.impl.EntityCache;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.logic.MetaTokenJpaController;
import org.myjaphoo.model.logic.MovieEntryJpaController;
import org.myjaphoo.model.logic.TokenJpaController;
import org.myjaphoo.model.logic.exceptions.NonexistentEntityException;

import java.util.Collection;
import java.util.List;



/**
 * Diese Klasse ist für Änderungen an den Daten zuständig.
 * Sie macht generell zwei Dinge:
 *  - Die Ändeurngen persistent über die entsprechenden DAO Klassen ansteuern
 *  - Die Änderungen im entsprechenden Cache nachziehen.
 *  - nach jeder internen Cache Änderung wird der kopierte (immutable) 
 *
 * Dadurch sollte nach jeder Änderung die Datenbank u. das Cache-Modell
 * synchron sein.
 * Alle Manipulationszugriffe aus der GUI sollten also immer über diese Klasse erfolgen,
 * damit gewährleistet ist, dass der Cache aktuell ist; zugriff direkt über DAO
 * Klassen sollte vermieden werden.
 *
 * Diese Zugriffsklasse wird als Actor zur Verfügung gestellt.
 * Alle Aktionen die über das Interface implementiert werden,
 * werden in einem Actor asynchron abgearbeitet.
 *
 * @author lang
 */
@ActiveObject
public class EntityCacheActorImpl implements EntityCacheActor {

    private DBConnection dbConn;
    private TokenJpaController tjpa;
    private MovieEntryJpaController jpa;
    private MetaTokenJpaController mtjpa;
    /** cache, den der Actor benutzt. */
    private EntityCache cache;

    public EntityCacheActorImpl(DBConnection dbConn, EventBus eventBus) {
        this.dbConn = dbConn;
        tjpa = new TokenJpaController(dbConn);
        jpa = new MovieEntryJpaController(dbConn);
        mtjpa = new MetaTokenJpaController(dbConn);
        cache = new EntityCache((DBConnection) dbConn);
        this.eventBus = new DelayableEventBus(eventBus);
    }

    private DelayableEventBus eventBus;

    @Override
    @ActiveMethod
    public void editMovie(MovieEntry entry) throws NonexistentEntityException, Exception {
        jpa.edit(entry);
        cache.editMovie(entry);
        eventBus.post(new MoviesChangedEvent(entry));
    }

    @Override
    @ActiveMethod
    public void removeToken(final Token currentSelectedToken) {
        tjpa.removeToken(currentSelectedToken);
        // cache aktualiseren:
        cache.removeToken(currentSelectedToken);
        eventBus.post(new TagsDeletedEvent(currentSelectedToken));
    }

    @Override
    @ActiveMethod
    public void removeMovieEntry(MovieEntry movieEntry) {
        jpa.removeMovieEntry(movieEntry);
        cache.removeMovieEntry(movieEntry);
        eventBus.post(new MoviesRemovedEvent(movieEntry));
    }

    @Override
    @ActiveMethod
    public void newMovie(MovieEntry movieEntry) {
        jpa.create(movieEntry);
        cache.newMovie(movieEntry);
        eventBus.post(new MoviesChangedEvent(movieEntry));
    }

    @Override
    @ActiveMethod
    public void unassignTokenFromMovies(final Token token, final Collection<MovieEntry> movies) {
        tjpa.unassignTokenFromMovies(token, movies);
        cache.unassignTokenFromMovies(token, movies);
        eventBus.post(new TagsUnassigendEvent(token).addList(movies));
    }

    @Override
    @ActiveMethod
    public void assignToken2MovieEntry(final Token token, List<MovieEntry> movies) {
        tjpa.assignToken2MovieEntry(token, movies);
        cache.assignToken2MovieEntry(token, movies);
        eventBus.post(new TagsAssignedEvent(token).addList(movies));
    }

    @Override
    @ActiveMethod
    public void createToken(Token token, Token parentToken) {
        if (parentToken == null) {
            parentToken = tjpa.findRootToken();
        }
        tjpa.create(token, parentToken);
        cache.createToken(token, parentToken);
        eventBus.post(new TagsAddedEvent(token));
    }

    @Override
    @ActiveMethod
    public void editToken(Token token) {
        tjpa.edit(token);
        cache.editToken(token);
        eventBus.post(new TagsChangedEvent(token));
    }

    @Override
    @ActiveMethod
    public void moveToken(final Token newParent, final Token token2Move) {
        tjpa.moveToken(newParent, token2Move);
        cache.moveToken(newParent, token2Move);
        eventBus.post(new TagsChangedEvent(newParent, token2Move));
    }

    @Override
    @ActiveMethod
    public void removeMetaToken(MetaToken mt) {
        mtjpa.removeToken(mt);
        cache.removeMetaToken(mt);
        eventBus.post(new MetaTagsDeletedEvent(mt));
    }

    @Override
    @ActiveMethod
    public void assignMetaTokenToToken(MetaToken metaToken, Token token) {
        mtjpa.assignTok(token, metaToken);
        cache.assignMetaTokenToToken(metaToken, token);
        eventBus.post(new MetaTagsAssignedEvent(metaToken).add(token));
    }

    @Override
    @ActiveMethod
    public void createMetaToken(MetaToken mt, MetaToken parentToken) {
        if (parentToken == null) {
            parentToken = mtjpa.findRootToken();
        }
        mtjpa.create(mt, parentToken);
        cache.createMetaToken(mt, parentToken);
        eventBus.post(new MetaTagsAddedEvent(mt));
    }

    @Override
    @ActiveMethod
    public void editMetaToken(MetaToken mt) {
        mtjpa.edit(mt);
        cache.editMetaToken(mt);
        eventBus.post(new MetaTagsChangedEvent(mt));
    }

    @Override
    @ActiveMethod
    public void moveMetaTokens(MetaToken tokenParent, MetaToken token2Move) {
        mtjpa.moveToken(tokenParent, token2Move);
        cache.moveMetaTokens(tokenParent, token2Move);
        eventBus.post(new MetaTagsChangedEvent(tokenParent, token2Move));
    }

    @Override
    @ActiveMethod
    public void unAssignMetaTokenFromToken(MetaToken currMetaToken, List<Token> toks2Remove) {
        mtjpa.unassignMetaToken(currMetaToken, toks2Remove);
        cache.unAssignMetaTokenFromToken(currMetaToken, toks2Remove);
        eventBus.post(new MetaTagsUnassignedEvent(currMetaToken).addTags(toks2Remove));
    }

    @Override
    @ActiveMethod(blocking = true)
    public WmEntitySet getImmutableModel() {
        return cache.getImmutableModel();
    }

    @Override
    @ActiveMethod
    public void resetImmutableCopy() {
        cache.resetCache();
    }

    @Override
    @ActiveMethod
    public void resetInternalCache() {
        cache.getInternalCache().resetCache();
    }

    @Override
    @ActiveMethod
    public void accumulateEvents() {
        eventBus.accumulateEvents();
    }

    @Override
    @ActiveMethod
    public void fireAllAccumulatedEvents() {
        eventBus.fireAllAccumulatedEvents();
    }


}
