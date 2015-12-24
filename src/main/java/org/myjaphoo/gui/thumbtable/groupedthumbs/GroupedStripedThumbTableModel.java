/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable.groupedthumbs;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.myjaphoo.MovieNode;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.model.WmEntitySet;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.cache.ChangeSet;
import org.myjaphoo.model.cache.events.MoviesRemovedEvent;
import org.myjaphoo.model.db.MovieEntry;

import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author lang
 */
public class GroupedStripedThumbTableModel extends AbstractTreeTableModel {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/thumbtable/groupedthumbs/resources/GroupedStripedThumbTableModel");
    private int colmax = 10;
    private GroupedThumbView groupview;
    /**
     * Struktur: liste mit zeil
     */
    //private ArrayList<ArrayList<BreakDownChild>> structure = new ArrayList<ArrayList<BreakDownChild>>();
    private int columncount;

    public GroupedStripedThumbTableModel(GroupedThumbView groupview, int colmax) {
        super(groupview);
        this.groupview = groupview;
        this.colmax = colmax;
        this.columncount = colmax + 1;
        // prepare breakdowns of the stripes
        groupview.prepareBreakDown(colmax);
    }

    @Override
    public int getColumnCount() {
        return columncount;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        } else {
            return MovieNode.class;
        }
    }

    @Override
    public Object getValueAt(Object node, int column) {
        if (node instanceof GroupedThumbView) {
            return null;
        } else if (node instanceof ThumbStripe) {
            if (column == 0) {
                return node;
            }
            column--;
            final List<AbstractLeafNode> movieNodes = ((ThumbStripe) node).getMovieNodes();
            if (column < movieNodes.size()) {
                return movieNodes.get(column);
            } else {
                return null;
            }
        } else if (node instanceof BreakDownChild) {
            if (column == 0) {
                return node;
            }
            column--;
            BreakDownChild child = (BreakDownChild) node;
            return child.getCol(column);
        }
        throw new RuntimeException();
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof GroupedThumbView) {
            return groupview.getStripes().get(index);
        } else if (parent instanceof ThumbStripe) {
            return ((ThumbStripe) parent).getBreakddownchildren().get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof GroupedThumbView) {
            return groupview.getStripes().size();
        } else if (parent instanceof ThumbStripe) {
            return ((ThumbStripe) parent).getBreakddownchildren().size();
        } else {
            return 0;
        }
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof GroupedThumbView) {
            return groupview.getStripes().indexOf(child);
        }
        if (parent instanceof ThumbStripe) {
            return ((ThumbStripe) parent).getBreakddownchildren().indexOf(child);
        } else {
            return 0;
        }
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            String dim = findStripingGroupingExpr();
            if (dim != null) {
                return "<html>" + dim + "</html>"; //NOI18N
            } else {
                return localeBundle.getString("STRIP");
            }
        } else {
            return Integer.toString(column);
        }
    }

    private String findStripingGroupingExpr() {
        // find nextbest grouping dim of this grouping view. in general it should be only one grouping dim.
        // could be only leafs, thats the only exception:
        for (ThumbStripe stripe : groupview.getStripes()) {
            if (stripe.getStructureNode().getGroupingExpr() != null) {
                return stripe.getStructureNode().getGroupingExpr();
            }
        }
        // only leafs...
        return null;
    }

    public void updateNodes(ChangeSet e) {
        WmEntitySet currentModel = CacheManager.getCacheActor().getImmutableModel();
        for (ThumbStripe stripe : groupview.getStripes()) {
            for (AbstractLeafNode node : stripe.getMovieNodes()) {
                MovieNode mnode = (MovieNode) node;
                MovieEntry changedEntry = currentModel.getMovieEntrySet().find(mnode.getMovieEntry());
                if (changedEntry != null) {
                    mnode.updateNode(changedEntry);
                }
            }
        }
    }

    public void updateRemovedNodes(MoviesRemovedEvent mre) {
        boolean somethingremoved = false;
        WmEntitySet currentModel = CacheManager.getCacheActor().getImmutableModel();
        for (ThumbStripe stripe : groupview.getStripes()) {
            Iterator<AbstractLeafNode> iter = stripe.getMovieNodes().iterator();
            while (iter.hasNext()) {
                AbstractLeafNode node = iter.next();
                MovieNode mnode = (MovieNode) node;
                MovieEntry changedEntry = currentModel.getMovieEntrySet().find(mnode.getMovieEntry());
                if (changedEntry != null) {
                    iter.remove();
                    somethingremoved = true;
                }
            }
        }
        if (somethingremoved) {
            // TODO post finer events to tree. problem is to translate the changed
            // nodes into treepaths of the stripe tree. we do not have
            // a function for doing so yet.
            modelSupport.fireNewRoot();
        }
    }
}
