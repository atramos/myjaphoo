/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui.movietree.grouping;

import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.ProcessingRequirementInformation;
import org.myjaphoo.model.grouping.GroupingExecutionContext;

import java.util.List;


/**
 * Grouping algorithm to structure the movie nodes into a tree.
 * @author mla
 */
public interface GroupAlgorithm  extends ProcessingRequirementInformation{

    /**
     * Groups a node into the tree.
     * @param root the root of the tree
     * @param node the node to add to the tree
     */
    public List<MovieStructureNode> findParents(AbstractMovieTreeNode root, JoinedDataRow row);

    public AbstractMovieTreeNode getOrCreateRoot();
    
    public AbstractMovieTreeNode findAccordingNode(AbstractMovieTreeNode root, String path);

    public void pruneEmptyDirs(AbstractMovieTreeNode root);

    public void postProcess(AbstractMovieTreeNode root);

    public void preProcess(GroupingExecutionContext context);

    /** a description of this grouper; probably the group expression representing this grouping. */
    public String getText();

    void pruneByHavingClause(MovieStructureNode root);

    void aggregate(MovieStructureNode root);
}
