/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import org.myjaphoo.MovieNode;
import java.util.List;
import javax.swing.AbstractListModel;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.cache.ChangeSet;
import org.myjaphoo.model.WmEntitySet;
import org.myjaphoo.model.cache.events.MoviesRemovedEvent;
import org.myjaphoo.model.db.MovieEntry;

/**
 *
 * @author mla
 */
class ThumbListModel extends AbstractListModel {

    private List<AbstractLeafNode> nodes;

    public ThumbListModel(List<AbstractLeafNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public int getSize() {
        return nodes.size();
    }

    @Override
    public Object getElementAt(int index) {
        return nodes.get(index);
    }

    void fireCellUpdated(int index) {
        fireContentsChanged(this, index, index);
    }

    void updateNodes(ChangeSet e) {
        WmEntitySet currentModel = CacheManager.getCacheActor().getImmutableModel();
        for (AbstractLeafNode node1 : nodes) {
            MovieNode node = ((MovieNode) node1);
            MovieEntry entry = node.getMovieEntry();
            MovieEntry changedEntry = currentModel.getMovieEntrySet().find(entry);
            if (changedEntry != null) {
                node.updateNode(changedEntry);
            }
        }
    }

    void updateRemovedNodes(MoviesRemovedEvent mre) {
        WmEntitySet currentModel = CacheManager.getCacheActor().getImmutableModel();
        for (int i = nodes.size() -1; i >=0; i--) {
            MovieNode node = ((MovieNode) nodes.get(i));
            MovieEntry entry = node.getMovieEntry();
            MovieEntry changedEntry = currentModel.getMovieEntrySet().find(entry);
            if (changedEntry != null) {
                // remove from model:
                nodes.remove(i);
                fireIntervalRemoved(this, i, i);
            }
        }
    }
}
