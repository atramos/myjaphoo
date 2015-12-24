/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;

import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Spezielles Literal, das als "null" bzw. "nothing" literal agiert.
 *
 * @author mla
 */
public class NullLiteral extends StringLiteral implements Literal {

    public NullLiteral() {
        super(null);
    }

    @Override
    public ExprType getType() {
        return ExprType.NULL;
    }

    @Override
    public String getDisplayExprTxt() {
        return "null";
    }

    @Override
    public Date asDate() {
        return null;
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
