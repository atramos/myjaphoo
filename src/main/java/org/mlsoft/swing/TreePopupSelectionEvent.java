/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.swing;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;

/**
 *
 * @author lang
 */
public class TreePopupSelectionEvent {

    public final JTree tree;
    public final TreeSelectionEvent evt;

    public TreePopupSelectionEvent(JTree tree, TreeSelectionEvent evt) {
        this.tree = tree;
        this.evt = evt;
    }
}
