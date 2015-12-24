/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.db.TokenType;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.CombinationResultGenerator;


/**
 * Gruppiert nach der Tokentype.
 * @author mla
 */
public class TokenTypePartialGrouper extends CachingPartialPathBuilder<TokenType> {

    /** no assignment path. */
    public static final Path[] NO_ASSIGNMENTPATH = new Path[]{new Path(GroupingDim.TokenType, "-- ohne Tokentype --")};

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        if (row.getToken() == CombinationResultGenerator.NULL_TOKEN) {
            return NO_ASSIGNMENTPATH;
        }
        Path[] result = new Path[1];

        result[0] = getPath(row.getToken().getTokentype());

        return result;
    }


    @Override
    protected Path createPath(TokenType tokType) {
        PathAttributes[] attributes = PathAttributes.createAttributes(GroupingDim.TokenType, tokType.toString());
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
