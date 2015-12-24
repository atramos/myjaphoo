/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.myjaphoo.MovieNode;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.thumbtable.groupedthumbs.BreakDownChild;
import org.myjaphoo.gui.thumbtable.groupedthumbs.GroupedStripedThumbTableModel;
import org.myjaphoo.gui.thumbtable.groupedthumbs.GroupedThumbView;
import org.myjaphoo.gui.thumbtable.groupedthumbs.ThumbStripe;
import org.myjaphoo.gui.thumbtable.thumbcache.ThreadedThumbCache;
import org.myjaphoo.gui.util.Colorization;
import org.myjaphoo.gui.util.ColorizationType;
import org.myjaphoo.gui.util.Tables;
import org.myjaphoo.model.FileType;
import org.myjaphoo.model.cache.ChangeSet;
import org.myjaphoo.model.cache.events.MoviesRemovedEvent;
import org.myjaphoo.model.db.MovieEntry;

import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author mla
 */
public class StripeThumbTable extends JXTreeTable implements ThumbDisplayingComponent {

    public static final int FIRSTCOL_WIDTH = 200;
    private ThumbPanelController controller;
    private int currNumCols = 10;

    static class NoHighlightForColorisation implements HighlightPredicate {

        private ThumbPanelController controller;
        public NoHighlightForColorisation(ThumbPanelController controller) {
            this.controller = controller;
        }
        @Override
        public boolean isHighlighted(Component component, ComponentAdapter componentAdapter) {
            return controller.getColorization().getType() == ColorizationType.NONE;
        }
    };

    public StripeThumbTable(ThumbPanelController controller) {
        super();
        this.controller = controller;

        HighlightPredicate groupPredicate = new HighlightPredicate.AndHighlightPredicate(HighlightPredicate.IS_FOLDER, new NoHighlightForColorisation(controller));
        addHighlighter(new ColorHighlighter(groupPredicate, Colorization.noColor.darker(), Color.black));

        HighlightPredicate stripePredicate = new HighlightPredicate.AndHighlightPredicate(HighlightPredicate.IS_LEAF, new NoHighlightForColorisation(controller));
        addHighlighter(new ColorHighlighter(stripePredicate, Colorization.noColor, Color.black));
        addHighlighter(Commons.ROLLOVER_CELL_HIGHLIGHTER);

        setHorizontalScrollEnabled(true);
        this.setDoubleBuffered(true);

    }

    public void refreshModelData(GroupedThumbView group, int scrollPaneWidth) {

        int effWidth = controller.getZoom().getEffectiveWidth() + 5;
        int effHeight = controller.getHeightForThumbLabelComponent();
        if (controller.isCardView()) {
            effWidth = effWidth * 5 / 2;
        }
        currNumCols = (scrollPaneWidth - StripeThumbTable.FIRSTCOL_WIDTH) / effWidth;

        final GroupedStripedThumbTableModel model = new GroupedStripedThumbTableModel(group, currNumCols);
        setTreeTableModel(model);
        ThreadedThumbCache.getInstance().predict(predictedEntries(group), false);

        setDefaultRenderer(MovieNode.class, new ThumbTableRenderer(controller, false, true));
        setRowHeight(effHeight);
        for (int c = 0; c < getModel().getColumnCount(); c++) {
            if (c == 0) {
                getColumnExt(c).setPreferredWidth(FIRSTCOL_WIDTH);
            } else {
                getColumnExt(c).setWidth(effWidth);
                getColumnExt(c).setPreferredWidth(effWidth);
            }
        }
        setShowGrid(true, false);
    }

    @Override
    public ArrayList<AbstractLeafNode> getAllSelectedNodes() {
        int[] selRows = Tables.getModelSelectedRows(this);
        int[] selCols = Tables.getModelSelectedCols(this);
        ArrayList<AbstractLeafNode> nodes = new ArrayList<AbstractLeafNode>();
        for (int selRow : selRows) {
            for (int selCol : selCols) {
                final Object value = getModel().getValueAt(selRow, selCol);
                // test, ob es wirklich ein blatt ist, und nicht ein stripe objekt:
                if (value instanceof AbstractLeafNode) {
                    nodes.add((AbstractLeafNode) value);
                }
            }
        }
        return nodes;
    }

