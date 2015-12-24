/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

/**
 * @author lang
 */
public class ParserQualifierTest extends AbstractParserTest {

    public void testLeftQual01() throws ParserException {
        tstTrue("left.name='bla.mpg' ");
    }

    public void testTagParenQual01() throws ParserException {
        tstTrue("tag.parents='ptokname' ");
    }

    public void testTagParenQual02() throws ParserException {
        tstTrue("left.tag.parents='ptokname' ");
    }

    public void testTagParenQual03() throws ParserException {
        assertFalse(tst("left.tag.parents='xyz' "));
    }

    public void testTagParenQual04() throws ParserException {
        try {
            assertFalse(tst("left.left.left.left.tag.tag.tag.tag.parents='xyz' "));
            fail();
        } catch (ParserException p) {

        }
    }
}
