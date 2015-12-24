package org.myjaphoo.model.logic;

import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.PathMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Cache for the faster substitution.
 * The cache is filled in parallel in the background and refreshed in scheduled intervals.
 * FasterFileSubstitution has therefore the benefit to delay free get always a cache.
 *
 * @author mla
 * @version $Id$
 */
public class FasterSubstitutionCache {

    /**
     * cache of only the substitutions, which exist as file in the file system.
     * This speeds up, because we do not have to always check all substitutions.
     */
    private List<PathMapping> cache = Collections.emptyList();

    private ScheduledExecutorService updateCacheTimerService = Executors.newScheduledThreadPool(1);

    public FasterSubstitutionCache() {
        updateCacheTimerService.schedule(new Runnable() {
            @Override
            public void run() {
                setCache(createCache());
            }
        }, 30, TimeUnit.SECONDS);
    }

    private List<PathMapping> createCache() {
        List<PathMapping> all = CacheManager.getPathMappingCache().getCachedPathMappings();
        List<PathMapping> result = new ArrayList<>();
        for (PathMapping pm : all) {
            if (fileExists(pm.getSubstitution())) {
                result.add(pm);
            }
        }
        return result;
    }

    private boolean fileExists(String filename) {
        File tester = new File(filename);
        return tester.exists();
    }

    public synchronized List<PathMapping> getCache() {
        return cache;
    }

    public synchronized void setCache(List<PathMapping> cache) {
        this.cache = cache;
    }
}
