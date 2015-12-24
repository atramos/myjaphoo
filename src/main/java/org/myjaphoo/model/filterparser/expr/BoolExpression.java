/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.expr;


import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.operator.AbstractOperator;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.Arrays;
import java.util.List;


/**
 * a boolean expression that consists of a  value op value.
 * @author mla
 */
public class BoolExpression extends AbstractBoolExpression {

    private Expression expr1;
    private AbstractOperator comparison;
    private Expression expr2;

    public BoolExpression(Expression expr1, AbstractOperator comparison, Expression expr2) throws ParserException {
        this.expr1 = expr1;
        this.comparison = comparison;
        this.expr2 = expr2;
        assert comparison.getReturnType(expr1.getType(), expr2.getType()) == ExprType.BOOLEAN;
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        return comparison.eval(expr1.evaluate(context, row), expr2.evaluate(context, row));
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(expr1, expr2);
    }

    @Override
    public String getDisplayExprTxt() {
        return expr1.getDisplayExprTxt() + " " + comparison.getSymbol() + " " + expr2.getDisplayExprTxt();
    }

    @Override
    public boolean needsTagRelation() {
        return expr1.needsTagRelation() || expr2.needsTagRelation();
    }

    @Override
    public boolean needsMetaTagRelation() {
        return expr1.needsMetaTagRelation() || expr2.needsMetaTagRelation();
    }

    public Expression getExpr1() {
        return expr1;
    }

    public AbstractOperator getComparison() {
        return comparison;
    }

    public Expression getExpr2() {
        return expr2;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }
}
