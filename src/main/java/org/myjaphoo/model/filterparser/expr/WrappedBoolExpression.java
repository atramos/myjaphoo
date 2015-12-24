/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.expr;


import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.Arrays;
import java.util.List;


/**
 * A boolean expression that consists of a "regular" expression which has a
 * result type of boolean.
 * This is used, to make regular expression e.g. terms & functions, which are
 * themself not within the type hierarchy of AbstractBoolExpression to a Object
 * within the type hierarchy of AbstractBoolExpression.
 * This is necessary as type checks and type safety with respect to AbstractBoolExpression 
 * is used within the expression and parser logic.
 * @author mla
 */
public class WrappedBoolExpression extends AbstractBoolExpression {

    /** the wrapped expression which has the invariant that it results to boolean. */
    private Expression expression;

    public WrappedBoolExpression(Expression expression) throws ParserException {
        this.expression = expression;
        // check the invariant. but this should never happen
        if (expression.getType() != ExprType.BOOLEAN && expression.getType() != ExprType.OBJECT) {
            throw new ParserException("", "internal parser error! wrapped non-boolean expression to boolean expression!", 0, 0);
        }
    }

    @Override
    public String getDisplayExprTxt() {
        return expression.getDisplayExprTxt();
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        return expression.evaluate(context, row);
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(expression);
    }

    @Override
    public boolean needsTagRelation() {
        return expression.needsTagRelation();
    }

    @Override
    public boolean needsMetaTagRelation() {
        return expression.needsMetaTagRelation();
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }
}
