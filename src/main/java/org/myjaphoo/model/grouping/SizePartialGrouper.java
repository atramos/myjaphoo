/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;


/**
 * Gruppiert nach der Size und dann nach directories.
 * @author mla
 */
public class SizePartialGrouper extends CachingPartialPathBuilder<String> {

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        String size = decideSizeCategory(row.getEntry());
        return new Path[]{getPath(size)};
    }

    private String decideSizeCategory(MovieEntry movieEntry) {
        SizeCategory cat = SizeCategory.searchNearesCatBySize(movieEntry.getFileLength());
        return cat.getName();
    }

    @Override
    protected Path createPath(String size) {
        return new Path(GroupingDim.Size, size);
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
