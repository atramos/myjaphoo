/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

/**
 *
 * @author mla
 */
public class ParserFunctionTest extends AbstractParserTest {
   
    
    public void testFunctionIsMov() throws ParserException {
        tstTrue("ismov()");
    }
    
    public void testFunctionIsPic() throws ParserException {
        assertFalse(tst("ispic()"));
        assertFalse(tst("istxt()"));
        tstTrue("not(ispic())");
        tstTrue("not(istxt())");
    }
    
    public void testRetainLettersFunction01() throws ParserException {
        tstTrue("'aaabbbccc' = retainletters(dir)");
    }
    
    public void testRetainLettersFunction02() throws ParserException {
        tstTrue("'blampg' = retainletters(name)");
    }    
    
    public void testRetainLettersFunction03() throws ParserException {
        try {
            tstTrue("'blampg' = retainletters(5)");
            fail("type error expected here!");
        } catch (ParserException pe) {
            // right, this should fail because of typeerrors
        }
    }

    public void testSplitFunction01() throws ParserException {
        tstTrue("'aaa' = split('aaa', ' ')");
    }

    public void testSplitFunction02() throws ParserException {
        tstTrue("aaa|a = split('aa;a', ';')");
    }

    public void testConcatFunction01() throws ParserException {
        tstTrue("'aaa' = concat('aa', 'a')");
    }
}
