/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import org.mlsoft.eventbus.GlobalBus;
import org.mlsoft.eventbus.Subscribe;
import org.mlsoft.structures.AbstractTreeNode;
import org.mlsoft.structures.TreeStructure;
import org.mlsoft.structures.Trees;
import org.myjaphoo.gui.WmTreeTableModel;
import org.myjaphoo.gui.editor.rsta.CachedHints;
import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.util.Utils;
import org.myjaphoo.model.cache.events.MoviesRemovedEvent;
import org.myjaphoo.model.db.MovieEntry;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * @author lang
 */
public class MovieTreeModel extends WmTreeTableModel {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/resources/MovieTreeModel");
    public static final String[] COLUMNS = new String[]{
            localeBundle.getString("GROUPING CATEGORY"),
            localeBundle.getString("TITLE"),
            localeBundle.getString("NUM OF MEDIA"),
            localeBundle.getString("SIZE OF MEDIA"),
            localeBundle.getString("DIMENSION")};

    /**
     * Constructor.
     * @param newroot Wurzel
     */
    public MovieTreeModel(MovieStructureNode newroot) {
        super(newroot, prepareColumnList(newroot));
        GlobalBus.bus.register(this);
    }

    private static String[] prepareColumnList(MovieStructureNode newroot) {
        ArrayList<String> colList = new ArrayList<>();
        colList.addAll(Arrays.asList(COLUMNS));

        colList.addAll(newroot.getAllAggregatedKeys());
        // add all the attributes:
        colList.addAll(CachedHints.getEntryAttrKeys());
        return colList.toArray(new String[colList.size()]);
    }

    @Override
    public Object getValueAt(Object node, int column) {
        switch (column) {
            case 0:
                return ((AbstractMovieTreeNode) node).getName();
            case 1:
                return ((AbstractMovieTreeNode) node).getTitle();
            case 2:
                return ((AbstractMovieTreeNode) node).getNumOfContainingMovies();
            case 3:
                return Utils.humanReadableByteCount(((AbstractMovieTreeNode) node).getSizeOfContainingMovies());
            case 4:
                return getDimInfo((AbstractMovieTreeNode) node);
            default:
                String attrName = getColumnName(column);
                if (node instanceof MovieNode) {
                    return ((MovieNode) node).getMovieEntry().getAttributes().get(attrName);
                } else if (node instanceof MovieStructureNode) {
                    return ((MovieStructureNode) node).getAggregatedValue(attrName);
                } else return null;
        }
    }

    private Object getDimInfo(AbstractMovieTreeNode node) {
        if (node instanceof MovieStructureNode) {
            return ((MovieStructureNode) node).getGroupingExpr();
        }
        return null;
    }


    /**
     * setzt eine neue Wurzel
     * @param newRoot neue Wurzel
     */
    public void setRoot(AbstractTreeNode newRoot) {
        root = newRoot;
        setColumns(prepareColumnList((MovieStructureNode) newRoot));
        modelSupport.fireNewRoot();
    }


    @Subscribe(onETD = true)
    public void moviesHaveBeenRemovedEvent(MoviesRemovedEvent mre) {
        for (MovieEntry entry : mre.getMovieEntrySet().asList()) {
            updateRemoveEvent(entry);
        }
    }

    private void updateRemoveEvent(final MovieEntry entry) {
        AbstractTreeNode foundNode = (AbstractTreeNode) Trees.searchDepthFirstSearch((TreeStructure) getRoot(), new Trees.SearchFunction<TreeStructure>() {

            @Override
            public boolean found(TreeStructure node) {
                return node instanceof MovieNode && entry.equals(((MovieNode) node).getMovieEntry());
            }
        });
        if (foundNode != null) {
            TreePath path = new TreePath(getPathToRoot(foundNode));
            modelSupport.fireChildRemoved(path, foundNode.getParent().getChildren().indexOf(foundNode), foundNode);
        }
    }
}
