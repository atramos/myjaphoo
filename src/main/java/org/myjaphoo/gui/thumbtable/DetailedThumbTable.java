/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.myjaphoo.MovieNode;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.thumbtable.thumbcache.ThreadedThumbCache;
import org.myjaphoo.gui.util.Tables;
import org.myjaphoo.model.cache.ChangeSet;
import org.myjaphoo.model.cache.events.MoviesRemovedEvent;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.util.Filtering;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.util.*;

/**
 *
 * @author mla
 */
public class DetailedThumbTable extends JXTable implements ThumbDisplayingComponent {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/thumbtable/resources/DetailedThumbTable");

    private ThumbPanelController controller;
    private ThumbTableModel thumbTableModel = null;

    public DetailedThumbTable(ThumbPanelController controller) {
        super();
        this.controller = controller;
        setDragEnabled(true);


        setTransferHandler(new TransferHandler() {

            @Override
            protected Transferable createTransferable(JComponent c) {
                return new TransferableImpl();
            }

            @Override
            public int getSourceActions(JComponent c) {
                return MOVE;
            }

            @Override
            public boolean canImport(TransferHandler.TransferSupport info) {
                return false;
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport info) {
                return false;
            }

            class TransferableImpl implements Transferable {

                private ArrayList<MovieNode> nodes = null;

                public TransferableImpl() {
                    nodes = Filtering.filterMovieNodes(getAllSelectedNodes());
                }

                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{ThumbTable.SELMOVIESFLAVOR};
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return flavor.equals(ThumbTable.SELMOVIESFLAVOR);
                }

                @Override
                public Object getTransferData(DataFlavor flavor) {
                    return nodes;
                }
            }
        });

        setColumnControlVisible(true);
        //setHighlighters(HighlighterFactory.createSimpleStriping());
        addHighlighter(Commons.ROLLOVER_ROW_HIGHLIGHTER);
        setHorizontalScrollEnabled(true);
        getActionMap().put("column.showThumbColumns",  //NOI18N
            new AbstractActionExt(localeBundle.getString("THUMB COLUMNS ON/OFF")) {

            boolean active = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                active = !active;
                getColumnExt(ThumbTableModel.COLNAMES[0]).setVisible(active);
                getColumnExt(ThumbTableModel.COLNAMES[1]).setVisible(active);
                getColumnExt(ThumbTableModel.COLNAMES[2]).setVisible(active);
                getColumnExt(ThumbTableModel.COLNAMES[3]).setVisible(active);
                getColumnExt(ThumbTableModel.COLNAMES[4]).setVisible(active);
                setStateAction(active);
            }
        });

        getActionMap().put("column.showOnlyFirstThumbCol",  //NOI18N
            new AbstractActionExt(localeBundle.getString("ONLY FIRST THUMB")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                getColumnExt(ThumbTableModel.COLNAMES[0]).setVisible(true);
                getColumnExt(ThumbTableModel.COLNAMES[1]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[2]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[3]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[4]).setVisible(false);
            }
        });

        getActionMap().put("column.showMinimal",  //NOI18N
            new AbstractActionExt(localeBundle.getString("ONLY MOST IMPORTANT")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                getColumnExt(ThumbTableModel.COLNAMES[0]).setVisible(true);
                getColumnExt(ThumbTableModel.COLNAMES[1]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[2]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[3]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[4]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[5]).setVisible(true);
                getColumnExt(ThumbTableModel.COLNAMES[6]).setVisible(true);
                getColumnExt(ThumbTableModel.COLNAMES[7]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[8]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[9]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[10]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[11]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[12]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[13]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[14]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[15]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[16]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[17]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[18]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[19]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[20]).setVisible(false);
                getColumnExt(ThumbTableModel.COLNAMES[21]).setVisible(false);
            }
        });

        getActionMap().put("column.showAll",  //NOI18N
                new AbstractActionExt(localeBundle.getString("ALL")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (String colName : ThumbTableModel.COLNAMES) {
                    getColumnExt(colName).setVisible(true);
                }
            }
        });


    }

    public void initWidths() {
        // set prototype widths for the columns:
        for (int i = 0; i < ThumbTableModel.COLNAMES.length; i++) {
            int width = 40;
            if (i <= 4) {
                width = controller.getZoom().getEffectiveWidth() + 2;
            } else if (i == ThumbTableModel.COL_NAME || i == ThumbTableModel.COL_TITLE) {
                width = 60;
            } else if (i == ThumbTableModel.COL_DIR) {
                width = 200;
            }
            getColumnExt(ThumbTableModel.COLNAMES[i]).setPreferredWidth(width);
            setRowHeight(controller.getHeightForThumbLabelComponent());
        }
    }

    void updateNodes(ChangeSet e) {
       thumbTableModel.updateNodes(e);
    }

    void updateRemovedNodes(MoviesRemovedEvent mre) {
        thumbTableModel.updateRemovedNodes(mre);
    }

    private static class LongComparator implements Comparator<Long> {

        @Override
        public int compare(Long o1, Long o2) {
            return o1.compareTo(o2);
        }
    };

    public void refreshModelData(List<AbstractLeafNode> movieNodes) {
        if (thumbTableModel == null) {
            thumbTableModel = new ThumbTableModel(movieNodes);
            setModel(thumbTableModel);

            getSortController().setComparator(ThumbTableModel.COL_FILELEN, new LongComparator());
            getSortController().setComparator(ThumbTableModel.COL_DUP_COUNT, new LongComparator());

            final ThumbTableRenderer thumbTableRenderer = new ThumbTableRenderer(controller, true, false);
            getColumnModel().getColumn(0).setCellRenderer(thumbTableRenderer);
            getColumnModel().getColumn(1).setCellRenderer(thumbTableRenderer);
            getColumnModel().getColumn(2).setCellRenderer(thumbTableRenderer);
            getColumnModel().getColumn(3).setCellRenderer(thumbTableRenderer);
            getColumnModel().getColumn(4).setCellRenderer(thumbTableRenderer);
            //getColumnModel().getColumn(21).setCellRenderer(new DateRenderer());

        } else {
            thumbTableModel.refreshModelData(movieNodes);
        }
  
        Collection<MovieEntry> entries = Filtering.nodes2Entries(Filtering.filterMovieNodes(movieNodes));
        ThreadedThumbCache.getInstance().predict(entries, true);

        initWidths();
    }

    @Override
    public ArrayList<AbstractLeafNode> getAllSelectedNodes() {
        int[] selRows = Tables.getModelSelectedRowsForJXTable(this);
        ArrayList<AbstractLeafNode> nodes = new ArrayList<AbstractLeafNode>();
        for (int row : selRows) {
            nodes.add((AbstractLeafNode) getModel().getValueAt(row, 0));
        }
        return nodes;
    }
}
