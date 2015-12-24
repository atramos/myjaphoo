/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree.events;

import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;

/**
 *
 * @author lang
 */
public class SelectedNodeChangeEvent {

    private AbstractMovieTreeNode abstractMovieTreeNode;

    public SelectedNodeChangeEvent(AbstractMovieTreeNode abstractMovieTreeNode) {
        this.abstractMovieTreeNode = abstractMovieTreeNode;
    }

    public AbstractMovieTreeNode getAbstractMovieTreeNode() {
        return abstractMovieTreeNode;
    }
}
