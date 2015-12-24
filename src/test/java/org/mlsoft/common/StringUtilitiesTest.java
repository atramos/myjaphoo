/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.common;

import junit.framework.TestCase;

/**
 *
 * @author mla
 */
public class StringUtilitiesTest extends TestCase {

    public void testRetainLetters01() {
        assertEquals("abcefghi", StringUtilities.retainLetters("abc123efg78hi"));
    }

    public void testRetainLetters02() {
        assertEquals("abcefghi", StringUtilities.retainLetters("abc1.2-3efg78hi"));
    }

    public void testRemoveSuffix01() {
        assertEquals("abc", StringUtilities.removeSuffix("abc.mpg"));
    }

    public void testRemoveSuffix02() {
        assertEquals("abc", StringUtilities.removeSuffix("abc"));
    }

    public void testRemoveSuffix03() {
        assertEquals("abc.def", StringUtilities.removeSuffix("abc.def.mpg"));
    }
}
