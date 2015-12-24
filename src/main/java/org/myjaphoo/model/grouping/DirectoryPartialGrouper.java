/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;


/**
 * Gruppiert nach der Tokenhierarchie und dann nach directories.
 * @author mla
 */
public class DirectoryPartialGrouper extends AbstractPartialPathBuilder {

    public static final Logger LOGGER = LoggerFactory.getLogger(DirectoryPartialGrouper.class.getName());
    private HashMap<String, Path[]> pathCache = new HashMap<String, Path[]>(80000);

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        return getPath(row.getEntry().getCanonicalDir());
    }


    public final Path[] getPath(String dir) {
        Path[] cachedPath = pathCache.get(dir);
        if (cachedPath != null) {
            return cachedPath;
        }

        Path[] result = new Path[1];
        result[0] = new Path(createAttributes(dir));
        pathCache.put(dir, result);
        return result;
    }

    private PathAttributes[] createAttributes(String dir) {
        if (dir == null) {
            return new PathAttributes[0];
        }
        String[] path = Helper.splitPathName(dir);
        PathAttributes[] attrs = new PathAttributes[path.length];
        File f = new File(dir);
        for (int i = path.length - 1; i >= 0; i--) {
            String canonicalPath = null;

            if (f != null) {

                canonicalPath = f.getAbsolutePath();

                f = f.getParentFile();
            }

            attrs[i] = new PathAttributes(GroupingDim.Directory.name(), path[i], canonicalPath, PathAttributes.PATHMARKER_NOTHING);

        }
        return attrs;
    }

    @Override
    public boolean needsTagRelation() {
        return false;
    }

    @Override
    public boolean needsMetaTagRelation() {
        return false;
    }
}
