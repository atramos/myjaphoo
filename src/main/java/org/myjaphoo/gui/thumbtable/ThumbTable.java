/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import org.jdesktop.swingx.JXList;
import org.myjaphoo.MovieNode;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.thumbtable.thumbcache.ThreadedThumbCache;
import org.myjaphoo.gui.token.TokenTransferHandler;
import org.myjaphoo.model.cache.ChangeSet;
import org.myjaphoo.model.cache.events.MoviesRemovedEvent;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.util.Filtering;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * the thumb nail list view.
 * 
 * @author lang
 */
public class ThumbTable extends JXList implements ThumbDisplayingComponent {

    /** flavor for movie drag. */
    public static final DataFlavor SELMOVIESFLAVOR = new DataFlavor(MovieNode.class,
            "Wankman movie(s)"); //NOI18N
    private ThumbPanelController controller;
    private ThumbListCellRenderer thumbListCellRenderer;

    public ThumbTable(ThumbPanelController controller) {
        super();
        this.controller = controller;
        thumbListCellRenderer = new ThumbListCellRenderer(controller);
        setDragEnabled(true);
        setDropMode(DropMode.ON);
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
        setVisibleRowCount(0);
        getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        setTransferHandler(new TransferHandler() {

            @Override
            protected Transferable createTransferable(JComponent c) {
                return new TransferableImpl();
            }

            @Override
            public int getSourceActions(JComponent c) {
                return MOVE;
            }

            public boolean canImport(TransferHandler.TransferSupport info) {
                if (!info.isDrop()) {
                    return false;
                }

                info.setShowDropLocation(true);

                // we only import Strings
                if (!info.isDataFlavorSupported(TokenTransferHandler.TOKENFLAVOR) && !info.isDataFlavorSupported(ThumbTable.SELMOVIESFLAVOR)) {
                    return false;
                }

                // fetch the drop location
                JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
                if (dl.getIndex() >= 0) {
                    return true;
                } else {
                    return false;
                }
            }

            public boolean importData(TransferHandler.TransferSupport info) {
                // if we can't handle the import, say so
                if (!canImport(info)) {
                    return false;
                }

                JList.DropLocation dl = (JList.DropLocation) info.getDropLocation();
                MovieNode node = (MovieNode) getModel().getElementAt(dl.getIndex());
                final Token token2Move;
                try {
                    token2Move = (Token) info.getTransferable().getTransferData(TokenTransferHandler.TOKENFLAVOR);
                } catch (UnsupportedFlavorException e) {
                    return false;
                } catch (IOException e) {
                    return false;
                }
                doAssignToken(dl.getIndex(), token2Move, node);
                return true;
            }

            class TransferableImpl implements Transferable {

                private ArrayList<MovieNode> nodes = null;

                public TransferableImpl() {
                    nodes = getAllSelectedMovieNodesFromAltThumbView();
                }

                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{SELMOVIESFLAVOR};
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return flavor.equals(SELMOVIESFLAVOR);
                }

                @Override
                public Object getTransferData(DataFlavor flavor) {
                    return nodes;
                }
            }
        });
        setRolloverEnabled(true);
        addHighlighter(Commons.ROLLOVER_CELL_HIGHLIGHTER);
    }

    @Override
    public ArrayList<AbstractLeafNode> getAllSelectedNodes() {
        ArrayList<AbstractLeafNode> nodes = new ArrayList<AbstractLeafNode>(getModel().getSize());
        for (int i = 0; i < getModel().getSize(); i++) {
            if (getSelectionModel().isSelectedIndex(i)) {
                nodes.add((AbstractLeafNode) getModel().getElementAt(i));
            }
        }
        return nodes;
    }

    public ArrayList<MovieNode> getAllSelectedMovieNodesFromAltThumbView() {
        return Filtering.filterMovieNodes(getAllSelectedNodes());
    }

    private void doAssignToken(int index, Token token, MovieNode node) {
        ArrayList<MovieNode> nodes = new ArrayList<MovieNode>();
        nodes.add(node);
        controller.assignTokenToMovieNodes(token, nodes);
        ((ThumbListModel) getModel()).fireCellUpdated(index);
    }

    public void refreshThumbView(List<AbstractLeafNode> currentMovieNodes) {

        setModel(new ThumbListModel(currentMovieNodes));

        int effHeight = controller.getHeightForThumbLabelComponent();
        int effWidth = controller.getZoom().getEffectiveWidth() + 5;
        if (controller.isCardView()) {
            effWidth = effWidth * 5 / 2;
        }
        setCellRenderer(thumbListCellRenderer);

        setFixedCellWidth(effWidth);
        setFixedCellHeight(effHeight);

        ThreadedThumbCache.getInstance().predict(Filtering.nodes2Entries(Filtering.filterMovieNodes(currentMovieNodes)), false);
    }

    void updateNodes(ChangeSet e) {
        if (getModel() != null && getModel() instanceof ThumbListModel) {
            ThumbListModel oldmodel = (ThumbListModel) getModel();
            oldmodel.updateNodes(e);
        }
    }

    void updateRemovedNodes(MoviesRemovedEvent mre) {
        if (getModel() != null && getModel() instanceof ThumbListModel) {
            ThumbListModel oldmodel = (ThumbListModel) getModel();
            oldmodel.updateRemovedNodes(mre);
        }
    }
}
