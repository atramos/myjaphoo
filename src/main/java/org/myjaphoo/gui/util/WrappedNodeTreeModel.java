/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.tree.TreePath;
import org.mlsoft.structures.TreeStructure;
import org.mlsoft.structures.Trees;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.myjaphoo.gui.WmTreeTableModel;

/**
 * Tree table model for wrapped nodes.
 * One special about this model is, that it could be used
 * to also build a flat list of the tree structure and it could be used to filter 
 * the nodes.
 * @author lang
 */
public abstract class WrappedNodeTreeModel<T extends TreeStructure<T> & Comparable<T>> extends WmTreeTableModel {

    private static Logger logger = LoggerFactory.getLogger(WrappedNodeTreeModel.class);
    protected boolean isEditable;
   

    public WrappedNodeTreeModel(T newroot, boolean filter, String typedText, boolean flatList, String[] columns) {
        super(new WrappedNode(newroot), columns);
        setRoot(prepareModel(newroot, filter, typedText, flatList));
    } 
    
    protected WrappedNode prepareModel(T newroot, boolean filter, String typedText, boolean flatList) {
        if (newroot == null) {
            return null;
        }
        WrappedNode root = new WrappedNode(newroot);
        if (flatList) {
            prepareFlatList(root, newroot, filter, typedText, 0);
        } else {
            prepareChildren(root, newroot, filter, typedText);
        }

        return root;
    }
    private static Comparator<WrappedNode> C = new Comparator<WrappedNode>() {

        @Override
        public int compare(WrappedNode o1, WrappedNode o2) {
            return ((Comparable) o1.getRef()).compareTo(o2.getRef());
        }
    };

    private void prepareFlatList(WrappedNode root, T newroot, boolean filter, String typedText, int level) {
        for (T child : newroot.getChildren()) {
            if (!filter || match(child, typedText)) {
                if (shouldDisplayNodeInFlatMode(child, level)) {
                    WrappedNode childNode = new WrappedNode(child);
                    root.addChild(childNode);
                }
            }
            prepareFlatList(root, child, filter, typedText, level +1);
        }
        Collections.sort(root.getChildren(), C);

    }

    private void prepareChildren(WrappedNode root, T newroot, boolean filter, String typedText) {
        for (T child : newroot.getChildren()) {
            if (!filter || match(child, typedText)) {
                WrappedNode childNode = new WrappedNode(child);
                root.addChild(childNode);
                prepareChildren(childNode, child, filter, typedText);
            } else {
                prepareChildren(root, child, filter, typedText);
            }

        }
    }

    abstract protected boolean match(T tok, String typedText);

    public TreeStructure[] findPathForWrappedObject(T token) {
        WrappedNode tokNode = findWrappedNodeforWrappedObject(token);
        return getPathToRoot(tokNode);
    }

    public TreePath findTreePathForWrappedObject(T t) {
        return new TreePath(findPathForWrappedObject(t));
    }

    public WrappedNode<T> findWrappedNodeforWrappedObject(T token) {
        WrappedNode root = (WrappedNode) getRoot();
        return searchRecursively(root, token);
    }

    private WrappedNode searchRecursively(WrappedNode<T> node, final T obj) {
        return Trees.searchDepthFirstSearch(node, new Trees.SearchFunction<WrappedNode<T>>() {

            @Override
            public boolean found(WrappedNode<T> node) {
                return obj.equals(node.getRef());
            }
        });
    }

    /**
     * Updates a list of wrapped nodes with new references values.
     * It then fires a node change event for each changed node.
     * Note, that the node value objects are identified by their equals
     * method.
     */
    public void updateNodes(List<T> objectsToUpdate) {

        logger.debug("updating wrapped nodes:{}", objectsToUpdate);
        for (T obj : objectsToUpdate) {
            if (obj != null) {
                logger.debug("searching node value {} in tree...", obj);
                WrappedNode<T> node = findWrappedNodeforWrappedObject(obj);
                if (node != null) {
                    logger.debug("found node value {}, now exchanging data, and firing node change event", obj);
                    // update the node
                    node.setRef(obj);
                    // and fire update:
                    nodeChanged(node);
                }
            }
        }
    }

    /**
     * adds a new object into the tree.
     * Builds a new wrapped node for the object, and adds it under
     * its parent node element.
     * @param parent the paren object
     * @param newObj the new object to wrap and to insert into the tree
     */
    public void addedNewNodeObject(T parent, T newObj) {
        WrappedNode<T> node = findWrappedNodeforWrappedObject(parent);
        WrappedNode<T> newChild = new WrappedNode<T>(newObj);
        node.addChild(newChild);
        modelSupport.fireChildAdded(findTreePathForWrappedObject(parent), node.getChildren().indexOf(newChild), newChild);
    }

    /**
     * Removes a object from the tree.
     */
    public void removeNodeObject(T parent, T objToDelete) {
        WrappedNode<T> node = findWrappedNodeforWrappedObject(parent);
        WrappedNode<T> childToRemove = findWrappedNodeforWrappedObject(objToDelete);
        modelSupport.fireChildRemoved(findTreePathForWrappedObject(parent), node.getChildren().indexOf(childToRemove), childToRemove);
    }

    /**
     * determines if the given children should be displayed in flat mode or not.
     * This method always returns true, which means, all nodes are displayed 
     * in flat mode. 
     * Implementations can override this to show only certain nodes, e.g.
     * only leafs of a certain level, etc.
     * @param node the node to check
     * @param level the level of this node (path depth)
     * @return true, if this node should be displayed in flat mode
     */
    public boolean shouldDisplayNodeInFlatMode(T node, int level) {
            return true;
    }
}
