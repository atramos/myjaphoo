/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

import java.util.HashSet;
import java.util.Set;

/**
 * Gruppiert nach der auto-keywords, die aus dem pfad gebildet werden.
 * @author mla
 */
public class AbstractAutoKeyWordPartialGrouper extends CachingPartialPathBuilder<String> {

    private int minLength;
    private String separators;

    public AbstractAutoKeyWordPartialGrouper(int minLength, String separators) {
        this.minLength = minLength;
        this.separators = separators;
    }

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        return getPaths(row.getEntry().getCanonicalPath());
    }


    public final Path[] getPaths(String path) {
        Set<String> parts = new HashSet<String>();

        String[] hints = org.apache.commons.lang.StringUtils.split(path.toLowerCase(), separators);
        for (String hint : hints) {
            if (hint.length() > minLength) {
                parts.add(hint);
            }
        }
        Path[] result = new Path[parts.size()];
        String[] partsarr = parts.toArray(new String[parts.size()]);
        for (int i = 0; i < parts.size(); i++) {
            result[i] = getPath(partsarr[i]);
        }

        return result;
    }

    @Override
    protected Path createPath(String word) {
        return new Path(GroupingDim.AutoKeyWord, word);
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
