/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

import org.myjaphoo.model.filterparser.expr.AbstractBoolExpression;
import org.myjaphoo.model.filterparser.visitors.AggregationTesterVisitor;

/**
 * @author mla
 */
public class TestAggExprVisitorParserTest extends AbstractParserTest {


    private boolean isAgg(String strexpr) {
        FilterParser parser = new FilterParser(null);
        AbstractBoolExpression expr = parser.parse(strexpr);

        // check visitor:
        AggregationTesterVisitor v = new AggregationTesterVisitor();
        return v.visit(expr);
    }

    public void testNot03() throws ParserException {
        assertFalse(isAgg("name=bla "));
    }

    public void testNot01() throws ParserException {
        assertFalse(isAgg("name=bla and not (path like cc)"));
    }

    public void testNot02() throws ParserException {
        assertFalse(isAgg("not (path like cc)"));
    }

    public void testNot021() throws ParserException {
        assertFalse(isAgg("not (path wie cc)"));
    }

    public void testAgg01() throws ParserException {
        assertTrue(isAgg("sum(size) > 7mb"));
    }

}
