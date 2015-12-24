package org.myjaphoo.model.groupbyparser.expr;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.expr.*;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.AggregationFunctionCallFinder;
import org.myjaphoo.model.filterparser.visitors.AggregationTesterVisitor;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;

import java.util.Arrays;
import java.util.List;

/**
 * An Aggregated expression, e.g. a having clause, or an aggregation for a grouping.
 * @author mla
 * @version $Id$
 */
public class AggregatedExpression implements Expression {

    /**
     * these are the aggregation parts of the whole having expression. Means this are all calls
     * to aggregation functions within the complete having clause. We need this to "populate" the aggregations.
     */
    private final List<FunctionCall> aggregations;

    private Expression aggregatedExpression;

    public AggregatedExpression(Expression aggregatedExpression) {
        this.aggregatedExpression = aggregatedExpression;
        aggregations = AggregationFunctionCallFinder.findAllFunctionCallsToAggregateFunctions(this);
    }

    @Override
    public ExprType getType() {
        return aggregatedExpression.getType();
    }

    @Override
    public String getDisplayExprTxt() {
        return aggregatedExpression.getDisplayExprTxt();
    }

    @Override
    public Value evaluate(ExecutionContext context, JoinedDataRow row) {
        return aggregatedExpression.evaluate(context, row);
    }

    @Override
    public List<Expression> getChildren() {
        return Arrays.asList(aggregatedExpression);
    }

    @Override
    public boolean needsTagRelation() {
        return aggregatedExpression.needsTagRelation();
    }

    @Override
    public boolean needsMetaTagRelation() {
        return aggregatedExpression.needsMetaTagRelation();
    }

    /**
     * Check the having clause. It must be an expression of aggregates or constants.
     */
    public void semanticCheckAggregationExpression() {
        AggregationTesterVisitor visitor = new AggregationTesterVisitor();
        Boolean aggregated = visitor.visit(this);
        if (!aggregated) {
            throw new ParserException(getDisplayExprTxt(), "This is no aggregated expression!", 0, 0);
        }

        // we do not support nested aggregations:
        for (FunctionCall aggregation : aggregations) {
            for (Expression arg : aggregation.getArgs()) {
                aggregated = visitor.visit(arg);
                if (aggregated) {
                    throw new ParserException(getDisplayExprTxt(), "No nested aggregations are allowed!", 0, 0);
                }
            }
        }
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
        return expressionVisitor.visit(this);
    }

    /**
     * Gets called for each row to populate the aggregated values of all the aggregation functions within an expression.
     * @param context
     * @param row
     */
    public void populateAggregations(ExecutionContext context, JoinedDataRow row) {
        for (FunctionCall aggregation : aggregations) {
            aggregation.evaluate(context, row);
        }
    }
}
