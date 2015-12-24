package org.mlsoft.common;

import junit.framework.TestCase;

public class DefaultPropertyAccessImplTest extends TestCase {

    public class B {

        private String attr;

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }
    }


    public class MyClass {

        private String attribute;

        public MyClass(String attribute) {
            this.attribute = attribute;
        }

        private B b = new B();

        public String getAttribute() {
            return attribute;
        }

        public void setAttribute(String attribute) {
            this.attribute = attribute;
        }

        public B getB() {
            return b;
        }

        public void setB(B b) {
            this.b = b;
        }
    }

    public class MyDerivedClass extends MyClass {


        public MyDerivedClass(String attribute) {
            super(attribute);
        }
    }

    public void testSetter() {
        DefaultPropertyAccessImpl accessor = new DefaultPropertyAccessImpl("attribute");
        MyClass myClass = new MyClass(null);
        accessor.setVal(myClass, "hello");

        assertEquals("hello", myClass.getAttribute());
    }

    public void testGetter() {
        DefaultPropertyAccessImpl accessor = new DefaultPropertyAccessImpl("attribute");
        MyClass myClass = new MyClass("hello");
        assertEquals("hello", accessor.getVal(myClass));
    }

    public void testDerivedClassSetter() {
        DefaultPropertyAccessImpl accessor = new DefaultPropertyAccessImpl("attribute");
        MyDerivedClass myClass = new MyDerivedClass(null);
        accessor.setVal(myClass, "hello");

        assertEquals("hello", myClass.getAttribute());
    }

    public void testDerivedClassGetter() {
        DefaultPropertyAccessImpl accessor = new DefaultPropertyAccessImpl("attribute");
        MyDerivedClass myClass = new MyDerivedClass("hello");
        assertEquals("hello", accessor.getVal(myClass));
    }

    public void testNestedAttributes() {
        DefaultPropertyAccessImpl accessor = new DefaultPropertyAccessImpl("b.attr");
        MyDerivedClass myClass = new MyDerivedClass("hello");
        accessor.setVal(myClass, "bla");
        assertEquals("bla", myClass.getB().attr);

        // read attribute:
        assertEquals("bla", accessor.getVal(myClass));
    }

}