/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.structures;

import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * @author lang
 */
public abstract class AbstractTreeNode<C extends AbstractTreeNode<C>> implements TreeNode, TreeStructure<C> {

    private C parent;
    private List<C> children = new ArrayList<C>();

    /**
     * unmodifizierte liste, die nach dem destruieren hingehängt wird.
     * Das ist "saver" als einfach die referenz auf null zu setzen.
     */
    private static final List DESTRUCTED_LIST = Collections.unmodifiableList(new ArrayList());

    public AbstractTreeNode() {
    }

    public AbstractTreeNode(C parent) {
        this.parent = parent;
    }

    public AbstractTreeNode(boolean isLeaf) {
        if (isLeaf) {
            children = DESTRUCTED_LIST;
        }
    }

    public void sortList(Comparator comparator) {
        if (children.size() > 0) {
            // prevent sort on immutable lists (the desctruct list)
            Collections.sort(children, comparator);
        }
    }

    public void addChild(C child) {
        children.add(child);
        child.setParent((C) this);
    }

    public void removeChild(C child) {
        children.remove(child);
    }

    @Override
    public C getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public C getParent() {
        return parent;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    public void setParent(C parent) {
        this.parent = parent;
    }

    public List<C> getChildren() {
        return children;
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration(children);
    }


    public void destruct() {
        parent = null;
        for (AbstractTreeNode child : children) {
            child.destruct();
        }
        if (children != DESTRUCTED_LIST) {
            children.clear();
            children = DESTRUCTED_LIST;
        }
    }

    protected AbstractTreeNode<C> findRoot() {
        AbstractTreeNode<C> root = this;
        while (root.getParent() != null)
            root = root.getParent();
        return root;
    }
}
