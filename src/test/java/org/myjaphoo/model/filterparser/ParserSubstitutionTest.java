/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser;

import java.util.HashMap;

/**
 *
 * @author mla
 */
public class ParserSubstitutionTest extends AbstractParserTest {

    private HashMap<String, Substitution> subs = new HashMap<String, Substitution>();

    {
        subs.put("bm1", new Substitution("bm1", "size > 20000"));
        subs.put("bm2", new Substitution("bm2", "subst('bm3')"));
        subs.put("bm3", new Substitution("bm3", "subst('bm2')"));
    }

    public void test01() throws ParserException {
        assertTrue(tst("subst('bm1')", subs));
        assertTrue(tst("((subst('bm1')))", subs));
        assertFalse(tst("not(subst('bm1'))", subs));
        assertTrue(tst("path like bla and subst('bm1')", subs));
        assertTrue(tst("path like vvvv or subst('bm1')", subs));
        assertFalse(tst("path like vvvv and subst('bm1')", subs));
        // testing same with $ instead of subst:
        assertTrue(tst("$bm1", subs));
        assertTrue(tst("$\"bm1\"", subs));
        assertTrue(tst("(($bm1))", subs));
        assertFalse(tst("not($bm1)", subs));
        assertTrue(tst("path like bla and $\"bm1\"", subs));
        assertTrue(tst("path like vvvv or $bm1", subs));
        assertFalse(tst("path like vvvv and $bm1", subs));
    }

    public void testMaxNesting() {
        try {
            tst("subst('bm2')", subs);
            throw new AssertionError("max nesting for endless loop not handled");
        } catch (ParserException ex) {
            // ok
        }
    }

}
