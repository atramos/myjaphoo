/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.values.DateValue;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * ein Datums literal.
 *
 * @author mla
 */
public class DateLiteral extends DateValue implements Literal {

    public DateLiteral(Date literal) {
        super(literal);
    }

    /**
     * @return the literal
     */
    public Date getLiteral() {
        return asDate();
    }

    @Override
    public String toString() {
        return "date:" + asDate();
    }

    @Override
    public String getDisplayExprTxt() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return "'" + df.format(asDate()) + "'";
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
