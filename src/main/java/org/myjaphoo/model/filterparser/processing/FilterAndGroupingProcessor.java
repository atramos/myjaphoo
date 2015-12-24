/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.processing;

import org.apache.commons.lang.time.StopWatch;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.dbcompare.CombinationSet;
import org.myjaphoo.model.dbcompare.ComparisonSetGenerator;
import org.myjaphoo.model.dbcompare.DBDiffCombinationResult;
import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.AbstractBoolExpression;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.values.BoolValue;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.ExpressionVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Handles the filtering and grouping by expressions.
 * 
 * @author lang
 */
public class FilterAndGroupingProcessor {

    public static final Logger LOGGER = LoggerFactory.getLogger(FilterAndGroupingProcessor.class);

    private static final AbstractBoolExpression TRUE_EXPRESSION = new AbstractBoolExpression() {

        @Override
        public String getDisplayExprTxt() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Value evaluate(ExecutionContext context, JoinedDataRow row) {
            return BoolValue.TRUE;
        }

        @Override
        public List<Expression> getChildren() {
            return Collections.EMPTY_LIST;
        }

        @Override
        public boolean needsTagRelation() {
            return false;
        }

        @Override
        public boolean needsMetaTagRelation() {
            return false;
        }

        @Override
        public <T> T accept(ExpressionVisitor<T> expressionVisitor) {
            return expressionVisitor.visit(this);
        }
    };


    /**
     * Filters the entries. It therefore creates a cross product of entries * token * metatoken if necessary
     * and then checks via the expression evaluate function, if that particular combination
     * gets filtered.
     */
    public static ArrayList<JoinedDataRow> filterEntries(Collection<MovieEntry> allMovies, Collection<MovieEntry> comparisonMovies,
            AbstractBoolExpression expr, ProcessingRequirementInformation pri) {
        StopWatch watch = new StopWatch();
        watch.start();
        ArrayList<JoinedDataRow> result = new ArrayList<JoinedDataRow>(allMovies.size() * 10);
        if (expr == null) {
            expr = TRUE_EXPRESSION;
        }

        ExecutionContext context = new ExecutionContext();
        CombinationSet combSet = CombinationGeneratorFactory.createCombinations(allMovies, expr, pri);

        if (comparisonMovies != null) {
            // prepare comparison combinations of the two databases:
            CombinationSet combOtherDBSet = CombinationGeneratorFactory.createCombinations(comparisonMovies, expr, pri);
            ComparisonSetGenerator generator = new ComparisonSetGenerator();
            Iterable<DBDiffCombinationResult> iterator = generator.createComparisonSetIterator(combSet, combOtherDBSet);

            for (DBDiffCombinationResult comb: iterator) {
                if (expr.evaluate(context, comb).asBool()) {
                    result.add(comb);
                }
            }
        } else {
            for (JoinedDataRow comb: combSet.getResult()) {
                if (expr.evaluate(context, comb).asBool()) {
                    result.add(comb);
                }
            }
        }

        watch.stop();
        LOGGER.info("finished filter processing result: duration:" + watch.toString()); //NOI18N
        return result;
    }
}
