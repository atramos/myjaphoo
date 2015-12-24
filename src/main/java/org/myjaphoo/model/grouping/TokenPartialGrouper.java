/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.CombinationResultGenerator;


/**
 * Gruppiert nach der Tokenhierarchie und dann nach directories.
 * @author mla
 */
public class TokenPartialGrouper extends CachingPartialPathBuilder<Token> {

    /** no assignment path. */
    public static final Path[] NO_ASSIGNMENTPATH = new Path[]{new Path(GroupingDim.Token, "-- no tags --")};

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        if (row.getToken() == CombinationResultGenerator.NULL_TOKEN) {
            return NO_ASSIGNMENTPATH;
        }

        Path[] result = new Path[1];

        result[0] = getPath(row.getToken());

        return result;
    }

    @Override
    protected final Path createPath(Token token) {
        PathAttributes[] attributes = PathAttributes.createAttributes(GroupingDim.Token, token.getName());
        return new Path(attributes);
    }

    @Override
    public boolean needsTagRelation() {
        return true;
    }

    @Override
    public boolean needsMetaTagRelation() {
        return false;
    }
}
