/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;


import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.values.StringValue;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.Collections;
import java.util.List;


/**
 * Bildet ein Stringliteral ab.
 *
 * @author mla
 */
public class StringLiteral extends StringValue implements Literal {

    public StringLiteral(String literal) {
        super(literal);
    }

    /**
     * @return the literal
     */
    public String getLiteral() {
        return asString();
    }

    @Override
    public String toString() {
        return "str:" + asString();
    }

    @Override
    public String getDisplayExprTxt() {
        return "'" + asString() + "'";
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        return this;
    }

    @Override
    public void setUnit(Unit unit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean needsTagRelation() {
        return false;
    }

    @Override
    public boolean needsMetaTagRelation() {
        return false;
    }

    @Override
    public List<Expression> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }
}
