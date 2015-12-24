/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.operator;

import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.values.Value;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Wraps another operator to use an existing operator as a synonym operator (i.e.
 * operator with the same function but another name).
 * @author mla
 */
public class SynonymOp extends AbstractOperator {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/model/filterparser/operator/resources/SynonymOp");
    private AbstractOperator synonymOperator;

    public SynonymOp(int tok, String symbolname, AbstractOperator synonymOperator) {
        super(tok, symbolname, MessageFormat.format(localeBundle.getString("SYNONYM FOR"), synonymOperator.getSymbol()));
        this.synonymOperator = synonymOperator;
    }

    @Override
    public Value eval(Value v1, Value v2) {
        return synonymOperator.eval(v1, v2);
    }

    @Override
    public Set<ExprType> worksWithTypes() {
        return synonymOperator.worksWithTypes();
    }

    @Override
    public ExprType getReturnType(ExprType rightExpressionType, ExprType leftExpressionType) {
        return synonymOperator.getReturnType(rightExpressionType, leftExpressionType);
    }
}
