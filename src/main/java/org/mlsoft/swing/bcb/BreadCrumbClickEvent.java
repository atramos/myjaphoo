/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.swing.bcb;

import javax.swing.tree.TreePath;

/**
 *
 * @author lang
 */
public class BreadCrumbClickEvent {

    public TreePath treePath;
    
    BreadCrumbClickEvent(TreePath treePath) {
        this.treePath = treePath;
    }
    
}
