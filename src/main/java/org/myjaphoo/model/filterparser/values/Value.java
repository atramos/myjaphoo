/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.values;

import java.util.Date;
import org.myjaphoo.model.filterparser.expr.ExprType;

/**
 * Stellt einen Wert in einem Ausdruck dar.
 * Der Wert hat einen bestimmten typ. Es gibt bestimmte
 * Servicemethoden, um den Wert schon mit dem entsprechenden typ zu bekommen.
 * 
 * 
 * @author mla
 */
public interface Value {

    ExprType getType();

    public Object getValue();

    /**
     * liefert den Werte als String.
     */
    public String asString();

    /**
     * liefert den Werte als long.
     */
    public long asLong();

    /**
     * liefert den Werte als double.
     */
    public Double asDouble();

    /**
     * liefert den Werte als date.
     */
    public abstract Date asDate();

    /**
     * liefert den Werte als boolean.
     */
    public abstract boolean asBool();
    
    /**
     * convertiert den Wert in einen String. 
     */
    public abstract String convertToString();
}
