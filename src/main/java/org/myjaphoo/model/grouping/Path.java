/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import java.util.List;

/**
 *
 * @author mla
 */
public class Path {

    private PathAttributes[] pathAttributes;
    private int lastIndex;


    public Path(GroupingDim groupingDim, String... path) {
        this(PathAttributes.createAttributes(groupingDim.name(), path));
    }

    public Path(String source, String... path) {
        this(PathAttributes.createAttributes(source, path));
    }

    public Path(GroupingDim groupingDim, List<String> path) {
        this(PathAttributes.createAttributes(groupingDim.name(), path.toArray(new String[path.size()])));
    }

    public Path(String source, List<String> path) {
        this(PathAttributes.createAttributes(source, path.toArray(new String[path.size()])));
    }

    public Path(Path p1, Path p2) {
        PathAttributes[] merged = new PathAttributes[p1.lastIndex + p2.lastIndex + 2];
        System.arraycopy(p1.pathAttributes, 0, merged, 0, p1.lastIndex + 1);
        System.arraycopy(p2.pathAttributes, 0, merged, p1.lastIndex + 1, p2.lastIndex + 1);
        pathAttributes = merged;
        lastIndex = merged.length - 1;
    }

    /**
     * Constructor to create a parent path for a path.
     */
    private Path(PathAttributes[] pathAttributes, int lastIndex) {
        this.lastIndex = lastIndex;
        this.pathAttributes = pathAttributes;
    }

    public Path(PathAttributes[] pathAttributes) {
        this(pathAttributes, pathAttributes.length - 1);
    }

    @Override
    public boolean equals(Object obj) {
        Path other = (Path) obj;
        if (lastIndex != other.lastIndex) {
            return false;
        }
        for (int i = 0; i <= lastIndex; i++) {
            if (!pathAttributes[i].getPathName().equals(other.pathAttributes[i].getPathName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i <= lastIndex; i++) {
            result = 31 * result + pathAttributes[i].getPathName().hashCode();
        }
        return result;
    }

    public String getLastPathName() {
        return pathAttributes[lastIndex].getPathName();
    }

    public String getLastPathBuilder() {
        return pathAttributes[lastIndex].getGroupingExpr();
    }

    public String getPathMarker() {
        return pathAttributes[lastIndex].getPathMarker();
    }

    /**
     * @return the canonicalDir
     */
    public String getCanonicalDir() {
        return pathAttributes[lastIndex].getCanonicalDir();
    }

    public Path getParentPath() {
        return new Path(pathAttributes, lastIndex - 1);
    }

    public boolean isRoot() {
        return lastIndex < 0;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i <= lastIndex; i++) {
            b.append("|");
            b.append(pathAttributes[i].getPathName());
        }
        return b.toString();
    }

}
