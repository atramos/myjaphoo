/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache;

import org.myjaphoo.model.logic.MyjaphooDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mla
 */
public abstract class AbstractCache<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCache.class.getName());
    /** der eigentliche Cache. Alle Tokens werden über diese Baumstruktur gehalten. */
    private volatile T cache = null;

    public T getCachedObject() {
        if (cache == null) {
            createCache();
        }
        return cache;
    }

    private void createCache() {
        synchronized (MyjaphooDB.singleInstance()) {
            if (cache == null) {
                LOGGER.info("recreate cached object");
                T cached2 = createCachedObject();
                cache = cached2;
                LOGGER.info("finished recreation of object!");
            }
        }
    }

    protected abstract T createCachedObject();

    /**
     * Notifies, that the cache is invalid (e.g. due saving new movie entries) and needs to be reinitialized.
     * Note, that a reinitialisation is not necessary, if movie entries are changed, which are
     * got amd merged from this cache, since this objects are then already refreshed by hibernate.
     * Hauptfall für den Aufruf dieser Method dürfte als der movie import sein.
     */
    public void resetCache() {
        LOGGER.info("**** resetting cache!");
        cache = null;
    }

    protected void setCache(T t) {
        cache = t;
    }
}
