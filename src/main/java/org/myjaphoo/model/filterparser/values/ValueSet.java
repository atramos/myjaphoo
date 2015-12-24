/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.values;

import org.myjaphoo.model.filterparser.expr.ExprType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Special value type, that contains multiple values.
 * E.g. all assigned tags-names.
 * @author mla
 */
public class ValueSet implements Value {

    private Collection<? extends Value> values;

    public static enum ValueCombining {

        OR,
        AND
    }
    private ValueCombining combining = ValueCombining.OR;

    public ValueSet(Collection<? extends Value> values) {
        this.values = values;
    }

    public static ValueSet createStringSet(String ... strings) {
        ArrayList<Value> values = new ArrayList<Value>();
        for (String str : strings) {
            values.add(new StringValue(str));
        }
        return new ValueSet(values);
    }

    @Override
    public ExprType getType() {
        if (values.size() > 0) {
            return values.iterator().next().getType();
        } else {
            return ExprType.TEXT; // TODO not sure... by may be necessary for null- comparison
        }
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return all of the values.
     */
    public Collection<? extends Value> getValues() {
        return values;
    }

    @Override
    public String convertToString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the combining
     */
    public ValueCombining getCombining() {
        return combining;
    }

    /**
     * @param combining the combining to set
     */
    public void setCombining(ValueCombining combining) {
        this.combining = combining;
    }
}
