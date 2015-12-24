package org.myjaphoo.model.filterparser.visitors;

import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.FunctionCall;
import org.myjaphoo.model.filterparser.functions.NumberAggregateFunction;

import java.util.ArrayList;

/**
 * Finds all function calls of aggregation functions within a expression.
 * @author mla
 * @version $Id$
 */
public class AggregationFunctionCallFinder extends DefaultExpressionVisitor {

    private ArrayList<FunctionCall> calls = new ArrayList<>();

    @Override
    public Object visit(FunctionCall functionCall) {
        if (functionCall.getFunction() instanceof NumberAggregateFunction) {
            calls.add(functionCall);
        }
        return null;
    }


    public static ArrayList<FunctionCall> findAllFunctionCallsToAggregateFunctions(Expression e) {
        AggregationFunctionCallFinder finder = new AggregationFunctionCallFinder();
        finder.visit(e);
        return finder.calls;
    }
}
