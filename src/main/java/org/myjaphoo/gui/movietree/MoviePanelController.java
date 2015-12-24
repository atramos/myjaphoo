/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree;

import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.ThumbDisplayFilterResult;
import org.myjaphoo.model.StructureType;
import org.myjaphoo.model.grouping.GroupingDim;

import javax.swing.*;
import java.util.List;

/**
 * Controller interface für das MoviePanel.
 * @author mla
 */
public interface MoviePanelController {

    public void setUserDefinedStructure(List<GroupingDim> hierarchy);

    public void fireStructureChanged();

    public boolean isPruneTree();

    public void setPruneTree(boolean selected);

    public MovieStructureNode createMovieTreeModel();

    public AbstractTreeNode getCurrentSelectedDir();

    public boolean isListChildMovies();

    public void setListChildMovies(boolean selected);

    public ThumbDisplayFilterResult getThumbsModel();

    public void setStructType(StructureType structureType);

    public boolean isCondenseDuplicates();

    public void setCondenseDuplicates(boolean selected);

    public void setCurrentDir(AbstractMovieTreeNode abstractMovieTreeNode);

    public void setPreviewThumbsInMovieTree(boolean b);

    public boolean isPreviewThumbsInMovieTree();

    public void updateChronic();

    public JPopupMenu createPopUpMenu(AbstractTreeNode currentSelectedDir);

    public String getUserDefinedStruct();

    MovieStructureNode getRoot();

    public List<String> getUsedLiterals();
}
