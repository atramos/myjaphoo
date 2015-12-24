/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.groupbyparser.expr;

import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.grouping.PartialPathBuilder;

/**
 * Groups by an expression. The expression must have the return type string (or multiple strings).
 *
 * @author lang
 */
public class StringExpressionGrouping extends Grouping {

    private Expression expression;

    public StringExpressionGrouping(Expression expression) {
        this.expression = expression;
    }

    @Override
    public PartialPathBuilder createPartialPathBuilder() {
        return new ByIdentifierPathBuilder(expression);
    }

    @Override
    public String getDisplayExprTxt() {
        return expression.getDisplayExprTxt();
    }
}
