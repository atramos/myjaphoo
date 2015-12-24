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
public class PathTest extends TestCase {

    public PathTest() {
    }





    public void testPathMerging() {
        Path p1 = new Path((String)null, new String[]{"a", "b"});
        Path p2 = new Path((String)null, new String[]{"c", "d"});
        Path merged = new Path(p1, p2);
        assertEquals("d", merged.getLastPathName());
        Path parentC = merged.getParentPath();
        assertEquals("c", parentC.getLastPathName());
        Path parentB = parentC.getParentPath();
        assertEquals("b", parentB.getLastPathName());
        Path parentA = parentB.getParentPath();
        assertEquals("a", parentA.getLastPathName());
    }

    public void testPathMerging2() {
        Path p1 = new Path((String)null, new String[]{"a", "b"});
        Path p2 = new Path((String)null, new String[]{"c", });
        Path merged = new Path(p1, p2);

        Path expected = new Path((String)null, new String[]{"a", "b", "c"});
        assertEquals(expected, merged);
    }

    public void testPathMerging3() {
        Path p1 = new Path((String)null, new String[]{"a",});
        Path p2 = new Path((String)null, new String[]{"c", });
        Path merged = new Path(p1, p2);

        Path expected = new Path((String)null, new String[]{"a", "c"});
        assertEquals(expected, merged);
    }
}