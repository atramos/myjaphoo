/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.expr;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.functions.Function;
import org.myjaphoo.model.filterparser.functions.NumberAggregateFunction;
import org.myjaphoo.model.filterparser.syntaxtree.AbstractParsedPiece;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.Collections;
import java.util.List;


/**
 * Funktionsaufruf: die funktions-definition und ihre argumente.
 *
 * @author mla
 */
public class FunctionCall implements Expression, AbstractParsedPiece {

    private Function function;
    private List<Expression> args;

    public FunctionCall(Function function, List<Expression> args) {
        this.function = function;
        this.args = args;
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        return function.evaluate(this, context, row, args);
    }

    @Override
    public List<Expression> getChildren() {
        return Collections.unmodifiableList(args);
    }

    @Override
    public ExprType getType() {
        return function.getType();
    }

    @Override
    public String getDisplayExprTxt() {
        return function.getName();
    }

    @Override
    public boolean needsTagRelation() {
        if (function.needsTagRelation()) {
            return true;
        }
        return Expressions.needsTagRelation(args);
    }

    @Override
    public boolean needsMetaTagRelation() {
        if (function.needsMetaTagRelation()) {
            return true;
        }
        return Expressions.needsMetaTagRelation(args);
    }

    public Function getFunction() {
        return function;
    }

    public List<Expression> getArgs() {
        return args;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }
}
