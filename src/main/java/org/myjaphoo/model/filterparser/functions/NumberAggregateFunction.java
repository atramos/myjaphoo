package org.myjaphoo.model.filterparser.functions;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.FunctionCall;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.syntaxtree.NullLiteral;
import org.myjaphoo.model.filterparser.values.LongValue;
import org.myjaphoo.model.filterparser.values.Value;

import java.util.List;

/**
 * NumberAggregateFunction
 * @author mla
 * @version $Id$
 */
public abstract class NumberAggregateFunction extends NumberFunction {

    public NumberAggregateFunction(String name, String descr) {
        super(name, descr, ExprType.NUMBER);
    }

    @Override
    public final Value evaluate(FunctionCall call, ExecutionContext context, JoinedDataRow row, List<Expression> args) {
        /**
         * aggregate functions have two phases:
         * during aggregation mode, it accumulates/aggregates the value
         * when the aggregation mode is finished, it just delivers the aggregated value
         */
        Long val = context.getAggregatedValue(call);

        if (context.isAggregationMode()) {

            if (val == null) {
                val = getBaseValue();
            }
            Value additionalVal = args.get(0).evaluate(context, row);
            val = aggregateVal(val, additionalVal.asLong());
            context.setAggregatedValue(call, val);
            return additionalVal;
        } else {
            if (val == null) {
                return new LongValue(0);
            } else {
                return new LongValue(val);
            }
        }
    }

    protected abstract long aggregateVal(long val, long additionalVal);

    protected abstract long getBaseValue();


}
