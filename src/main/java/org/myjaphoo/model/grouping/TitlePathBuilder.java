/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.filterparser.expr.JoinedDataRow;


/**
 * Groupes by Title.
 * @author mla
 */
public class TitlePathBuilder extends CachingPartialPathBuilder<String> {

    private static final Path[] NOTITLE = new Path[]{new Path(GroupingDim.Title, "NOTITLE")};

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        String title = row.getEntry().getTitle();
        if (title == null) {
            return NOTITLE;
        }
        return new Path[]{getPath(title)};
    }

    @Override
    protected Path createPath(String txt) {
        return new Path(GroupingDim.Title, txt);
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
