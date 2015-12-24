/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

import org.myjaphoo.model.filterparser.expr.AbstractBoolExpression;
import org.myjaphoo.model.filterparser.idents.Identifiers;
import org.myjaphoo.model.filterparser.operator.Operators;

import java.util.HashMap;

/**
 * @author mla
 */
public class ParserBrickTest extends AbstractParserTest {

    private HashMap<String, Substitution> subs = new HashMap<String, Substitution>();

    {
        subs.put("bm1", new Substitution("bm1", "size > 20000"));
        subs.put("bm2", new Substitution("bm2", "size > 21000"));
        subs.put("bm3", new Substitution("bm3", "$bm1 or $bm2"));
    }

    private FilterBrickParser.ParsedFilterBricks tester(String strexpr) {
        FilterParser parser = new FilterParser(subs);
        AbstractBoolExpression expr = parser.parse(strexpr);
        FilterBrickParser brickParser = new FilterBrickParser();
        return brickParser.parseFilterBrickExpression(expr);
    }

    public void test01() throws ParserException {
        FilterBrickParser.ParsedFilterBricks rslt = tester("text like 'brabbel'");
        assertEquals(0, rslt.bookmarkBricks.size());
        assertEquals(1, rslt.bricks.size());
        assertEquals(Identifiers.TEXT, rslt.bricks.get(0).ident);
        assertEquals("brabbel", rslt.bricks.get(0).literalStr);
        assertEquals(Operators.LIKE, rslt.bricks.get(0).operator);
    }

    public void test02() throws ParserException {
        FilterBrickParser.ParsedFilterBricks rslt = tester("$bm3");
        assertEquals(1, rslt.bookmarkBricks.size());
        assertEquals(0, rslt.bricks.size());
        assertEquals("bm3", rslt.bookmarkBricks.get(0));
    }

}
