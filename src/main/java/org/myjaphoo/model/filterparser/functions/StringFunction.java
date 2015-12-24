/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.functions;

import org.myjaphoo.model.filterparser.expr.ExprType;

/**
 * Basisklasse für String-funktionen, die einen String zurückliefern.
 * @author mla
 */
public abstract class StringFunction extends Function {

    public StringFunction(String name, String descr, ExprType... argTypes) {
        super(name, descr, argTypes);
    }

    @Override
    final public ExprType getType() {
        return ExprType.TEXT;
    }
}
