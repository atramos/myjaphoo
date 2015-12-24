/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

/**
 * @author mla
 */
public class ParserAttributesTest extends AbstractParserTest {


    public void test01() throws ParserException {
        tstTrue("entryattr(attr1) = val1");
        assertFalse(tst("entryattr(attr1) = val2"));
    }

    public void test02() throws ParserException {
        tstTrue("entryattr#attr1 = val1");
        assertFalse(tst("entryattr#attr1 = val2"));
    }

    public void test03() throws ParserException {
        tstTrue("entryattr(attr1) + 'b' = val1b");
        //tstTrue("(entryattr#attr1) + 'b' = val1b");
        //tstTrue("entryattr#attr1 + 'b' = val1b");

    }
    public void test04() throws ParserException {
        //tstTrue("entryattrs() = val1");
        assertFalse(tst("entryattr#attr1 = val2"));
    }

    public void test05() throws ParserException {
        tstTrue("entryattr#attr3 is nothing");
        tstTrue("entryattr(attr3) is nothing");

        tstTrue("tagattr(attr3) is nothing");

        tstTrue("metatagattr(attr3) is nothing");
    }
}
