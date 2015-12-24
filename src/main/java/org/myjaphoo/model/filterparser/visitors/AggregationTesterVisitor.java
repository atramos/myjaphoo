package org.myjaphoo.model.filterparser.visitors;

import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.FunctionCall;
import org.myjaphoo.model.filterparser.functions.NumberAggregateFunction;
import org.myjaphoo.model.filterparser.syntaxtree.Constant;
import org.myjaphoo.model.filterparser.syntaxtree.Literal;

/**
 * Checks, if an expression is an aggregated expression.
 * This is the case
 * 1. it is enclosed in a aggregation function
 * 2. it is a constant
 * 3. it is a expression assembled by other expressions which are them self aggregated expressions.
 * @author mla
 * @version $Id$
 */
public class AggregationTesterVisitor extends DefaultExpressionVisitor<Boolean> {

    public Boolean doIteration(Expression expression) {
        if (expression instanceof FunctionCall) {
            // aggregate function are of course aggregations, other functions are not aggregated expressions.
            return  ((FunctionCall) expression).getFunction() instanceof NumberAggregateFunction;
        }
        if (expression instanceof Literal) {
            return true;
        }
        if (expression instanceof Constant) {
            return true;
        }
        /**
         * by all assembled expressions, its aggregated, if all parts/childen are aggregated:
         */
        if (expression.getChildren().size() > 0) {
            for (Expression child : expression.getChildren()) {
                if (!child.accept(this)) {
                    return false;
                }
            }
            return true;
        } else {
            // all other atomic expressions are no aggregations.
            return false;
        }
    }
}
