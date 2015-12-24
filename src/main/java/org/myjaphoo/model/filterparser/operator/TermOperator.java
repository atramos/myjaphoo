/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.operator;

import org.myjaphoo.model.filterparser.expr.ExprType;

/**
 * a operator that could be used within a term, e.g.
 * "a + b".
 * This is a marker class for the parser to match such operators when
 * parsing terms.
 *
 * @author mla
 */
public abstract class TermOperator extends AbstractOperator {

    protected TermOperator(int tok, char ch, String description, ExprType... types) {
        super(tok, ch, description, types);

    }

    protected TermOperator(int tok, String str, String description, ExprType... types) {
        super(tok, str, description, types);
    }
}
