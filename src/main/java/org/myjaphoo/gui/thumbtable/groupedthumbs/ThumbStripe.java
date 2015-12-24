/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable.groupedthumbs;

import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author mla
 */
public class ThumbStripe {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/thumbtable/groupedthumbs/resources/ThumbStripe");
    private List<AbstractLeafNode> movieNodes;
    private MovieStructureNode structureNode;
    private List<BreakDownChild> breakddownchildren = new ArrayList<BreakDownChild>();

    public ThumbStripe(List<AbstractLeafNode> movieNodes) {
        this.movieNodes = movieNodes;
    }

    public ThumbStripe(MovieStructureNode structureNode, List<AbstractLeafNode> movieNodes) {
        this.structureNode = structureNode;
        this.movieNodes = movieNodes;
    }

    /**
     * @return the movieNodes
     */
    public List<AbstractLeafNode> getMovieNodes() {
        return movieNodes;
    }

    /**
     * @return the structureNode
     */
    public MovieStructureNode getStructureNode() {
        return structureNode;
    }

    @Override
    public String toString() {
        return "<html>" + structureNode.getName() + //NOI18N
                "<br>" + structureNode.getGroupingExpr() + //NOI18N
                "<br>" + //NOI18N
                Integer.toString(getMovieNodes().size()) + localeBundle.getString("MEDIAS")
                + "<br>" + //NOI18N
                Utils.humanReadableByteCount(sumSize(getMovieNodes())) + localeBundle.getString("SIZE")
                + "</html>"; //NOI18N
    }

    /**
     * @return the breakddownchildren
     */
    public List<BreakDownChild> getBreakddownchildren() {
        return breakddownchildren;
    }

    public void setMovieNodes(List<AbstractLeafNode> movieNodes) {
        this.movieNodes = movieNodes;
    }

    private long sumSize(List<AbstractLeafNode> movieNodes) {
        long size = 0;
        for (AbstractLeafNode node : movieNodes) {
            size += node.getFileLength();
        }
        return size;
    }

    public void buildBreakDownChildrenStructure(int colmax) {
        breakddownchildren.clear();
        int size = getMovieNodes().size();

        for (int i = colmax; i < size; i += colmax) {
            BreakDownChild child = new BreakDownChild();
            child.stripe = this;
            child.startIndex = i;
            breakddownchildren.add(child);
        }
    }
}
