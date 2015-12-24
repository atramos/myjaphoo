/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.expr;


import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.operator.AbstractOperator;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.Arrays;
import java.util.List;


/**
 * a simple term of the form expression operator expression.
 * @author mla
 */
public class Term implements Expression {

    private Expression b1;
    private AbstractOperator op;
    private Expression b2;

    public Term(Expression b1, AbstractOperator op, Expression b2) {
        this.b1 = b1;
        this.op = op;
        this.b2 = b2;
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        return op.eval(b1.evaluate(context, row), b2.evaluate(context, row));
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(b1, b2);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }

    @Override
    public String getDisplayExprTxt() {
        return b1.getDisplayExprTxt() + " " + op.getSymbol() + " " + b2.getDisplayExprTxt();
    }

    @Override
    public boolean needsTagRelation() {
        return b1.needsTagRelation() || b2.needsTagRelation();
    }

    @Override
    public boolean needsMetaTagRelation() {
        return b1.needsMetaTagRelation() || b2.needsMetaTagRelation();
    }

    @Override
    public ExprType getType() {
        return op.getReturnType(b1.getType(), b2.getType());
    }

    public AbstractOperator getOp() {
        return op;
    }

    public Expression getB1() {
        return b1;
    }

    public Expression getB2() {
        return b2;
    }
}
