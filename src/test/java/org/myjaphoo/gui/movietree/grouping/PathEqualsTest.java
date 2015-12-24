/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui.movietree.grouping;

import junit.framework.TestCase;
import org.myjaphoo.model.grouping.Path;

/**
 *
 * @author mla
 */
public class PathEqualsTest extends TestCase {

    public PathEqualsTest() {
    }

      public void testPathEqual() {
        Path p1 = new Path((String)null, new String[]{"hello", "world"});
        Path p2 = new Path((String)null, new String[]{"hello", "world"});

        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
        assertEquals(p1, p2);

    }

      public void testPathEqual2() {
        Path p1 = new Path((String)null, new String[]{"hello", "world"});
        String hello = "he" + "llo";
        String world = "wor" + "ld";
        Path p2 = new Path((String)null, new String[]{hello, world});

        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
        assertEquals(p1, p2);

    }

      public void testPathEqualTimeTest() {
        Path p1 = new Path((String)null, new String[]{"hello", "world", "blabla", "blubb"});
        Path p2 = new Path((String)null, new String[]{"hello", "world", "blabla", "blubb"});

        long start= System.currentTimeMillis();
        for (int i=0; i<1000000; i++) {
            p1.equals(p2);
        }
        long stop = System.currentTimeMillis();
        System.out.println("time = " + (stop-start));

    }
}