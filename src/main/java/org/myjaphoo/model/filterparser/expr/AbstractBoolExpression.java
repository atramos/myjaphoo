/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.expr;

import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

/**
 * Base class for expressions which have a result type of boolean.
 * This class is used to make the presence of expressions with boolean result type
 * typesave in the parser and expression logic.
 * @author mla
 */
public abstract class AbstractBoolExpression implements Expression {
    
    @Override
    public final ExprType getType() {
        return ExprType.BOOLEAN;
    }

}
