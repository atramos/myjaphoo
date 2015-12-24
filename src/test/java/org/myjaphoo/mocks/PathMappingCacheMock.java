/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.mocks;

import java.util.ArrayList;
import java.util.List;
import org.myjaphoo.model.cache.PathMappingCache;
import org.myjaphoo.model.db.PathMapping;

/**
 *
 * @author mla
 */
public class PathMappingCacheMock implements PathMappingCache {

    private List<PathMapping> mappings = new ArrayList<PathMapping>();

    public PathMappingCacheMock() {
    }

    public PathMappingCacheMock(List<PathMapping> mappings) {
        this.mappings = mappings;
    }

    @Override
    public List<PathMapping> getCachedPathMappings() {
        return mappings;
    }

    @Override
    public void resetCache() {
    }
}
