/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree;

import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.gui.thumbtable.ThumbPopupMenus;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author mla
 */
public class MovieTreePopupCreator {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/movietree/resources/MovieTreePopupCreator");

    public static JPopupMenu createPopup(MyjaphooController controller, AbstractTreeNode currentSelectedDir) {
        ViewContext context = new ViewContext(flatAllNodesInTree(currentSelectedDir));
        return ThumbPopupMenus.createThumbPopupMenu(controller, context);
    }

    private static List<AbstractLeafNode> flatAllNodesInTree(AbstractTreeNode currentSelectedDir) {
        ArrayList<AbstractLeafNode> result = new ArrayList<AbstractLeafNode>();
        if (currentSelectedDir != null) {
            flatLeafs(currentSelectedDir, result);
        }
        return result;
    }

    private static void flatLeafs(AbstractTreeNode<? extends AbstractTreeNode> root, ArrayList<AbstractLeafNode> result) {
        if (root.isLeaf()) {
            result.add((AbstractLeafNode) root);
        } else {
            for (AbstractTreeNode child : root.getChildren()) {
                flatLeafs(child, result);
            }
        }
    }
}
