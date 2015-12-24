/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import org.mlsoft.structures.Trees;
import org.myjaphoo.gui.OrderType;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.thumbtable.AbstractThumbPanelController;
import org.myjaphoo.gui.thumbtable.ThumbPopupMenus;
import org.myjaphoo.model.ThumbMode;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mla
 */
public class MainThumbController extends AbstractThumbPanelController {

    private static final Logger logger = LoggerFactory.getLogger(MainThumbController.class);

    private MyjaphooController controller;

    public MainThumbController(MyjaphooController controller) {
        this.controller = controller;
    }

    @Override
    public void assignTokenToMovieNodes(Token token, ArrayList<MovieNode> nodes) {
        controller.assignTokenToMovieNodes(token, nodes);
    }

    @Override
    public void playMovies(List<AbstractLeafNode> nodes) {
        controller.playMovies(nodes);
    }

    @Override
    public void playMovie(AbstractLeafNode node) {
        controller.playMovie(node);
    }

    @Override
    public JPopupMenu createPopUpMenu(ArrayList<AbstractLeafNode> selectedNodes) {
        return ThumbPopupMenus.createThumbPopupMenu(controller, selectedNodes);
    }

    @Override
    public ThumbMode getThumbMode() {
        return controller.getFilter().getThumbMode();
    }

    @Override
    public void setThumbMode(ThumbMode mode) {
        controller.getFilter().setThumbMode(mode);
    }

    
    
    @Override
    public void setOrder(OrderType orderType) {
        controller.getFilter().setOrder(orderType);
    }

    @Override
    public void currSelectionChanged(List<MovieNode> currSelectedMovies) {
        controller.getView().getInfoPanel().currSelectionChanged(currSelectedMovies);
        if (controller.getView().isPreviewVisible()) {
            if (currSelectedMovies.size() > 0) {
                controller.getView().getPreviewPanel().displayImage(currSelectedMovies.get(0));
            }
        }
        MovieEntry me = null;
        MovieNode node = null;
        if (currSelectedMovies.size() == 1) {
            node = currSelectedMovies.get(0);
            me = node.getMovieEntry();
        }
        controller.getView().getPropertiesPanel().currSelectionChanged(currSelectedMovies, me, node);
    }

    @Override
    public ThumbDisplayFilterResult getThumbsModel() {
        return controller.getFilter().getThumbsModel(getThumbMode().getMode(), isPreventGroupingDups());
    }

    @Override
    public void breadCrumbPathChanged(TreePath treePath) {
        Object lastNode = treePath.getLastPathComponent();
        controller.getMainMoviePanelController().setCurrentDir((AbstractMovieTreeNode) lastNode);
        
    }

    @Override
    public TreeModel getBreadCrumbTreeModel() {
        return new DefaultTreeModel(controller.getMainMoviePanelController().getRoot());
    }

    @Override
    public TreePath getBreadCrumbTreePath() {
        final Object[] pathToRoot = Trees.getPathToRoot( controller.getMainMoviePanelController().getRoot(), controller.getCurrentSelectedDir());
        if (pathToRoot == null) {
            return null;
        } else {
            return new TreePath(pathToRoot);
        }
    }
}
