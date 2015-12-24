/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.operator;

import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.values.StringValue;
import org.myjaphoo.model.filterparser.values.Value;

import java.util.Date;

/**
 * default boolean operator implementation. 
 * @author mla
 */
public abstract class DefaultBooleanOperator extends AbstractBoolResultOperator {

    protected DefaultBooleanOperator(int tok, char ch, String descr, ExprType... types) {
        super(tok, ch, descr, types);
    }

    protected DefaultBooleanOperator(int tok, String str, String descr, ExprType... types) {
        super(tok, str, descr, types);
    }

    @Override
    protected final boolean internEval(Value v1, Value v2) {
        if (hasType(v1, v2, ExprType.TEXT)) {
            return eval(v1.asString(), (StringValue) v2);
        } else if (hasType(v1, v2, ExprType.NUMBER)) {
            return eval(v1.asLong(), v2.asLong());
        } else if (hasType(v1, v2, ExprType.DATE)) {
            return eval(v1.asDate(), v2.asDate());
        } else {
            throw new RuntimeException("type mismatch for value " + v1.getValue() + " and " + v2.getValue());
        }
    }

    private boolean hasType(Value v1, Value v2, ExprType type) {
        return v1.getType().isCompatible(type) && v2.getType().isCompatible(type);
    }

    public boolean eval(String txt1, StringValue stringLiteral) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean eval(long i1, long i2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean eval(boolean i1, boolean i2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean eval(Date i1, Date i2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
