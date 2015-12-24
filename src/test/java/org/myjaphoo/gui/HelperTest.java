/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui;

import java.awt.Color;
import junit.framework.TestCase;
import org.myjaphoo.gui.util.Helper;

/**
 *
 * @author mla
 */
public class HelperTest  extends TestCase {


    public void testwrapcolor() {
        String result = Helper.wrapColored(Color.blue, "hallo");
        System.out.println(result);
    }
}
