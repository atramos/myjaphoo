/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.swing.jxtree;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.mlsoft.common.DefaultPropertyAccessImpl;
import org.mlsoft.common.PropertyAccessor;
import org.mlsoft.structures.TreeStructure;
import org.mlsoft.swing.jtable.ChangeAction;
import org.mlsoft.swing.jtable.ColDescr;
import org.mlsoft.swing.util.MappingUtils;

import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Mapped tree table model.
 */
public class MappedTreeTableModel<T> extends AbstractTreeTableModel {

    private Class<T> clazz;
    private ColDescr[] colDescrs;

    private ResourceBundle localeBundle = null;

    private PropertyAccessor parentAccessor;
    private PropertyAccessor childrenAccessor;

    private ChangeAction<T> changeAction;

    /**
     * Constructor.
     *
     * @param root Wurzel
     */
    public MappedTreeTableModel(T root, Class<T> clazz, String parentExpr, String childrenExpr, ColDescr... colDescrs) {
        super(root);
        this.clazz = clazz;
        this.colDescrs = colDescrs;
        this.parentAccessor = new DefaultPropertyAccessImpl(parentExpr);
        this.childrenAccessor = new DefaultPropertyAccessImpl(childrenExpr);
    }


    public static <T> MappedTreeTableModel<T> configure(JXTreeTable tree, T root, Class<T> clazz, String parentExpr, String childrenExpr, ColDescr... colDescrs) {
        MappedTreeTableModel model = new MappedTreeTableModel(root, clazz, parentExpr, childrenExpr, colDescrs);
        tree.setTreeTableModel(model);
        model.configureTable(tree);
        return model;
    }

    private void configureTable(JXTreeTable tree) {
        for (int i = 0; i < colDescrs.length; i++) {
            ColDescr col = colDescrs[i];
            TableColumn tableCol = tree.getColumnModel().getColumn(i);
            if (col.getCellRenderer() != null) {
                tableCol.setCellRenderer(col.getCellRenderer());
            }
            if (col.getCellEditor() != null) {
                tableCol.setCellEditor(col.getCellEditor());
            }
            if (col.getWidth() > 0) {
                tableCol.setWidth(col.getWidth());
                tableCol.setMinWidth(col.getWidth());
            }
        }
    }

    /**
     * sets a new root.
     */
    public void setRoot(T newRoot) {
        root = newRoot;
        modelSupport.fireNewRoot();
    }

    /**
     * Invoke this method if you've totally changed the children of
     * node and its childrens children...  This will post a
     * treeStructureChanged event.
     *
     * @param node die Node
     */
    public void nodeStructureChanged(T node) {
        if (node != null) {
            modelSupport.fireTreeStructureChanged(new TreePath(getPathToRoot(node)));
        }
    }

    /**
     * Invoke this method after you've changed how node is to be
     * represented in the tree.
     *
     * @param node die Node
     */
    public void nodeChanged(T node) {
        modelSupport.firePathChanged(new TreePath(getPathToRoot(node)));
    }

    public void nodeAdded(T node, int index, T child) {
        modelSupport.fireChildAdded(new TreePath(getPathToRoot(node)), index, child);
    }

    public void nodeRemoved(T node, int index, T child) {
        modelSupport.fireChildRemoved(new TreePath(getPathToRoot(node)), index, child);
    }

    /**
     * Invoke this method if you've modified the TreeNodes upon which this
     * model depends.  The model will notify all of its listeners that the
     * model has changed below the node <code>node</code> (PENDING).
     *
     * @param node die Node
     */
    public void reload(T node) {
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
        reload((T) root);
    }

    /**
     * liefert das Kind Nummer index zu einer Node
     *
     * @param parent die Node
     * @param index  Kindindex von dieser Node
     * @return das i-te Kind der Node
     */
    @Override
    public final Object getChild(Object parent, int index) {
        return getAllChildren(parent).get(index);
    }


    private List getAllChildren(Object parent) {
        return (List) childrenAccessor.getVal(parent);
    }

    private T getParent(Object node) {
        return (T)  parentAccessor.getVal(node);
    }

    /**
     * liefert Anzahl Kinder einer Node
     *
     * @param parent die Node
     * @return Anzahl Kinder
     */
    @Override
    public final int getChildCount(Object parent) {
        return getAllChildren(parent).size();
    }

    /**
     * {@inheritDoc}
     * vorläufig nicht implementiert.
     *
     * @param path     {@inheritDoc}
     * @param newValue {@inheritDoc}
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // macht noch nix...
    }

    /**
     * liefert den Index eines Kindes für eine Vater-node
     *
     * @param parent der Vater
     * @param child  ein Kinde des Vaters
     * @return index des Kindes
     */
    @Override
    public final int getIndexOfChild(Object parent, Object child) {
        return getAllChildren(parent).indexOf(child);
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
    public final T[] getPathToRoot(T aNode) {
        return getPathToRoot(aNode, 0);
    }

    private T[] getPathToRoot(T aNode, int depth) {
        T[] retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /*
         * Check for null, in case someone passed in a null node, or
         *  they passed in an element that isn't rooted at root.
         */
        if (aNode == null) {
            if (depth == 0) {
                return null;
            } else {
                retNodes = (T[]) new TreeStructure[depth];
            }
        } else {
            depth++;
            if (getParent(aNode) == null) {
                retNodes = (T[]) new TreeStructure[depth];
            } else {
                retNodes = (T[]) getPathToRoot(getParent(aNode), depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }


    @Override
    public boolean isCellEditable(java.lang.Object node, int column) {
        return colDescrs[column].isEditable(node);
    }

    @Override
    public int getColumnCount() {
        return colDescrs.length;
    }

    @Override
    public String getColumnName(int column) {
        return getLocaleString(colDescrs[column].getUiName());
    }

    public java.lang.Class<?> getColumnClass(int columnIndex) {
        Class<?> cl = colDescrs[columnIndex].getPropertyAccessor().getType(clazz);
        return MappingUtils.getCorrectType(cl);
    }


    private String getLocaleString(String string) {
        return localeBundle != null ? localeBundle.getString(string) : string;
    }

    @Override
    public Object getValueAt(Object o, int i) {
        T row = (T) o;
        return colDescrs[i].getPropertyAccessor().getVal(row);
    }

    public void setValueAt(java.lang.Object value, java.lang.Object node, int columnIndex) {
        T row = (T) node;
        ColDescr col = colDescrs[columnIndex];

        Object oldVal = col.getPropertyAccessor().getVal(row);

        col.getPropertyAccessor().setVal(row, value);

        if (getChangeAction() != null) {
            getChangeAction().onChangeNode(row, oldVal, value, col);
        }
        if (col.getChangeAction() != null) {
            col.getChangeAction().onChangeNode(row, oldVal, value, col);
        }

    }

    public ChangeAction<T> getChangeAction() {
        return changeAction;
    }

    public void setChangeAction(ChangeAction<T> changeAction) {
        this.changeAction = changeAction;
    }
}
