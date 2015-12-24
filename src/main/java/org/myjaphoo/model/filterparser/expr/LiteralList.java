/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.expr;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.syntaxtree.Literal;
import org.myjaphoo.model.filterparser.syntaxtree.LiteralListSymbol;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.values.ValueSet;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 *
 * @author mla
 */
public class LiteralList extends ValueSet implements Expression {

    public LiteralList(LiteralListSymbol literalListOperator, List<Literal> literalList) {
        super(literalList);
        if (literalListOperator == LiteralListSymbol.AND_SEPARATOR) {
            setCombining(ValueCombining.AND);
        }
    }

    @Override
    public String getDisplayExprTxt() {
        if (getValues().size() == 1) {
            return getValues().iterator().next().convertToString();
        } else {
            StringBuilder b = new StringBuilder();
            for (Value value : getValues()) {
                if (b.length() > 0) {
                    b.append(getCombining());
                }
                b.append(value.convertToString());
            }
            return b.toString();
        }
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        return this;
    }
    
    /** cached values of this literallist. */
    private Collection<? extends Value> cachedValues = null;
    
    /**
     * a literal list has one advantage, over a regular valueset:
     * it is constant over the time (since its build by literals).
     * so this method caches the values build from the underlying valueset.
     */
    @Override
    public Collection<? extends Value> getValues() {
        if (cachedValues == null) {
            cachedValues = super.getValues();
        }
        return cachedValues;
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
