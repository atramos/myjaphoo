/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo;

import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.movietree.MoviePanelController;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.movietree.MovieTreePopupCreator;
import org.myjaphoo.gui.movietree.events.*;
import org.myjaphoo.model.StructureType;
import org.myjaphoo.model.grouping.GroupingDim;

import javax.swing.*;
import java.util.List;

/**
 *
 * @author mla
 */
public class MainMoviePanelController implements MoviePanelController {

    private MyjaphooController controller;

    public MainMoviePanelController(MyjaphooController controller) {
        this.controller = controller;
    }

    @Override
    public void setUserDefinedStructure(List<GroupingDim> hierarchy) {
        controller.getFilter().setUserDefinedStructure(hierarchy);
        controller.getEventBus().post(new UserDefinedStructureChangedEvent(controller.getFilter().getUserDefinedStruct()));
    }

    @Override
    public void fireStructureChanged() {
        controller.getView().updateMovieAndThumbViews();
    }

    @Override
    public boolean isPruneTree() {
        return controller.getFilter().isPruneTree();
    }

    @Override
    public void setPruneTree(boolean selected) {
        controller.getFilter().setPruneTree(selected);
        controller.getEventBus().post(new PruneTreeChangeEvent());
    }

    @Override
    public MovieStructureNode createMovieTreeModel() {
        return controller.getFilter().createMovieTreeModel();
    }

    @Override
    public MovieStructureNode getRoot() {
        return controller.getFilter().getLastCreatedMovieTreeModel();
    }

    @Override
    public List<String> getUsedLiterals() {
        return controller.getFilter().getUsedLiterals();
    }

    @Override
    public AbstractTreeNode getCurrentSelectedDir() {
        return controller.getCurrentSelectedDir();
    }

    @Override
    public boolean isListChildMovies() {
        return controller.getFilter().isListChildMovies();
    }

    @Override
    public void setListChildMovies(boolean selected) {
        controller.getFilter().setListChildMovies(selected);
        controller.getEventBus().post(new ListChildMoviesChangeEvent());
    }

    @Override
    public void setStructType(StructureType structureType) {
        controller.getFilter().setStructType(structureType);
        controller.getEventBus().post(new StructTypeChangedEvent(controller.getFilter().getUserDefinedStruct()));
    }

    @Override
    public boolean isCondenseDuplicates() {
        return controller.getFilter().isCondenseDuplicates();
    }

    @Override
    public void setCondenseDuplicates(boolean selected) {
        controller.getFilter().setCondenseDuplicates(selected);
        controller.getEventBus().post(new CondenseDuplicatesChangeEvent());
    }

    @Override
    public void setCurrentDir(AbstractMovieTreeNode abstractMovieTreeNode) {
        controller.getFilter().setCurrentDir(abstractMovieTreeNode);
        controller.getEventBus().post(new SelectedNodeChangeEvent(abstractMovieTreeNode));
    }

    @Override
    public void setPreviewThumbsInMovieTree(boolean b) {
        controller.setPreviewThumbsInMovieTree(b);
        controller.getEventBus().post(new PreviewThumbsInTreeChangeEvent());
    }

    @Override
    public boolean isPreviewThumbsInMovieTree() {
        return controller.isPreviewThumbsInMovieTree();
    }

    @Override
    public void updateChronic() {
        controller.getView().updateChronic();
    }

    @Override
    public ThumbDisplayFilterResult getThumbsModel() {
        return controller.getMainThumbController().getThumbsModel();
    }

    @Override
    public JPopupMenu createPopUpMenu(AbstractTreeNode currentSelectedDir) {
        return MovieTreePopupCreator.createPopup(controller, currentSelectedDir);
    }

    @Override
    public String getUserDefinedStruct() {
        return controller.getFilter().getUserDefinedStruct();
    }

}
