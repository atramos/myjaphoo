/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.*;


/**
 * Speziell definierte Konstanten.
 * @author mla
 */
public class Constant extends SpecialCharSymbol implements NumberLiteral, SelfDescriptingElement {

    private static HashMap<String, Constant> map = new HashMap<String, Constant>();
    private ExprType type;
    private long value;

    public Constant(String name, long value, ExprType type) {
        super(name);
        this.type = type;
        this.value = value;
        map.put(name, this);
    }

    public static Collection<Constant> getAllConstants() {
        return map.values();
    }

    @Override
    public ExprType getType() {
        return type;
    }

    @Override
    public String getDisplayExprTxt() {
        return getSymbol();
    }

    @Override
    public long getLiteral() {
        return value;
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        return this;
    }

    @Override
    public List<Expression> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void setUnit(Unit unit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSelfDescription() {
        return getSymbol() + " := " + getSelfShortDescription();
    }

    @Override
    public String getSelfShortDescription() {
        return convertToString();
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String asString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long asLong() {
        return value;
    }

    @Override
    public Double asDouble() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date asDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean asBool() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String convertToString() {
        return Long.toString(value);
    }

    @Override
    public String getExampleUsage() {
        return "";
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
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }
}
