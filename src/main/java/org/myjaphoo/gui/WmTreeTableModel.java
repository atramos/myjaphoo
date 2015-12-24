/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui;

import javax.swing.tree.TreePath;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.mlsoft.structures.TreeStructure;
import org.mlsoft.structures.Trees;
import org.mlsoft.structures.AbstractTreeNode;

/**
 *
 * @author mla
 */
public abstract class WmTreeTableModel extends AbstractTreeTableModel {

    /** die column namen. */
    private String[] columns;

    /**
     * Erzeugt eine Instanz von {@link MetadataTreeModel}.
     * @param  newroot Wurzel
     */
    public WmTreeTableModel(AbstractTreeNode newroot, String[] columns) {
        super(newroot);
        this.columns = columns;
    }

    /**
     *
     * setzt eine neue Wurzel
     * @param  newRoot neue Wurzel
     */
    public void setRoot(AbstractTreeNode newRoot) {
        root = newRoot;
        modelSupport.fireNewRoot();
    }

    /**
     *  Invoke this method if you've totally changed the children of
     *  node and its childrens children...  This will post a
     *  treeStructureChanged event.
     * @param  node die Node
     */
    public void nodeStructureChanged(AbstractTreeNode node) {
        if (node != null) {
            modelSupport.fireTreeStructureChanged(new TreePath(getPathToRoot(node)));
        }
    }

    /**
     *  Invoke this method after you've changed how node is to be
     *  represented in the tree.
     * @param  node die Node
     */
    public void nodeChanged(AbstractTreeNode node) {
        modelSupport.firePathChanged(new TreePath(getPathToRoot(node)));
    }

    /**
     * Invoke this method if you've modified the TreeNodes upon which this
     * model depends.  The model will notify all of its listeners that the
     * model has changed below the node <code>node</code> (PENDING).
     * @param  node die Node
     */
    public void reload(AbstractTreeNode node) {
        if (node != null) {
            modelSupport.fireTreeStructureChanged(new TreePath(getPathToRoot(node)));
        }
    }

    /**
     * Invoke this method if you've modified the TreeNodes upon which this
     * model depends.  The model will notify all of its listeners that the
     * model has changed.
     */
    public void reload() {
        reload((AbstractTreeNode) root);
    }

    /**
     *
     * liefert das Kind Nummer index zu einer Node
     * @param  parent die Node
     * @param  index Kindindex von dieser Node
     * @return das i-te Kind der Node
     */
    @Override
    public final Object getChild(Object parent, int index) {
        return ((AbstractTreeNode) parent).getChildAt(index);
    }

    /**
     * liefert Anzahl Kinder einer Node
     * @param  parent die Node
     * @return Anzahl Kinder
     */
    @Override
    public final int getChildCount(Object parent) {
        return ((AbstractTreeNode) parent).getChildCount();
    }

    /**
     * {@inheritDoc}
     * vorläufig nicht implementiert.
     * @param  path {@inheritDoc}
     * @param  newValue {@inheritDoc}
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // macht noch nix...
    }

    /**
     *
     * liefert den Index eines Kindes für eine Vater-node
     * @param  parent der Vater
     * @param  child ein Kinde des Vaters
     * @return index des Kindes
     */
    @Override
    public final int getIndexOfChild(Object parent, Object child) {
        return ((AbstractTreeNode) parent).getIndex((AbstractTreeNode) child);
    }

    /**
     * Builds the parents of node up to and including the root node,
     * where the original node is the last element in the returned array.
     * The length of the returned array gives the node's depth in the
     * tree.
     *
     * @param aNode the TreeNode to get the path for
     * @return Pfad
     */
    public final TreeStructure[] getPathToRoot(AbstractTreeNode aNode) {
        return Trees.getPathToRoot((AbstractTreeNode) root, aNode);
    }

    @Override
    public final int getColumnCount() {
        return columns.length;
    }

    @Override
    public final String getColumnName(int column) {
        return columns[column];
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }
}
