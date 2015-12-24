package org.mlsoft.structures;

import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import org.myjaphoo.gui.movietree.MovieStructureNode;

/**
 * Tests the Trees class.
 */
public class TreesTest extends TestCase {

    static class TestStruct {
        MovieStructureNode a = new MovieStructureNode("a");
        MovieStructureNode b = new MovieStructureNode(("b"));
        MovieStructureNode bb = new MovieStructureNode(("bb"));
        MovieStructureNode c = new MovieStructureNode(("c"));
        MovieStructureNode cc = new MovieStructureNode(("cc"));

        {
            a.addChild(b);
            a.addChild(bb);
            b.addChild(c);
            b.addChild(cc);
        }
    }

    private static Trees.EqualNodeFunction<TreeStructure> PATHNAME_EQ = new Trees.EqualNodeFunction<TreeStructure>() {

        @Override
        public boolean isEqual(TreeStructure node1, TreeStructure node2) {
            if (node1 instanceof MovieStructureNode && node2 instanceof MovieStructureNode) {
                return StringUtils.equals(((MovieStructureNode) node1).getName(), ((MovieStructureNode) node2).getName());
            } else {
                return false;
            }
        }
    };


    public void testPathSearch() {
        TestStruct t = new TestStruct();

        // simple self search:
        MovieStructureNode foundNode = (MovieStructureNode) Trees.pathSearch((TreeStructure) t.a, t.b, PATHNAME_EQ);
        assertEquals(t.b, foundNode);

    }

    public void testPathSearch2() {
        TestStruct t = new TestStruct();

        // simple self search:
        MovieStructureNode foundNode = (MovieStructureNode) Trees.pathSearch((TreeStructure) t.a, t.c, PATHNAME_EQ);
        assertEquals(t.c, foundNode);

    }

    public void testPathSearch3() {
        TestStruct t = new TestStruct();

        MovieStructureNode x = new MovieStructureNode("x");
        // simple self search:
        MovieStructureNode foundNode = (MovieStructureNode) Trees.pathSearch((TreeStructure) t.a, x, PATHNAME_EQ);
        assertNull(foundNode);

    }
}
