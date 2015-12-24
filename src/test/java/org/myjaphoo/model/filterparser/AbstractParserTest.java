/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.movietree.grouping.GroupAlgorithm;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Rating;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.db.TokenType;
import org.myjaphoo.model.filterparser.expr.AbstractBoolExpression;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.FilterAndGroupingProcessor;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.visitors.DefaultExpressionVisitor;
import org.myjaphoo.model.grouping.GroupingExecutionContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author mla
 */
public abstract class AbstractParserTest extends TestCase {

    public static MovieEntry testEntry2 = new MovieEntry();
    public static MovieEntry testEntry = new MovieEntry();
    public static Token token = new Token();
    public static Token parentTok = new Token();

    static {
        testEntry.setName("bla.mpg");
        testEntry.setCanonicalDir("aaa/bbb/ccc");
        testEntry.setFileLength(50000);
        testEntry.setRating(Rating.GOOD);

        testEntry.getAttributes().put("attr1", "val1");
        testEntry.getAttributes().put("attr2", "val2");

        token.setName("mytok");
        token.setTokentype(TokenType.MOVIENAME);
        token.setDescription("hallihallo");
        token.getMovieEntries().add(testEntry);
        testEntry.getTokens().add(token);

        token.getAttributes().put("attr1", "val1");
        token.getAttributes().put("attr2", "val2");

        token.setParent(parentTok);
        parentTok.setName("ptokname");
        parentTok.setDescription("ptokdescrname");

        BasicConfigurator.configure();
    }
    public static final GroupAlgorithm nullGrouper = new GroupAlgorithm() {

        @Override
        public List<MovieStructureNode> findParents(AbstractMovieTreeNode root, JoinedDataRow row) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public AbstractMovieTreeNode getOrCreateRoot() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public AbstractMovieTreeNode findAccordingNode(AbstractMovieTreeNode root, String path) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void pruneEmptyDirs(AbstractMovieTreeNode root) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void postProcess(AbstractMovieTreeNode root) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void preProcess(GroupingExecutionContext context) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getText() {
            return null;
        }

        @Override
        public void pruneByHavingClause(MovieStructureNode root) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void aggregate(MovieStructureNode root) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean needsTagRelation() {
            return false;
        }

        @Override
        public boolean needsMetaTagRelation() {
            return false;
        }
    };

    protected boolean tst(String strexpr) throws ParserException {
        return tst(strexpr, new HashMap<String, Substitution>());
    }

    protected boolean tst(String strexpr, Map<String, Substitution> subs) throws ParserException {
        FilterParser parser = new FilterParser(subs);
        AbstractBoolExpression expr = parser.parse(strexpr);

        // check visitor:
        DefaultExpressionVisitor v = new DefaultExpressionVisitor();
        v.visit(expr);

        return FilterAndGroupingProcessor.filterEntries(Arrays.asList(testEntry), null, expr, nullGrouper).size() > 0;

        //return expr.evaluate(new ExecutionContext(), testEntry).asBool();
    }

    protected Value evalConstToValue(String strexpr) throws ParserException, IOException {
        FilterParser parser = new FilterParser(new HashMap<String, Substitution>());
        Expression expr = parser.parseExpression(strexpr);
        Value value = expr.evaluate(new ExecutionContext(), null);
        return value;
    }

    protected boolean tst2(String strexpr) throws ParserException {
        FilterParser parser = new FilterParser(new HashMap<String, Substitution>());
        AbstractBoolExpression expr = parser.parse(strexpr);

        return FilterAndGroupingProcessor.filterEntries(Arrays.asList(testEntry2), null, expr, nullGrouper).size() > 0;
        //return expr.evaluate(new ExecutionContext(), testEntry2).asBool();
    }

    protected void tstTrue(String strexpr) throws ParserException {
        assertTrue(tst(strexpr));
        // meta tests:
        // a and !a = false
        // a and a = true
        // !(a or !a) = false
        String a1 = "(" + strexpr + ") and not (" + strexpr + ")";
        assertFalse(tst(a1));
        String a2 = "(" + strexpr + ") and (" + strexpr + ")";
        assertTrue(tst(a2));
        String a3 = "not((" + strexpr + ") or not(" + strexpr + "))";
        assertFalse(tst(a3));
    }
}
