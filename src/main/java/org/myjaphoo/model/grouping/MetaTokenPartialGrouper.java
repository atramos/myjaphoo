/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.CombinationResultGenerator;


/**
 * Gruppiert nach der Tokentype.
 * @author mla
 */
public class MetaTokenPartialGrouper extends CachingPartialPathBuilder<MetaToken> {

    /** no assignment path. */
    public static final Path[] NO_ASSIGNMENTPATH; //= new Path[]{new Path(GroupingDim.Metatoken, "-- ohne Metatoken --")};

    static {
        // brauchen ein pseudo pfad, mit gesetzten metatoken, welches "nix" selektiert,
        // ansonsten würde das dependency filtern nicht funktionieren.
        PathAttributes[] attributes = PathAttributes.createAttributes(GroupingDim.Metatoken, "-- ohne Metatoken --");
        //MetaToken pseudoNix = new MetaToken();
        //pseudoNix.setId(Long.MIN_VALUE);
        //attributes[0].setMetaTokens(Arrays.asList(new MetaToken[]{pseudoNix}));
        NO_ASSIGNMENTPATH = new Path[]{new Path(attributes)};
    }

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        if (row.getMetaToken() == CombinationResultGenerator.NULL_META_TOKEN) {
            return NO_ASSIGNMENTPATH;
        }
        Path[] result = new Path[1];

        result[0] = getPath(row.getMetaToken());

        return result;
    }


    @Override
    protected Path createPath(MetaToken metatoken) {
        PathAttributes[] attributes = PathAttributes.createAttributes(GroupingDim.Metatoken, metatoken.getName());
        return new Path(attributes);
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
