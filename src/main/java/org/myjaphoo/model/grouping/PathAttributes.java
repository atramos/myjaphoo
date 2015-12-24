/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.db.StringPool;

/**
 * erweiterte Attribute, die mit einem Pfad gespeichert werden.
 *
 * @author mla
 */
public class PathAttributes {

    public static final String PATHMARKER_NOTHING = "nothing";
    private String groupingExpr;
    private String pathName;
    private String canonicalDir;

    /**
     * marker for this path for later use.
     */
    private String pathMarker = PATHMARKER_NOTHING;

    public PathAttributes(String groupingExpr, String pathName) {
        this.groupingExpr = groupingExpr;
        this.pathName = StringPool.pooledString(pathName);
    }

    public PathAttributes(String groupingExpr, String pathName, String canonicalDir, String pathMarker) {
        this.groupingExpr = groupingExpr;
        this.pathName = StringPool.pooledString(pathName);
        this.canonicalDir = StringPool.pooledString(canonicalDir);
        this.pathMarker = pathMarker;
    }

    /**
     * @return the groupingDim
     * @todo rename
     */
    public String getGroupingExpr() {
        return groupingExpr;
    }

    /**
     * @return the pathName
     */
    public String getPathName() {
        return pathName;
    }

    public static PathAttributes[] createAttributes(String groupingExpr, String... path) {
        PathAttributes[] attrs = new PathAttributes[path.length];
        for (int i = 0; i < path.length; i++) {
            attrs[i] = new PathAttributes(groupingExpr, path[i]);
        }
        return attrs;
    }

    public static PathAttributes[] createAttributes(GroupingDim groupingDim, String... path) {
        return createAttributes(groupingDim.name(), path);
    }

    /**
     * @return the canonicalDir
     */
    public String getCanonicalDir() {
        return canonicalDir;
    }

    public String getPathMarker() {
        return pathMarker;
    }
}
