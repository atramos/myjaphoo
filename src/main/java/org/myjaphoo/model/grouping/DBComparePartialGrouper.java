/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.dbcompare.DatabaseComparison;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;


/**
 * Gruppiert nach der Tokenhierarchie und dann nach directories.
 * @author mla
 */
public class DBComparePartialGrouper extends CachingPartialPathBuilder<String> {

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        String cat = DatabaseComparison.getInstance().getCategoryName(row.getEntry());
        return new Path[]{getPath(cat)};
    }

    @Override
    protected Path createPath(String cat) {
        return new Path(GroupingDim.DB_Comparison, cat);
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