    public ArrayList<AbstractLeafNode> getClickedStripeNodes() {
        int selrow = Tables.getModelSelectedRow(this);
        int selcol = Tables.getModelSelectedCol(this);
        AbstractLeafNode node = (AbstractLeafNode) this.getModel().getValueAt(selrow, selcol);
        if (node.is(FileType.Pictures)) {
            final Object strip = this.getModel().getValueAt(selrow, 0);
            // ein bild ist doppelt geklickt worden -> liste aller bilder im streifen holen; liste
            // rotieren, dass ds bild am anfang steht, und zurückliefern, damit alle bilder des streifens durch-
            // laufen werden können:
            ArrayList<AbstractLeafNode> list = getMovieNodesOfStripe(selrow);
            Collections.rotate(list, -list.indexOf(node));
            return list;
        } else {
            ArrayList<AbstractLeafNode> list = new ArrayList<AbstractLeafNode>();
            list.add(node);
            return list;
        }

    }

    private ArrayList<AbstractLeafNode> getMovieNodesOfStripe(int row) {
        ArrayList<AbstractLeafNode> list = new ArrayList<AbstractLeafNode>();
        final Object strip = this.getModel().getValueAt(row, 0);
        // ein bild ist doppelt geklickt worden -> liste aller bilder im streifen holen; liste
        // rotieren, dass ds bild am anfang steht, und zurückliefern, damit alle bilder des streifens durch-
        // laufen werden können:
        if (strip instanceof ThumbStripe) {
            ThumbStripe stripe = (ThumbStripe) strip;
            list.addAll(stripe.getMovieNodes());
        } else if (strip instanceof BreakDownChild) {
            BreakDownChild child = (BreakDownChild) strip;
            list.addAll(child.stripe.getMovieNodes());
        }
        return list;
    }

    /**
     * Liefert die entries, deren thumb wohl zuerst angezeigt werden wird (muss)
     */
    private Collection<MovieEntry> predictedEntries(GroupedThumbView group) {
        ArrayList<MovieEntry> result = new ArrayList<MovieEntry>();
        ArrayList<MovieEntry> last = new ArrayList<MovieEntry>();
        for (ThumbStripe stripe : group.getStripes()) {
            int stripeindex = 0;
            for (AbstractLeafNode node : stripe.getMovieNodes()) {
                // die ersten 10 elemente jedes stripes an den anfang setzen,
                // den rest ans ende, da zuerst jeder stripe zusammengeklappt ist
                // (und die breakdowns nicht sichtbar sind)

                if (node instanceof MovieNode) {
                    MovieEntry entry = ((MovieNode) node).getMovieEntry();
                    if (entry != null) {
                        if (stripeindex < group.getColmax()) {
                            result.add(entry);
                        } else {
                            last.add(entry);
                        }
                        stripeindex++;
                    }
                }
            }
        }
        result.addAll(last);
        return result;
    }

    void updateNodes(ChangeSet e) {
        TreeTableModel model = getTreeTableModel();
        if (model instanceof GroupedStripedThumbTableModel) {
            GroupedStripedThumbTableModel gttmodel = (GroupedStripedThumbTableModel) model;
            gttmodel.updateNodes(e);
        }
    }

    /**
     * prüfen, ob die erste info-spalte geklickt wurde.
     * @return
     */
    boolean isfirstColClicked() {

        int[] selCols = Tables.getModelSelectedCols(StripeThumbTable.this);
        return selCols.length == 1 && selCols[0] == 0;
    }

    /**
     * Doppelklick auf erste info-spalte: hier selektieren wir alle
     * thumbs dieses Streifens.
     */
    void handleFirstColClickedEvent() {
        TreePath path = getTreeSelectionModel().getSelectionPath();
        ThumbStripe stripe = (ThumbStripe) path.getLastPathComponent();
        /*
         * wir müssen den path expanden, ansonsten könnten wir nicht
         * untergeordnete breakdownrows mit selektieren.
         * die selektionsmöglichkeiten generell sind beim JXTabletree
         * wohl nicht sehr flexibel.
         */
        StripeThumbTable.this.expandPath(path);
        int numOfRows = stripe.getBreakddownchildren().size();
        StripeThumbTable.this.setHorizontalScrollEnabled(false);
        int row = StripeThumbTable.this.getRowForPath(path);
        // meta column 0 ist selektiert worden
        // event-> um alle columns dieser row bzw. dieses stripes (alle dazugehörigen breakdown rows auch) zu selektieren:
        for (int i = 1; i <= currNumCols; i++) {
            for (int rowiter = row; rowiter < row + numOfRows + 1; rowiter++) {
                StripeThumbTable.this.changeSelection(rowiter, i, false, true);
            }
        }
        StripeThumbTable.this.setHorizontalScrollEnabled(true);
    }

    void updateRemovedNodes(MoviesRemovedEvent mre) {
        TreeTableModel model = getTreeTableModel();
        if (model instanceof GroupedStripedThumbTableModel) {
            GroupedStripedThumbTableModel gttmodel = (GroupedStripedThumbTableModel) model;
            gttmodel.updateRemovedNodes(mre);
        }
    }
}
