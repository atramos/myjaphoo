package org.myjaphoo.model.groupbyparser.expr;

/**
 * Aggregation
 * @author mla
 * @version $Id$
 */
public class Aggregation {
    private AggregatedExpression expr;

    public Aggregation(AggregatedExpression expr) {
        this.expr = expr;
    }

    public AggregatedExpression getExpr() {
        return expr;
    }
}
