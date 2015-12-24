/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.impl;

import org.apache.commons.lang.time.StopWatch;
import org.myjaphoo.model.WmEntitySet;
import org.myjaphoo.model.cache.AbstractCache;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.logic.MetaTokenJpaController;
import org.myjaphoo.model.logic.MovieEntryJpaController;
import org.myjaphoo.model.logic.MyjaphooDB;
import org.myjaphoo.model.logic.TokenJpaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Interner Cache, der ein WmEntitySet hält.
 * Dieses wird immer intern manipuliert. Nach aussen wird aber immer nur eine Kopie
 * dieses Sets geliefert.
 *
 * @author mla
 */
public class InternalCache extends AbstractCache<WmEntitySet> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalCache.class);
    private MovieEntryJpaController jpa;
    private TokenJpaController tokenJpa;
    private MetaTokenJpaController metatokenJpa;
    private int tokenassignments = 0;

    public InternalCache(DBConnection dbConn) {
        jpa = new MovieEntryJpaController(dbConn);
        tokenJpa = new TokenJpaController(dbConn);
        metatokenJpa = new MetaTokenJpaController(dbConn);
    }

    @Override
    protected WmEntitySet createCachedObject() {
        StopWatch watch = new StopWatch();
        watch.start();
        LOGGER.info("**** creating MovieEntryCache!");
        LOGGER.info("* loading movies...");
        List<MovieEntry> loadedList = jpa.fetchAll();

        LOGGER.info("* loading tokens...");
        tokenassignments = 0;
        List<Token> allTokens = tokenJpa.fetchAll();
        //Token cachedToken2 = buildTree(allTokens);
        //lazyLoadMovieRelation(cachedToken2);
        LOGGER.info("* loading meta tokens...");
        List<MetaToken> allMetaTokens = metatokenJpa.fetchAll();
        WmEntitySet set = new WmEntitySet(loadedList, allTokens, allMetaTokens);

        LOGGER.info("* building duplicate hash map...");
        set.buildTransientRelations();
        // clear first level cache after loading the data
        MyjaphooDB.singleInstance().emClear();
        watch.stop();
        LOGGER.info("**** finished creating internal entity cache! duration " + watch.toString());
        return set;
    }
}
