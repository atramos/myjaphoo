/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.DuplicateHashMap;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;


/**
 *
 * @author mla
 */
public class DuplicatePartialGrouper extends AbstractPartialPathBuilder {

    private DuplicateHashMap duplicates;
    private static final Path[] NODUPSPATH = new Path[]{new Path(GroupingDim.Duplicates, "-- no duplicates --")};

    @Override
    public void preProcess(GroupingExecutionContext context) {
        duplicates = new DuplicateHashMap(context.getAllEntriesToGroup());
    }

    @Override
    public Path[] getPaths(JoinedDataRow row) {
        // check, if this is a node, where duplicates exists:
        if (!duplicates.hasDuplicates(row.getEntry().getChecksumCRC32())) {
            return NODUPSPATH;
        }
        return new Path[]{new Path(GroupingDim.Duplicates, "dups", "dup " + row.getEntry().getChecksumCRC32())};
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
