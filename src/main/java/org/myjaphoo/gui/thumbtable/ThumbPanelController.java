/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import org.myjaphoo.MovieNode;
import org.myjaphoo.ThumbDisplayFilterResult;
import org.myjaphoo.gui.OrderType;
import org.myjaphoo.gui.ThumbTypeDisplayMode;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.util.Colorization;
import org.myjaphoo.model.ThumbMode;
import org.myjaphoo.model.db.Token;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;


/**
 * Controller interface for the thumb panel view.
 * @author mla
 */
public interface ThumbPanelController {

    public TreeModel getBreadCrumbTreeModel();
    
    public TreePath getBreadCrumbTreePath();

    public void breadCrumbPathChanged(TreePath treePath);

    public void setCardView(boolean b);

    public boolean isCardView();
    /**
     * @return the thumbTypeDisplayMode
     */
    public ThumbTypeDisplayMode getThumbTypeDisplayMode();

    /**
     * @param thumbTypeDisplayMode the thumbTypeDisplayMode to set
     */
    public void setThumbTypeDisplayMode(ThumbTypeDisplayMode thumbTypeDisplayMode);

    public void assignTokenToMovieNodes(Token token, ArrayList<MovieNode> nodes);

    public Zooming getZoom();

    public Colorization getColorization();

    public void playMovies(List<AbstractLeafNode> nodes);

    public void playMovie(AbstractLeafNode node);

    public JPopupMenu createPopUpMenu(ArrayList<AbstractLeafNode> selectedNodes);

    public ThumbDisplayFilterResult getThumbsModel();

    public void setOrder(OrderType orderType);

    public void currSelectionChanged(List<MovieNode> currSelectedMovies);

    public void setThumbMode(ThumbMode mode);

    public ThumbMode getThumbMode();

    public void setPreventGroupingDups(boolean selected);

    public boolean isPreventGroupingDups();

    int getHeightForThumbLabelComponent();
}
