/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.myjaphoo.model.filterparser.functions;

import org.myjaphoo.model.filterparser.expr.ExprType;

/**
 * Base class for boolean functions.
 *
 * @author mla
 */
public abstract class BoolFunction extends Function {

    public BoolFunction(String name, String descr) {
        super(name, descr);
    }

    @Override
    public ExprType getType() {
        return ExprType.BOOLEAN;
    }

}
