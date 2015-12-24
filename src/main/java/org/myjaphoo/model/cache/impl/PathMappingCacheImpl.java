/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.impl;

import org.myjaphoo.model.cache.PathMappingCache;
import org.myjaphoo.model.db.PathMapping;
import org.myjaphoo.model.logic.PathMappingJpaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Observable;

/**
 *
 * @author lang
 */
public class PathMappingCacheImpl extends Observable implements PathMappingCache {

    public PathMappingCacheImpl() {
    }
    private final static Logger logger = LoggerFactory.getLogger(PathMappingCacheImpl.class.getName());
    private PathMappingJpaController pathJpa = new PathMappingJpaController();
    private List<PathMapping> allMappings = null;

    @Override
    public List<PathMapping> getCachedPathMappings() {

        if (allMappings == null) {
            allMappings = pathJpa.findPathMappingEntities();
        }
        return allMappings;
    }

    /**
     * Notifies, that the cache is invalid (e.g. due saving new movie entries) and needs to be reinitialized.
     * Note, that a reinitialisation is not necessary, if movie entries are changed, which are
     * got amd merged from this cache, since this objects are then already refreshed by hibernate.
     * Hauptfall für den Aufruf dieser Method dürfte als der movie import sein.
     */
    @Override
    public void resetCache() {
        allMappings = null;
        setChanged();
        notifyObservers(null);
    }
}
