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
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.FilterAndGroupingProcessor;

import java.util.ArrayList;
import java.util.HashMap;

import static org.myjaphoo.model.filterparser.processing.CombinationResultGenerator.NULL_META_TOKEN;
import static org.myjaphoo.model.filterparser.processing.CombinationResultGenerator.NULL_TOKEN;

/**
 * First very simple compare db tests.
 * @author lang
 */
public class CompareDBTest extends TestCase {

    private static ArrayList<MovieEntry> entryList = new ArrayList<MovieEntry>();

    static {

        MovieEntry testEntry = new MovieEntry();
        testEntry.setName("bla");
        testEntry.setCanonicalDir("/aaa/bbb/ccc");
        testEntry.setFileLength(50000);
        testEntry.setRating(Rating.GOOD);
        testEntry.setChecksumCRC32(5L);
        entryList.add(testEntry);

        Token t = new Token();
        t.setName("jonny");
        t.setId(1L);
        t.getMovieEntries().add(testEntry);
        testEntry.getTokens().add(t);

        t = new Token();
        t.setName("brian");
        t.setId(2L);
        t.getMovieEntries().add(testEntry);
        testEntry.getTokens().add(t);


        t = new Token();
        t.setName("jones");
        t.setId(3L);
        t.getMovieEntries().add(testEntry);
        testEntry.getTokens().add(t);

    }



    public void testSimpleComparisonWithItself() throws ParserException {
        FilterParser parser = new FilterParser(new HashMap<String, Substitution>());
        // we want all entries:
        AbstractBoolExpression expr = parser.parse("tag like jonny|brian|jones");

        // simulate filtering with a comparison with itself:
        ArrayList<JoinedDataRow> result = FilterAndGroupingProcessor.filterEntries(entryList, entryList, expr, AbstractParserTest.nullGrouper);
        assertEquals(3, result.size());
    }


    /**
     * compare: left one entry; right same entry but with attached tag.
     */
    public void testSimpleComparison01()throws ParserException  {
        CompareTester t = new CompareTester(1);

        MovieEntry left = t.leftSet.createEntry("bla", "dir", 5000, 1000, Rating.GOOD);

        MovieEntry right = t.rightSet.createEntry("bla", "dir", 5000, 1000, Rating.GOOD);
        Token rightTag = t.rightSet.createAndAssignToken(right, "tag1");

        // prepare expression which selects all and builds join with tags:
        t.buildCombinations("tag <> xyz");

        //t.assertExists(left, NULL_TOKEN, NULL_META_TOKEN, right, NULL_TOKEN, NULL_META_TOKEN);
        t.assertExists(left, NULL_TOKEN, NULL_META_TOKEN, right, rightTag, NULL_META_TOKEN);

    }

    /**
     * compare: left one entry with tag1; right same entry but with tag2.
     */
    public void testSimpleComparison02()throws ParserException  {
        CompareTester t = new CompareTester(2);
        MovieEntry left = t.leftSet.createEntry("bla", "dir", 5000, 1000, Rating.GOOD);
        Token leftTag = t.leftSet.createAndAssignToken(left, "tag1");

        MovieEntry right = t.rightSet.createEntry("bla", "dir", 5000, 1000, Rating.GOOD);
        Token rightTag = t.rightSet.createAndAssignToken(right, "tag2");

        // prepare expression which selects all and builds join with tags:
        t.buildCombinations("tag <> xyz");

        t.assertExists(left, leftTag, NULL_META_TOKEN, right, NULL_TOKEN, NULL_META_TOKEN);
        t.assertExists(left, NULL_TOKEN, NULL_META_TOKEN, right, rightTag, NULL_META_TOKEN);
    }

    /**
     * compare: left one entry; right same entry but with two tags.
     */
    public void testSimpleComparison03()throws ParserException  {
        CompareTester t = new CompareTester(2);

        MovieEntry left = t.leftSet.createEntry("bla", "dir", 5000, 1000, Rating.GOOD);

        MovieEntry right = t.rightSet.createEntry("bla", "dir", 5000, 1000, Rating.GOOD);
        Token rightTag = t.rightSet.createAndAssignToken(right, "tag1");
        Token rightTag2 = t.rightSet.createAndAssignToken(right, "tag2");

        // prepare expression which selects all and builds join with tags:
        t.buildCombinations("tag <> xyz");

        //t.assertExists(left, NULL_TOKEN, NULL_META_TOKEN, right, NULL_TOKEN, NULL_META_TOKEN);
        t.assertExists(left, NULL_TOKEN, NULL_META_TOKEN, right, rightTag, NULL_META_TOKEN);
        t.assertExists(left, NULL_TOKEN, NULL_META_TOKEN, right, rightTag2, NULL_META_TOKEN);
    }

    /**
     * compare: left one entry with tag; right same entry but without tag.
     */
    public void testSimpleComparison04()throws ParserException  {
        CompareTester t = new CompareTester(1);

        MovieEntry left = t.leftSet.createEntry("bla", "dir", 5000, 1000, Rating.GOOD);
        Token leftTag = t.leftSet.createAndAssignToken(left, "tag1");

        MovieEntry right = t.rightSet.createEntry("bla", "dir", 5000, 1000, Rating.GOOD);

        // prepare expression which selects all and builds join with tags:
        t.buildCombinations("tag <> xyz");

        //t.assertExists(left, NULL_TOKEN, NULL_META_TOKEN, right, NULL_TOKEN, NULL_META_TOKEN);
        t.assertExists(left, leftTag, NULL_META_TOKEN, right, NULL_TOKEN, NULL_META_TOKEN);

    }
}
