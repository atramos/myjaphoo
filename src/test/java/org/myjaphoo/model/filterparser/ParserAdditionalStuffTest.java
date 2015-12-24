/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

import org.myjaphoo.model.filterparser.ParserException;

/**
 * Tests, that test additional new features of the parser.
 * @author mla
 */
public class ParserAdditionalStuffTest extends AbstractParserTest {

    public void testLiteralThenIdent() throws ParserException {
        assertFalse(tst("bla=name"));
    }

    public void testTwoLiterals() throws ParserException {
        assertTrue(tst("1 < 3"));
    }

    public void testFunctionsOnOneSite() throws ParserException {
        assertTrue(tst("'blabla' = concat('bla','bla')"));
    }

    public void testEndswith() throws ParserException {
        assertTrue(tst("'blabla.mp4' endswith '.mp4'"));
    }

    public void testdatecomparedwithnothing() throws ParserException {
        assertTrue(tst("exif_create_date is nothing"));
    }

    public void testIdentifiersAsArgumentsForFunctions01() throws ParserException {
        assertTrue(tst("'bla.mpgbla.mpg' = concat(name,name)"));
    }
    
    public void testIdentifiersAsArgumentsForFunctions02() throws ParserException {
        assertTrue(tst("'bla.mpgbla.mpgbla.mpg' = concat(name, concat(name,name))"));
    }    
}
