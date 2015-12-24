package org.mlsoft.common.swing;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.tree.TreeModelSupport;
import org.mlsoft.structures.AbstractTreeNode;
import org.mlsoft.structures.TreeStructure;
import org.mlsoft.structures.Trees;

/**
 * Treemodel for treenodes.
 */
public class TreeNodeModel implements TreeModel {

    /**
     * Wurzelknoten
     */
    private AbstractTreeNode root;
    protected TreeModelSupport tms = new TreeModelSupport(this);

    /**
     * Erzeugt eine Instanz von {@link MetadataTreeModel}.
     * @param  newroot Wurzel
     */
    public TreeNodeModel(AbstractTreeNode newroot) {
        root = newroot;
    }

    /**
     *
     * setzt eine neue Wurzel
     * @param  newRoot neue Wurzel
     */
    public void setRoot(AbstractTreeNode newRoot) {
        root = newRoot;
        tms.fireNewRoot();
    }

    /**
     *
     * liefert die Wurzel
     * @return  die Wurzel
     */
    @Override
    public Object getRoot() {
        return root;
    }

    /**
     *
     * liefert das Kind Nummer index zu einer Node
     * @param  parent die Node
     * @param  index Kindindex von dieser Node
     * @return das i-te Kind der Node
     */
    @Override
    public Object getChild(Object parent, int index) {
        return ((AbstractTreeNode) parent).getChildAt(index);
    }

    /**
     * liefert Anzahl Kinder einer Node
     * @param  parent die Node
     * @return Anzahl Kinder
     */
    @Override
    public int getChildCount(Object parent) {
        return ((AbstractTreeNode) parent).getChildCount();
    }

    /**
     *
     * pr�ft, ob eine Node ein Blatt ist.
     * @param  node die Node
     * @return true, wenns ein Blatt ist
     */
    @Override
    public boolean isLeaf(Object node) {
        return ((AbstractTreeNode) node).isLeaf();
    }

    /**
     * {@inheritDoc}
     * vorl�ufig nicht implementiert.
     * @param  path {@inheritDoc}
     * @param  newValue {@inheritDoc}
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // macht noch nix...
    }

    /**
     *
     * liefert den Index eines Kindes f�r eine Vater-node
     * @param  parent der Vater
     * @param  child ein Kinde des Vaters
     * @return index des Kindes
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ((AbstractTreeNode) parent).getIndex((AbstractTreeNode) child);
    }

    /**
     * f�gt einen Listener hinzu.
     * @param  l der Listener
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        tms.addTreeModelListener(l);
    }

    /**
     * entfernt einen Listener
     * @param  l der Listener
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        tms.removeTreeModelListener(l);
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
    public TreeStructure[] getPathToRoot(AbstractTreeNode aNode) {
        return Trees.getPathToRoot(root, aNode);
    }
}
