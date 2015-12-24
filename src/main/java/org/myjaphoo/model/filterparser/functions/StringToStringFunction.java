/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.myjaphoo.model.filterparser.functions;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.FunctionCall;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.values.StringValue;
import org.myjaphoo.model.filterparser.values.Value;

import java.util.List;


/**
 * A function that takes one string as argument and has a string as return
 * value.
 *
 * @author mla
 */
public abstract class StringToStringFunction extends StringFunction {

    public StringToStringFunction(String name, String descr) {
        super(name, descr, ExprType.TEXT);
    }

    @Override
    public final Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
        Expression arg0 = args.get(0);
        String s1 = arg0.evaluate(context, row).asString();

        return new StringValue(stringToStringFunction(context, s1));
    }

    /**
     * the string to string function to implement
     */
    abstract protected String stringToStringFunction(ExecutionContext context, String s);
}
