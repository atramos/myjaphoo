/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.structures;

import java.util.Collection;
import java.util.Comparator;

/**
 *
 * @author lang
 */
public class Trees {

    /**
     * Prevent that a "circle" would be created when changing the parent of a node.
     * @param newParent
     * @param token2Move 
     */
    public static void checkCircle(TreeStructure newParent, TreeStructure token2Move) {
        // check the new parents path back: there must not be the the token to move in it:
        TreeStructure pathCheck = newParent;
        while (pathCheck != null) {
            if (pathCheck.equals(token2Move)) {
                throw new RuntimeException("You must not move a parents node under its children!");
            }
            pathCheck = pathCheck.getParent();
        }
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
    public static <T extends TreeStructure> T[] getPathToRoot(T root, T aNode) {
        return getPathToRoot(root, aNode, 0);
    }

    /**
     * Builds the parents of node up to and including the root node,
     * where the original node is the last element in the returned array.
     * The length of the returned array gives the node's depth in the
     * tree.
     *
     * @param aNode  the TreeNode to get the path for
     * @param depth  an int giving the number of steps already taken towards
     *        the root (on recursive calls), used to size the returned array
     * @return an array of TreeNodes giving the path from the root to the
     *         specified node
     */
    private static <T extends TreeStructure> T[] getPathToRoot(T root, T aNode, int depth) {
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
                retNodes = (T[])  new TreeStructure[depth];
            }
        } else {
            depth++;
            if (aNode == root) {
                retNodes = (T[]) new TreeStructure[depth];
            } else {
                retNodes = (T[]) getPathToRoot(root, aNode.getParent(), depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

    private static <T extends TreeStructure> T[] getPathToRoot(T aNode, int depth) {
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
                retNodes = (T[])  new TreeStructure[depth];
            }
        } else {
            depth++;
            if (aNode.getParent() == null) {
                retNodes = (T[]) new TreeStructure[depth];
            } else {
                retNodes = (T[]) getPathToRoot(aNode.getParent(), depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

    /** comparator to order tree structures by level, i.e. first root node,then first level children, second level children, and so on. */
    public static final Comparator<TreeStructure> TREELEVELORDER_COMPARATOR = new Comparator<TreeStructure>() {

        @Override
        public int compare(TreeStructure o1, TreeStructure o2) {
            int level1 = getLevel(o1);
            int level2 = getLevel(o2);
            return level1 - level2;
        }

        private int getLevel(TreeStructure t) {
            int i = 0;
            while (t.getParent() != null) {
                i++;
                t = t.getParent();
            }
            return i;
        }
    };

    public static interface SearchFunction<T extends TreeStructure> {

        public boolean found(T node);
    }

    public static interface EqualNodeFunction<T extends TreeStructure> {

        public boolean isEqual(T node1, T node2);
    }

    public static interface EqualPathComponent<T extends TreeStructure> {
        public boolean isEqual(T node, Object pathComponent);
    }

    /**
     * Common Depth first search method for trees.
     * 
     */
    public static <T extends TreeStructure<T>> T searchDepthFirstSearch(T node, SearchFunction f) {
        if (node == null) {
            return null;
        }
        if (f.found(node)) {
            return node;
        }
        for (T child : node.getChildren()) {
            T result = searchDepthFirstSearch(child, f);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * searches for a node by comparing each path component, starting with the root.
     * Therefore this function needs a compare function that compares the path component of a node.
     * It then returns the found node.
     * This is useful when you search for a node in a tree, that are not reference identical,
     * but e.g. equal by an attribute like a name.
     * @param root
     * @param node2Search
     * @param f
     * @param <T>
     * @return
     */
    public static <T extends TreeStructure<T>> T pathSearch(T root, T node2Search, EqualNodeFunction f) {
        T[] path = getPathToRoot(node2Search, 0);
        return pathSearch(root, path, f, 0);
    }

    public static <T extends TreeStructure<T>> T pathSearch(T currNode, T[] path, EqualNodeFunction f, int i) {
        T pathComponent = path[i];
        if (!f.isEqual(currNode, pathComponent)) {
            return null;
        }
        if (i+1 >= path.length) {
            // finished path and found node:
            return currNode;
        }
        if (i + 1<path.length && currNode.getChildren().size() > 0) {
            for (T child: currNode.getChildren()) {
                T result = pathSearch(child, path, f, i+1);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public static <T extends TreeStructure<T>> T pathSearchByObjectPath(T currNode, Object[] path, EqualPathComponent f, int i) {
        Object pathComponent = path[i];
        if (!f.isEqual(currNode, pathComponent)) {
            return null;
        }
        if (i+1 >= path.length) {
            // finished path and found node:
            return currNode;
        }
        if (i + 1<path.length && currNode.getChildren().size() > 0) {
            for (T child: currNode.getChildren()) {
                T result = pathSearchByObjectPath(child, path, f, i+1);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * Find by name from a simple list of objects. Does a plain sequential search.
     * @param collection
     * @param name
     * @param <T>
     * @return
     */
    public static <T extends TreeStructure<T>> T findByName(Collection<T> collection, String name) {
        for (T t: collection) {
            if (name.equals(t.getName())) {
                return t;
            }
        }
        return null;
    }
}
