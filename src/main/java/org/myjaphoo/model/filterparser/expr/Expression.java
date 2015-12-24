/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.expr;


import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.processing.ProcessingRequirementInformation;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.List;


/**
 * @author mla
 */
public interface Expression extends ProcessingRequirementInformation {

    ExprType getType();

    public String getDisplayExprTxt();

    /**
     * evaluate the expression for the given movie entry, tag and metatag combination.
     */
    public Value evaluate(ExecutionContext context, JoinedDataRow row);

    /**
     * Returns a list of children of this expression, if this expression is composed of other
     * expressions.
     * If this is an atomic expression, this method returns the empty list.
     *
     * @return all child elements or an empty list
     */
    public List<Expression> getChildren();


    public <T> T accept(ExpressionVisitor<T> expressionVisitor);

}
