/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

import junit.framework.TestCase;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Rating;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.filterparser.expr.AbstractBoolExpression;
import org.myjaphoo.model.filterparser.processing.FilterAndGroupingProcessor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mla
 */
public class ParserLastTest extends TestCase {

    private static ArrayList<MovieEntry> entryList = new ArrayList<MovieEntry>();
    private static int count = 300000;

    static {

        MovieEntry testEntry = new MovieEntry();
        testEntry.setName("bla");
        testEntry.setCanonicalDir("rqwrrzrzrqwezrezrzwezrzwzrwzerzzwerzwzrzwrzzr/rewrqwerwer/EWRWERRur/r243432423434/fnfnfnfnfn/aaa/bbb/ccc");
        testEntry.setFileLength(50000);
        testEntry.setRating(Rating.GOOD);

        Token token = new Token();
        token.setName("mytok");
        token.getMovieEntries().add(testEntry);
        testEntry.getTokens().add(token);


        Token t = new Token();
        t.setName("jonny");
        t.getMovieEntries().add(testEntry);
        testEntry.getTokens().add(t);

        t = new Token();
        t.setName("brian");
        t.getMovieEntries().add(testEntry);
        testEntry.getTokens().add(t);


        t = new Token();
        t.setName("jones");
        t.getMovieEntries().add(testEntry);
        testEntry.getTokens().add(t);

        // build list of n times the same entry:
        for (int i = 0; i < count; i++) {
            entryList.add(testEntry);
        }

    }

    public void testSimpleLikeExpr() throws ParserException {
        FilterParser parser = new FilterParser(new HashMap<String, Substitution>());
        AbstractBoolExpression expr = parser.parse("path like 'bb'");

        FilterAndGroupingProcessor.filterEntries(entryList, null, expr, AbstractParserTest.nullGrouper);

    }

    public void testSimpleOrExpression() throws ParserException {
        FilterParser parser = new FilterParser(new HashMap<String, Substitution>());
        AbstractBoolExpression expr = parser.parse("size > 60000 or path like 'bla'");

        FilterAndGroupingProcessor.filterEntries(entryList, null, expr, AbstractParserTest.nullGrouper);

    }

    public void testSimpleTokenLikeExpr() throws ParserException {
        FilterParser parser = new FilterParser(new HashMap<String, Substitution>());
        AbstractBoolExpression expr = parser.parse("tok like yto");

        FilterAndGroupingProcessor.filterEntries(entryList, null, expr, AbstractParserTest.nullGrouper);

    }

    public void testliterallistExpr() throws ParserException {
        FilterParser parser = new FilterParser(new HashMap<String, Substitution>());
        AbstractBoolExpression expr = parser.parse("tok like tttjrjrj|wqerqwerqlr|werqwerqwe|nnadfadf|afadsf|afzzf|asdfz|dsfasdf|jonny");

        FilterAndGroupingProcessor.filterEntries(entryList, null, expr, AbstractParserTest.nullGrouper);

    }
}
