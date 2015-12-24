/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.sellists;

import org.myjaphoo.MovieNode;
import org.myjaphoo.ThumbDisplayFilterResult;
import org.myjaphoo.gui.OrderType;
import org.myjaphoo.gui.PlayerHandler;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.thumbtable.AbstractThumbPanelController;
import org.myjaphoo.gui.thumbtable.groupedthumbs.GroupedThumbView;
import org.myjaphoo.gui.thumbtable.groupedthumbs.ThumbStripe;
import org.myjaphoo.model.ThumbMode;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.player.VLCPlayer;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * thumbnail list controller to display a set of entries.
 * This could be either a list of movie entries that we got for some action or
 * a list of entry nodes which we got for some action.
 *
 * @author lang
 */
class SelectionDisplayThumbController extends AbstractThumbPanelController {

    private List<AbstractLeafNode> nodes;
    private GroupedThumbView groupedView;

    public SelectionDisplayThumbController(List<MovieEntry> entryList) {
        nodes = new ArrayList<AbstractLeafNode>(entryList.size());
        int i = 0;
        for (MovieEntry entry : entryList) {
            nodes.add(new MovieNode(entry, true, null));
            i++;
        }
        init();
    }

    public SelectionDisplayThumbController(Collection<MovieNode> entryList) {
        nodes = new ArrayList<AbstractLeafNode>(entryList);
        init();
    }

    private void init() {
        MovieStructureNode structNode = new MovieStructureNode("Selektion"); //NOI18N
        List<ThumbStripe> stripes = new ArrayList<ThumbStripe>();
        ThumbStripe stripe = new ThumbStripe(structNode, nodes);
        stripes.add(stripe);
        groupedView = new GroupedThumbView(stripes);
    }

    @Override
    public void assignTokenToMovieNodes(Token token, ArrayList<MovieNode> nodes) {
        throw new UnsupportedOperationException("Not supported yet."); //NOI18N
    }

    @Override
    public void playMovies(List<AbstractLeafNode> nodes2Play) {
        PlayerHandler.playMovies(nodes2Play, new VLCPlayer(), nodes, null);
    }

    @Override
    public void playMovie(AbstractLeafNode node) {
        playMovies(Arrays.asList(node));
    }

    @Override
    public JPopupMenu createPopUpMenu(ArrayList<AbstractLeafNode> selectedNodes) {
        return null;
    }

    @Override
    public void setOrder(OrderType orderType) {
        throw new UnsupportedOperationException("Not supported yet."); //NOI18N
    }

    @Override
    public void currSelectionChanged(List<MovieNode> currSelectedMovies) {
    }

    @Override
    public ThumbDisplayFilterResult getThumbsModel() {
        if (getThumbMode() == ThumbMode.STRIPES) {
            return new ThumbDisplayFilterResult(groupedView);
        } else {
            return new ThumbDisplayFilterResult(nodes);
        }
    }

    @Override
    public void breadCrumbPathChanged(TreePath treePath) {
        // do nothing
    }

    @Override
    public TreeModel getBreadCrumbTreeModel() {
        return null;
    }

    @Override
    public TreePath getBreadCrumbTreePath() {
        return null;
    }
}
