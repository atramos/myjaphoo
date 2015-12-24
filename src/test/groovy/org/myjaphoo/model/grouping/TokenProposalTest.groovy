package org.myjaphoo.model.grouping

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * TokenProposalTest 
 * @author lang
 * @version $Id$
 *
 */
class TokenProposalTest extends GroovyTestCase {

    public void testPattern() {
        String ps = "John Doe";
        String regex = TokenAssignemntProposolerPartialGrouper.createLenientPattern(ps);
        Pattern  pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher("blaafl.john.doe.adslfsadlf");
        assertTrue(m.matches());
    }

    public void testWordMatchPattern() {
        String ps = "John Doe";
        String regex = TokenAssignemntProposolerPartialGrouper.createWordmatchPattern(ps);
        Pattern  pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher("blaafl john doe adslfsadlf");
        assertTrue(m.matches());
    }

    public void testWordMatchPattern02() {
        String ps = "John Doe";
        String regex = TokenAssignemntProposolerPartialGrouper.createWordmatchPattern(ps);
        Pattern  pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher("blaafl_john_doe_adslfsadlf");
        assertTrue(m.matches());
    }

    public void testLenientPattern02() {
        String ps = "John Doe";
        String regex = TokenAssignemntProposolerPartialGrouper.createLenientPattern(ps);
        Pattern  pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher("blaafl_john_doe_adslfsadlf");
        assertTrue(m.matches());
    }

    public void testWordMatchPattern2() {
        String ps = "John Doe";
        String regex = TokenAssignemntProposolerPartialGrouper.createWordmatchPattern(ps);
        Pattern  pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher("blaafl,john.doe.adslfsadlf");
        assertTrue(m.matches());
    }

    public void testWordMatchPattern3() {
        String ps = "John Doe";
        String regex = TokenAssignemntProposolerPartialGrouper.createWordmatchPattern(ps);
        Pattern  pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher("blaafljohn.doe.adslfsadlf");
        assertFalse(m.matches());
    }
}
