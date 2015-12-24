/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.values;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.myjaphoo.model.filterparser.expr.ExprType;

/**
 *
 * @author mla
 */
public class DateValue implements Value {

    private Date value;
    /** date formatter. saved thread local, since multiple threads use this class. */
    private ThreadLocal<DateFormat> dateFormatter = new ThreadLocal<DateFormat>() {

        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy/MM/dd");
        }
    };

    public DateValue(Date value) {
        this.value = value;
    }

    @Override
    public ExprType getType() {
        return ExprType.DATE;
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
        return value;
    }

    @Override
    public boolean asBool() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String convertToString() {
        return formatValue(value);
    }

    private String formatValue(Date date) {
        String stringval = null;
        if (date != null) {
            stringval = dateFormatter.get().format(date);
        }
        return stringval;
    }
}
