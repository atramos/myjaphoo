/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.CombinationResultGenerator;

import java.util.ArrayList;


/**
 * Gruppiert nach der Tokenhierarchie und dann nach directories.
 * @author mla
 */
public class TokenHierarchyPartialGrouper extends AbstractPartialPathBuilder {

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        if (row.getToken() == CombinationResultGenerator.NULL_TOKEN) {
            return TokenPartialGrouper.NO_ASSIGNMENTPATH;
        }

        Path[] result = new Path[1];

        String[] tokHierarchy = buildHierarchy(row.getToken());
        PathAttributes[] attributes = PathAttributes.createAttributes(GroupingDim.TokenHierarchy, tokHierarchy);
        result[0] = new Path(attributes);

        return result;
    }

    private String[] buildHierarchy(Token token) {
        ArrayList<String> hierarchy = new ArrayList<String>();
        hierarchy.add(token.getName());
        // solange bis zur wurzel wiederholen, die wurzel selbst aber weglassen:
        while (token.getParent() != null && token.getParent().getParent() != null) {
            token = token.getParent();
            hierarchy.add(0, token.getName());
        }
        return hierarchy.toArray(new String[hierarchy.size()]);
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
