package org.myjaphoo.model.filterparser.values;

import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.expr.ExprType;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * an object value. This is the wrapper for a return value from a groovy method within a filter language expression.
 *
 * @author lang
 * @version $Id$
 */
public class ObjectValue implements Value {

    private Object o;

    public ObjectValue(Object o) {
        this.o = o;
    }

    @Override
    public ExprType getType() {
        return ExprType.OBJECT;
    }

    @Override
    public Object getValue() {
        return o;
    }

    @Override
    public String asString() {
        return o == null ? "" : o.toString();
    }

    @Override
    public long asLong() {
        if (o instanceof Number) {
            return ((Number) o).longValue();
        }
        return Long.parseLong(asString());
    }

    @Override
    public Double asDouble() {
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        return Double.parseDouble(asString());
    }

    @Override
    public Date asDate() {
        if (o instanceof Date) {
            return (Date) o;
        } else {
            try {
                return DateFormat.getDateInstance().parse(asString());
            } catch (ParseException e) {
                throw new ParserException(null, "error converting " + o + " into a date!", 0, 0);
            }
        }
    }

    @Override
    public boolean asBool() {
        if (o instanceof Boolean) {
            return (Boolean) o;
        }
        throw new ParserException(null, "error converting " + o + " into a boolean!", 0, 0);
    }

    @Override
    public String convertToString() {
        return asString();
    }
}
