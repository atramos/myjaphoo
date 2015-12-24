/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.values;

import java.util.Date;
import org.myjaphoo.model.filterparser.expr.ExprType;

/**
 *
 * @author mla
 */
public class BoolValue implements Value {

    public static final BoolValue TRUE = new BoolValue(true);
    public static final BoolValue FALSE = new BoolValue(false);
    private boolean value;

    public static BoolValue forVal(boolean value) {
        if (value) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    private BoolValue(boolean value) {
        this.value = value;
    }

    @Override
    public ExprType getType() {
        return ExprType.BOOLEAN;
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
        throw new UnsupportedOperationException("Not supported yet.");
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
        return value;
    }

    @Override
    public String convertToString() {
        return value? "TRUE" : "FALSE";
    }
}
