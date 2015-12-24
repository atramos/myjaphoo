/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.groupingtests;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.AssertionFailedError;
import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;

/**
 * Checker für AbstractMovieTreenodes.
 * @author mla
 */
public class StructureChecker {

    private MovieStructureNode root;

    public StructureChecker(MovieStructureNode root) {
        this.root = root;
    }

    public AbstractTreeNode getNode(AbstractTreeNode root, String... path) {
        List<String> pathlist = new ArrayList<String>(Arrays.asList(path));
        return getNode(root, pathlist);
    }

    private AbstractTreeNode getNode(AbstractTreeNode root, List<String> pathlist) {
        AbstractTreeNode child = searchChild(root, pathlist.get(0));
        if (child == null) {
            return null;
        }
        pathlist.remove(0);
        if (pathlist.size() > 0) {
            return getNode(child, pathlist);
        } else {
            return child;
        }
    }

    private AbstractMovieTreeNode searchChild(AbstractTreeNode node, String name) {
        for (Object child : node.getChildren()) {
            AbstractMovieTreeNode chn = (AbstractMovieTreeNode) child;
            if (chn.getName().equals(name)) {
                return chn;
            }
        }
        return null;
    }

    void assertPathExists(AbstractTreeNode root, String ... path) {
        AbstractTreeNode child = getNode(root, path);
        if (child == null) {
            throw new AssertionFailedError("expected path not found!" + path.toString());
        }
    }

    int getNumOfLeafs() {
        int count = countLeafs(root);
        return count;
    }

    private int countLeafs(AbstractTreeNode node) {
        if (node.getChildCount() == 0) {
            return 1; // this is a leaf;
        } else {
            int count = 0;
            for (Object child : node.getChildren()) {
                count+= countLeafs((AbstractTreeNode)child);
            }
            return count;
        }
    }
}
