/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.values;

import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.expr.ExprType;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author mla
 */
public class StringValue implements Value {

    private String value;
    /**
     * gecachtes pattern für regex expressions.
     */
    private transient Pattern patternLiteral = null;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public ExprType getType() {
        return ExprType.TEXT;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String asString() {
        return value;
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

    public Pattern getRegExPattern() throws ParserException {
        if (patternLiteral == null) {
            try {
                patternLiteral = Pattern.compile(value, Pattern.CASE_INSENSITIVE);
            } catch (java.util.regex.PatternSyntaxException pse) {
                throw new ParserException(value, "error parsing regular expression!" + pse.getDescription(), 0, 0);
            }
        }
        return patternLiteral;
    }

    @Override
    public String convertToString() {
        return value;
    }
}
