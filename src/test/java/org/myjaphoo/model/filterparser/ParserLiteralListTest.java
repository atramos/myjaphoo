/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

/**
 *
 * @author mla
 */
public class ParserLiteralListTest extends AbstractParserTest {

    public void test01() throws ParserException {
        tstTrue("path like bla&aaa & ccc");
    }

    public void test02() throws ParserException {
        tstTrue("name = 'bla.mpg' | zezr | erzewz");
    }

    public void test03() throws ParserException {
        assertFalse(tst("name = bla & zezr & erzewz"));
    }

    public void test04() throws ParserException {
        tstTrue("not(name = bla & zezr & erzewz)");
    }

    public void test05() throws ParserException {
        assertFalse(tst("path like erwe&rwewqr & cvbvb"));
    }

    public void test06() throws ParserException {
        tstTrue("path like aaa | vvcxv | ewrq");
    }

    public void test07() throws ParserException {
        assertFalse(tst("path like aaa | vvcxv | ewrq and path like hurz"));
    }

    public void testLiteralsOfDifferentTypeNotAllowed() throws ParserException {
        try {
        tst("path like bla&04");
            fail();
        } catch (ParserException pe) {

        }
    }

    public void test08() throws ParserException {
        tstTrue("rating = GOOD");
    }

    public void test09() throws ParserException {
        assertFalse(tst("rating > GOOD"));
    }
}
