/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.groupbyparser.expr;

import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.grouping.CachingPartialPathBuilder;
import org.myjaphoo.model.grouping.Path;


/**
 *
 * @author mla
 */
public class IfThenElsePartialPathBuilder extends CachingPartialPathBuilder<IfElseGrouping.ConsequenceReturnValue> {

    private IfElseGrouping g;

    public IfThenElsePartialPathBuilder(IfElseGrouping g) {
        this.g = g;
    }

    @Override
    protected Path createPath(IfElseGrouping.ConsequenceReturnValue value) {
        return new Path(value.getDisplayExprTxt(), value.getConsequenceLiteral());
    }

    @Override
    public Path[] getPaths(JoinedDataRow row) {
        IfElseGrouping.ConsequenceReturnValue literal = g.determineConsequenceLiteral(getContext().getFilterContext(), row);
        return new Path[]{getPath(literal)};
    }

    @Override
    public boolean needsTagRelation() {
        return g.needsTagRelation();
    }

    @Override
    public boolean needsMetaTagRelation() {
        return g.needsMetaTagRelation();
    }
}
