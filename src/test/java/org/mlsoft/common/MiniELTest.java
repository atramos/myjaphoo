package org.mlsoft.common;

import junit.framework.TestCase;


/**
 * test cases for the mini el.
 */
public class MiniELTest extends TestCase {

    public class A {
        private String myprop = "testval";

        public String getMyprop() {
            return myprop;
        }

        public void setMyprop(String myprop) {
            this.myprop = myprop;
        }

        public int getMyIntMeth() {
            return 7;
        }
    }


    public class B {
         private A myA = new A();

        public A getMyA() {
            return myA;
        }

        public void setMyA(A myA) {
            this.myA = myA;
        }
    }

    public void testTypeAccess() {
        B b = new B();
        Class clazz = MiniEL.getType(b.getClass(), "myA.myprop") ;
        assertEquals(String.class, clazz);
    }
    public void testTypeAccess2() {
        B b = new B();
        Class clazz = MiniEL.getType(b.getClass(), "myA.myIntMeth") ;
        assertEquals(Integer.TYPE, clazz);
    }
}
