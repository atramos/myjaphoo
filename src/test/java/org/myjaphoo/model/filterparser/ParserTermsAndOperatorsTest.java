/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

/**
 * Tests new term and operator enhancements which where introduced in v3.4. E.g.
 * terms consisting of operators that result in non-boolean values.
 *
 * @author mla
 */
public class ParserTermsAndOperatorsTest extends AbstractParserTest {

    public void testTermWithOperatorPlusWithStrings01() throws ParserException {
        assertTrue(tst("'bla.mpgbla.mpg' = name + name"));
    }

    public void testTermWithOperatorPlusWithStrings02() throws ParserException {
        assertTrue(tst("'bla.mpgbla.mpgbla.mpg' = name+name+name"));
    }

    public void testTermInFunction() throws ParserException {
        assertTrue(tst("'0' = seq(name + name)"));
    }

    public void testOperatorDivWithNumbers01() throws ParserException {
        assertTrue(tst("1 = 5 / 5 "));
    }

    public void testOperatorMultWithNumbers01() throws ParserException {
        assertTrue(tst("1 < 1 * 5 "));
    }

    public void testOperatorMinusWithNumbers01() throws ParserException {
        assertTrue(tst("1 > 1 - 1 "));
    }

    public void testOperatorPlusWithNumbers01() throws ParserException {
        assertTrue(tst("1 < 1 + 1 "));
    }

    public void testOperatorPlusWithNumbers02() throws ParserException {
        assertTrue(tst("1 < 1+1 "));
    }

    public void testOperatorPlusWithNumbers03() throws ParserException {
        assertTrue(tst("2 = 1+1 "));
    }

    public void testOperatorPlusWithNumbers04() throws ParserException {
        assertTrue(tst("3 = 1+1+1 "));
    }

    public void testOperatorPlusWithBraces01() throws ParserException {
        assertTrue(tst("3 = (1+1+1) "));
    }

    public void testOperatorPlusWithBraces02() throws ParserException {
        assertTrue(tst("3 = (1+1) +1 "));
    }

    public void testOperatorPlusWithBraces03() throws ParserException {
        assertTrue(tst("(3 = 1+ (1 +1)) "));
    }

    public void testOperatorBiggerThan01() throws ParserException {
        assertTrue(tst("3 >= 3"));
    }

    public void testOperatorBiggerThan02() throws ParserException {
        assertTrue(tst("4 >= 3"));
    }

    public void testOperatorBiggerThan03() throws ParserException {
        assertFalse(tst("2 >= 3"));
    }

    public void testOperatorSmallerThan01() throws ParserException {
        assertTrue(tst("3 <= 3"));
    }

    public void testOperatorSmallerThan02() throws ParserException {
        assertTrue(tst("2 <= 3"));
    }

    public void testOperatorSmallerThan03() throws ParserException {
        assertFalse(tst("4 <= 3"));
    }
}
