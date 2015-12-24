/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache;

import org.mlsoft.eventbus.GlobalBus;
import org.myjaphoo.model.cache.impl.PathMappingCacheImpl;



/**
 *
 * @author mla
 */
public class CacheManager {

    private static PathMappingCache pathMappingInstance = null;
    private static EntityCacheActor cacheActor = null;


    public static EntityCacheActor getCacheActor() {
        if (cacheActor == null) {
            cacheActor = new EntityCacheActorImpl(null, GlobalBus.bus);
        }
        return cacheActor;
    }

    public static PathMappingCache getPathMappingCache() {
        if (pathMappingInstance == null) {
            pathMappingInstance = new PathMappingCacheImpl();
        }
        return pathMappingInstance;
    }

    /**
     * For testing to add mocks.
     * @param cache
     */
    public static void setPathMappingCache(PathMappingCache cache) {
        pathMappingInstance = cache;
    }

    /**
     * For mocking during tests
     * @param aCacheActor the cacheActor to set
     */
    public static void setCacheActor(EntityCacheActor aCacheActor) {
        cacheActor = aCacheActor;
    }
}
