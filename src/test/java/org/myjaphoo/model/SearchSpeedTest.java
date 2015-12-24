/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model;


import junit.framework.TestCase;

/**
 *
 * @author mla
 */
public class SearchSpeedTest extends TestCase {

    //private static BoyerMooreHorspool bmh = new BoyerMooreHorspool();
    private String textToSearch = "blabla this is a demo text";
    private String pattern = "is a demo";

    private int rounds = 10000000;

    public void testSpeedNormalJavaSearch() {
        for (int i = 0; i < rounds; i++) {
            textToSearch.indexOf(pattern);
        }
    }

 
}
