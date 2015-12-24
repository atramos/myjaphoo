/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.expr;


import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.values.BoolValue;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.List;


/**
 *
 * @author mla
 */
public class NegatedBoolExpression extends AbstractBoolExpression {

    private AbstractBoolExpression boolExpression;

    public NegatedBoolExpression(AbstractBoolExpression boolExpression) {
        this.boolExpression = boolExpression;
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        Value origValue = boolExpression.evaluate(context, row);
        return BoolValue.forVal(!origValue.asBool());
    }

    @Override
    public List<Expression> getChildren() {
        return boolExpression.getChildren();
    }

    @Override
    public String getDisplayExprTxt() {
        return " not(" + boolExpression.getDisplayExprTxt() + ")";
    }

    @Override
    public boolean needsTagRelation() {
        return boolExpression.needsTagRelation();
    }

    @Override
    public boolean needsMetaTagRelation() {
        return boolExpression.needsMetaTagRelation();
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }
}
