/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.common.prefs.ui;

import java.util.ArrayList;
import java.util.List;

import org.mlsoft.common.prefs.model.edit.EditableGroup;
import org.mlsoft.common.prefs.model.editors.EditorGroup;

import org.mlsoft.common.swing.TreeNodeModel;
import org.mlsoft.structures.AbstractTreeNode;

/**
 *
 * @author mla
 */
public class PrefPanelTreeModel extends TreeNodeModel {

    public PrefPanelTreeModel(AbstractTreeNode newroot) {
        super(newroot);
    }

    @Override
    public int getChildCount(Object parent) {

        return getFilteredChildren(parent).size();
    }

    @Override
    public boolean isLeaf(Object node) {
        return getFilteredChildren(node).size() == 0;
    }

    /**
     *
     * liefert das Kind Nummer index zu einer Node
     * @param  parent die Node
     * @param  index Kindindex von dieser Node
     * @return das i-te Kind der Node
     */
    public Object getChild(Object parent, int index) {
        return getFilteredChildren(parent).get(index);
    }

    /**
     *
     * liefert den Index eines Kindes f�r eine Vater-node
     * @param  parent der Vater
     * @param  child ein Kinde des Vaters
     * @return index des Kindes
     */
    public int getIndexOfChild(Object parent, Object child) {
        return getFilteredChildren(parent).indexOf(child);

    }

    /**
     * Filters the children, to cut off all value nodes, and only show 
     * the group nodes
     * @param parent
     * @return 
     */
    private List<AbstractTreeNode> getFilteredChildren(Object parent) {
        List<AbstractTreeNode> children = ((AbstractTreeNode) parent).getChildren();
        if (parent instanceof EditorGroup) {
            ArrayList<AbstractTreeNode> filtered = new ArrayList<AbstractTreeNode>();
            for (AbstractTreeNode node : children) {
                if (node instanceof EditorGroup) {
                    filtered.add(node);
                }
            }
            return filtered;
        } else {
            return children;
        }
    }
}
