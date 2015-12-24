/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.functions;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.FunctionCall;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.values.Value;

import java.util.List;
import java.util.ResourceBundle;


/**
 * "Pseudo" funktion substition:
 * Substituiert eine Bookmark.
 * Ist nur eine "Marker" Funktion. Sie wird an sich nicht verwendet,
 * die Substitution findet direkt im FilterParser statt.
 *
 * @author mla
 */
public class SubstitutionFunction extends Function {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/filterparser/functions/resources/SubstitutionFunction");

    public SubstitutionFunction() {
        super("subst", localeBundle.getString("SUBST_DESCR"), ExprType.TEXT); //NOI18N
    }

    @Override
    public Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
        throw new RuntimeException("not implemented; only marker class"); //NOI18N
    }

    @Override
    public ExprType getType() {
        return ExprType.BOOLEAN;
    }

}
