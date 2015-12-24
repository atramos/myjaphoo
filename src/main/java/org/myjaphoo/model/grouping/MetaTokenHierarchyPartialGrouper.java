/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.CombinationResultGenerator;

import java.util.ArrayList;


/**
 * Gruppiert nach der MetaTokenhierarchie.
 * @author mla
 */
public class MetaTokenHierarchyPartialGrouper extends AbstractPartialPathBuilder {

    @Override
    public final Path[] getPaths(JoinedDataRow row) {

        if (row.getMetaToken() == CombinationResultGenerator.NULL_META_TOKEN) {
            return MetaTokenPartialGrouper.NO_ASSIGNMENTPATH;
        }

        Path[] result = new Path[1];

        String[] tokHierarchy = buildHierarchy(row.getMetaToken());
        PathAttributes[] attributes = PathAttributes.createAttributes(GroupingDim.MetatokenHierarchy, tokHierarchy);
        result[0] = new Path(attributes);

        return result;
    }

    private String[] buildHierarchy(MetaToken token) {
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
        return false;
    }

    @Override
    public boolean needsMetaTagRelation() {
        return true;
    }
}
