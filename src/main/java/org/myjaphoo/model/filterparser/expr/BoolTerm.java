/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.expr;


import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.operator.AbstractOperator;
import org.myjaphoo.model.filterparser.operator.Operators;
import org.myjaphoo.model.filterparser.values.BoolValue;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.Arrays;
import java.util.List;


/**
 *
 * @author mla
 */
public class BoolTerm extends AbstractBoolExpression {

    private AbstractBoolExpression b1;
    private AbstractOperator boolop;
    private AbstractBoolExpression b2;

    public BoolTerm(AbstractBoolExpression b1, AbstractOperator boolop, AbstractBoolExpression b2) {
        this.b1 = b1;
        this.boolop = boolop;
        this.b2 = b2;
        if (boolop != Operators.AND && boolop != Operators.OR) {
            throw new RuntimeException("interner fehler: nicht unterstützter operator!");
        }
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        if (Operators.AND == boolop) {
            return BoolValue.forVal(b1.evaluate(context, row).asBool() && b2.evaluate(context, row).asBool());
        } else if (Operators.OR == boolop) {
            return BoolValue.forVal(b1.evaluate(context, row).asBool() || b2.evaluate(context, row).asBool());
        }
        throw new RuntimeException("interner fehler. kein and und kein or...");
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList((Expression)b1, b2);
    }

    @Override
    public String getDisplayExprTxt() {
        return b1.getDisplayExprTxt() + " " + boolop.getSymbol() + " " + b2.getDisplayExprTxt();
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
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }
}
