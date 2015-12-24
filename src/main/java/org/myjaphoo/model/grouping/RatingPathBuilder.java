/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.util.Helper;


/**
 *
 * @author mla
 */
public class RatingPathBuilder extends CachingPartialPathBuilder<String> {

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        String txt = Helper.getRatingCategoryText(row.getEntry());
        return new Path[]{getPath(txt)};
    }

    @Override
    protected Path createPath(String txt) {
        return new Path(GroupingDim.Rating.name(), txt);
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
