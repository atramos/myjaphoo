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
public class LongValue implements Value {

    private long value;

    public LongValue(long value) {
        this.value = value;
    }

    @Override
    public ExprType getType() {
        return ExprType.NUMBER;
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
        return value;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String convertToString() {
        return Long.toString(asLong());
    }
}
