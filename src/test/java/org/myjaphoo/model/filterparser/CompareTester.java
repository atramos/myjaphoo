package org.myjaphoo.model.filterparser;

import org.myjaphoo.datasets.TestSet;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.dbcompare.DBDiffCombinationResult;
import org.myjaphoo.model.filterparser.expr.AbstractBoolExpression;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.FilterAndGroupingProcessor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Helper class to test compare test results.
 */
public class CompareTester {

    /**
     * left test set.
     */
    public TestSet leftSet = new TestSet();

    /**
     * right test set.
     */
    public TestSet rightSet = new TestSet();

    private ArrayList<JoinedDataRow> result;

    private int expectedNumberOfResultRows;

    public CompareTester(int expectedNumberOfResultRows) {
        this.expectedNumberOfResultRows = expectedNumberOfResultRows;
    }

    public void buildCombinations(String filterExpr) throws ParserException {
        FilterParser parser = new FilterParser(new HashMap<String, Substitution>());
        AbstractBoolExpression expr = parser.parse("tag <> xyz");
        result = FilterAndGroupingProcessor.filterEntries(leftSet.entries, rightSet.entries, expr, AbstractParserTest.nullGrouper);
        junit.framework.Assert.assertEquals("not correct num of expected results!", expectedNumberOfResultRows, result.size());
    }

    public void assertExists(MovieEntry left, Token leftt, MetaToken leftmt, MovieEntry right, Token rightt, MetaToken rightmt) {
        junit.framework.Assert.assertTrue("combination does not exist!", exists(left, leftt, leftmt, right, rightt, rightmt));
    }

    public boolean exists(MovieEntry left, Token leftt, MetaToken leftmt, MovieEntry right, Token rightt, MetaToken rightmt) {
        for (JoinedDataRow r : result) {
            DBDiffCombinationResult d = (DBDiffCombinationResult) r;
            if (d.getEntry().equals(left) && d.getToken().equals(leftt) && d.getMetaToken().equals(leftmt) &&
                    d.getCDBEntry().equals(right) && d.getCDBToken().equals(rightt) && d.getCDBMetaToken().equals(rightmt)) {
                return true;
            }
        }
        return false;
    }
}
