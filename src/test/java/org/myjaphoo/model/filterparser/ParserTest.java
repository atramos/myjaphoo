/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

import org.myjaphoo.model.filterparser.expr.AbstractBoolExpression;

import java.util.HashMap;

/**
 *
 * @author mla
 */
public class ParserTest extends AbstractParserTest {

    public void testNot01() throws ParserException {
        assertFalse(tst("name=bla and not (path like cc)"));
    }

    public void testNot02() throws ParserException {
        assertFalse(tst("not (path like cc)"));
    }

    public void testNot021() throws ParserException {
        assertFalse(tst("not (path wie cc)"));
    }

    public void testNot03() throws ParserException {
        tstTrue("not(name=yyy)");
    }

    public void testNot04() throws ParserException {
        tstTrue("not(name=yyy) and not(path=bb)");
    }

    public void testNot041() throws ParserException {
        tstTrue("not(name is yyy) and not(path is bb)");
    }

    public void test01() throws ParserException {
        tstTrue("name='bla.mpg'");
    }

    public void testNotEqual() throws ParserException {
        assertFalse(tst("name<>'bla.mpg'"));
    }

    public void testSimpleWordSearch() throws ParserException {
        assertTrue(tst("bla"));
    }

    public void testSimpleWordSearch2() throws ParserException {
        assertTrue(tst("mytok"));
    }

    public void test011() throws ParserException {
        tstTrue("name is 'bla.mpg'");
    }

    public void test02() throws ParserException {
        assertFalse(tst("name=trullalal"));
    }

    public void test021() throws ParserException {
        assertFalse(tst("name is trullalal"));
    }

    public void test03() throws ParserException {
        tstTrue("name like bl");
    }

    public void test031() throws ParserException {
        tstTrue("name wie bl");
    }

    public void testPath() throws ParserException {
        tstTrue("path like 'bb'");
    }

    public void testPath1() throws ParserException {
        tstTrue("path wie 'bb'");
    }

    public void testSize() throws ParserException {
        tstTrue("size > 20000");
    }

    public void testUnitPrefixWithHiger() throws ParserException {
        tstTrue("size > 20KB");
    }

    public void testUnitPrefixWithLower() throws ParserException {
        tstTrue("size < 70 KB");
    }

    public void testUnitPrefixWithLower2() throws ParserException {
        assertFalse(tst("size > 60 KB"));
    }

    public void testLower() throws ParserException {
        tstTrue("size < 70000");
    }

    public void testLower02() throws ParserException {
        assertFalse(tst("size < 20000"));
    }

    public void testSize02() throws ParserException {
        assertFalse(tst("size > 60000"));
    }

    public void testTokenMatch() throws ParserException {
        tstTrue("tag = mytok");
    }

    public void testTokenLikeMatch() throws ParserException {
        tstTrue("tag like 'yto'");
    }

    public void testTokenLikeMatch1() throws ParserException {
        tstTrue("tag wie 'yto'");
    }

    public void testTokenTypeLikeMatch() throws ParserException {
        tstTrue("tagtype like 'viename'");
    }

    public void testTokenTypeLikeMatch1() throws ParserException {
        tstTrue("tagtype wie 'viename'");
    }

    public void testTokenDescrLikeMatch() throws ParserException {
        tstTrue("tagdescr like 'hallo'");
    }

    public void testTokenDisMatch() throws ParserException {
        assertFalse(tst("tag = anothertok"));
    }

    public void testTokenIsNothing() throws ParserException {
        assertTrue(tst2("tag = nothing"));
    }

    public void testTokenIsNothing1() throws ParserException {
        assertTrue(tst2("tag is nothing"));
    }

    public void testNotTokenIsNothing() throws ParserException {
        assertFalse(tst2("not(tag = nothing)"));
    }

    public void testAndMatch() throws ParserException {
        tstTrue("size > 30000 and name='bla.mpg'");
    }

    public void testAndDisMatch() throws ParserException {
        assertFalse(tst("size > 60000 and name='bla.mpg'"));
    }

    public void testOrDisMatch() throws ParserException {
        assertFalse(tst("size > 60000 or name='blubb'"));
    }

    public void testOrMatch() throws ParserException {
        tstTrue("size > 60000 or name='bla.mpg'");
    }

    public void testBracesMatch() throws ParserException {
        tstTrue("size > 20000 and (name='murks' or path like 'bb')");
    }

    public void testBracesFail() throws ParserException {
        assertFalse(tst("(size > 20000 and name='murks') or path like 'ddd'"));
    }

    public void testBracesParseFail() {
        FilterParser parser = new FilterParser(new HashMap<String, Substitution>());
        try {
            AbstractBoolExpression expr = parser.parse("(size > 20000 and name='murks' or path like 'ddd'");
            fail("no parser failure because of missing close brace");
        } catch (ParserException r) {
        }
    }

    public void testLexerError() {
        FilterParser parser = new FilterParser(new HashMap<String, Substitution>());
        try {
            AbstractBoolExpression expr = parser.parse("!(a<b)");
            fail("we did not recognize the lexer error and continue successfully with parsing!");
        } catch (ParserException r) {
        }
    }

    public void testNotAnd() throws ParserException {
        tstTrue("not(path like blabal) and path like cc ");
        tstTrue("path like cc and not(path like blabal)");
        assertFalse(tst("path like ff and not(path like blabal)"));
        assertFalse(tst("not(path like blabal) and path like ff "));
    }

    public void testDate() throws ParserException {
        tstTrue("exif_create_date < '2000/12/15'");
    }

    public void testStartwith() throws ParserException {
        tstTrue("path startswith \"aaa/bbb\"");
    }
}
