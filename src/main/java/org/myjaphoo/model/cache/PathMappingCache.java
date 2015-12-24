/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache;

import java.util.List;
import org.myjaphoo.model.db.PathMapping;

/**
 *
 * @author lang
 */
public interface PathMappingCache {

    public List<PathMapping> getCachedPathMappings();

    /**
     * Notifies, that the cache is invalid (e.g. due saving new movie entries) and needs to be reinitialized.
     * Note, that a reinitialisation is not necessary, if movie entries are changed, which are
     * got amd merged from this cache, since this objects are then already refreshed by hibernate.
     * Hauptfall für den Aufruf dieser Method dürfte als der movie import sein.
     */
    public void resetCache();
}
