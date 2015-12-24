/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import java.util.HashMap;

/**
 * base class for partial grouper, which are more efficient, if they cache paths.
 * The cache maps a string to a path.
 * @author mla
 */
public abstract class CachingPartialPathBuilder<KEY> extends AbstractPartialPathBuilder {

    private HashMap<KEY, Path> pathCache = new HashMap<KEY, Path>(20000);

    protected Path getPath(KEY string) {
        Path foundPath = pathCache.get(string);
        if (foundPath != null) {
            return foundPath;
        }
        Path created = createPath(string);
        pathCache.put(string, created);
        return created;

    }

    protected abstract Path createPath(KEY string);
}
