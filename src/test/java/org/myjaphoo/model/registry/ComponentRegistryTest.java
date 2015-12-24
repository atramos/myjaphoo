package org.myjaphoo.model.registry;

import junit.framework.TestCase;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * ComponentRetistryTest
 * @author mla
 * @version $Id$
 */
public class ComponentRegistryTest extends TestCase {

    public static interface I1 {

        public void run();
    }

    public static class C1 implements I1 {

        public C1() {
        }

        @Override
        public void run() {
            System.out.println("run!");
        }
    }


    public void testReg1() {
        ComponentRegistry.registry.clear();
        ComponentRegistry.registry.register("myclass", I1.class, C1.class);

        I1 iInstance = ComponentRegistry.registry.newInstance(I1.class);
    }

    public void testReg2() {
        ComponentRegistry.registry.clear();
        C1 c1 = new C1();
        ComponentRegistry.registry.register("name1", I1.class, c1);
        C1 c2 = new C1();
        ComponentRegistry.registry.register("name2", I1.class, c2);

        Set<Map.Entry<String, I1>> entries = ComponentRegistry.registry.getEntries(I1.class);
        Iterator<Map.Entry<String, I1>> iterator = entries.iterator();
        assertEquals(c1, iterator.next().getValue());
        assertEquals(c2, iterator.next().getValue());

        Collection<I1> coll = ComponentRegistry.registry.getEntryCollection(I1.class);
        Iterator<I1> iter = coll.iterator();
        assertEquals(c1, iter.next());
        assertEquals(c2, iter.next());
    }

    public void testReg3() {
        ComponentRegistry.registry.clear();
        C1 c1 = new C1();
        ComponentRegistry.registry.register("name1", I1.class, c1);

        I1 rc = ComponentRegistry.registry.getSingleEntry(I1.class);

        assertEquals(c1, rc);
    }
}
