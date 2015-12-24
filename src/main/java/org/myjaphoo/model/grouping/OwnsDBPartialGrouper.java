/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.filterparser.expr.JoinedDataRow;


/**
 * Gruppiert nach zugehörigkeit zur Datenbank.
 * MovieEntries gehören dazu. Files gehören nicht dazu.
 * @author mla
 */
@Deprecated
public class OwnsDBPartialGrouper extends AbstractPartialPathBuilder {

    /** no assignment path. */
    public static final Path[] INDB = new Path[]{new Path(GroupingDim.File_Or_Entry_Divider, "-- in DB --")};
    public static final Path[] NOT_INDB = new Path[]{new Path(GroupingDim.File_Or_Entry_Divider, "-- nicht in DB --")};

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        return INDB;
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
