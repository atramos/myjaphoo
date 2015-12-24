/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;


import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.values.LongValue;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.Collections;
import java.util.List;


/**
 * @author mla
 */
public class NumberLiteralImpl extends LongValue implements NumberLiteral {

    private Unit unit;

    public NumberLiteralImpl(long literal) {
        super(literal);
    }

    /**
     * @return the literal
     */
    @Override
    public long getLiteral() {
        return asLong();
    }

    @Override
    public Object getValue() {
        return asLong();
    }

    @Override
    public long asLong() {
        return super.asLong() * getUnitFactor();
    }

    @Override
    public String toString() {
        return "num:" + asLong();
    }

    @Override
    public String getDisplayExprTxt() {
        return Long.toString(asLong());
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        return this;
    }

    @Override
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    private long getUnitFactor() {
        if (unit == null) {
            return 1;
        } else {
            return unit.getFactor();
        }
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
