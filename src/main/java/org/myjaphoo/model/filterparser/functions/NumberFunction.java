/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.functions;

import org.myjaphoo.model.filterparser.expr.ExprType;

/**
 * Base class for functions which have as return type numbers.
 * @author lang
 */
public abstract class NumberFunction extends Function {

    public NumberFunction(String name, String descr, ExprType... argTypes) {
        super(name, descr, argTypes);
    }

    @Override
    final public ExprType getType() {
        return ExprType.NUMBER;
    }
}
