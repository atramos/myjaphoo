/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.syntaxtree;

import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.values.Value;

/**
 * Interface für Literale. Faktisch sind das nur Values, die fix in einem
 * geparsten Ausdruck vorkommen.
 * @author mla
 */
public interface Literal extends AbstractParsedPiece, Expression, Value {

    public void setUnit(Unit unit);
}
