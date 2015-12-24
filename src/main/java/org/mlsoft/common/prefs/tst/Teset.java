/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mlsoft.common.prefs.tst;

import java.awt.Dimension;
import javax.swing.JDialog;
import org.mlsoft.common.prefs.ui.*;

/**
 *
 * @author mla
 */
public class Teset {
    public static void main(String[] args) {
        JDialog  d = new JDialog();
        GenericPrefPanel p = new GenericPrefPanel(AppPrefs.getPrefStructure(), null);
        d.add(p);
        d.setPreferredSize(new Dimension(500, 300));
        d.setVisible(true);
    }

}
