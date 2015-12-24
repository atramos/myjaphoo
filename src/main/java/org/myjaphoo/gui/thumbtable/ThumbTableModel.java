/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import org.myjaphoo.MovieNode;
import org.myjaphoo.gui.WmTableModel;
import org.myjaphoo.gui.editor.rsta.CachedHints;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.util.Helper;
import org.myjaphoo.gui.util.Utils;
import org.myjaphoo.model.WmEntitySet;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.cache.ChangeSet;
import org.myjaphoo.model.cache.events.MoviesRemovedEvent;
import org.myjaphoo.model.db.MovieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author mla
 */
public class ThumbTableModel extends WmTableModel<AbstractLeafNode> {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/thumbtable/resources/ThumbTableModel");
    public static final String[] COLNAMES = new String[]{
            localeBundle.getString("THUMB1"),
            localeBundle.getString("THUMB2"),
            localeBundle.getString("THUMB3"),
            localeBundle.getString("THUMB4"),
            localeBundle.getString("THUMB5"),
            localeBundle.getString("NAME"),
            localeBundle.getString("DIR"),
            localeBundle.getString("SIZE"),
            localeBundle.getString("CHECKSUM"),
            localeBundle.getString("TAGS"),
            localeBundle.getString("DUPLICATES"),
            localeBundle.getString("WASTEDMEM"),
            localeBundle.getString("RATING"),
            localeBundle.getString("FORMAT"),
            localeBundle.getString("WIDTH"),
            localeBundle.getString("HEIGHT"),
            localeBundle.getString("LEN"),
            localeBundle.getString("FPS"),
            localeBundle.getString("BITRATE"),
            localeBundle.getString("TITLE"),
            localeBundle.getString("COMMENT"),
            localeBundle.getString("EXIF CREATE DATE")
    };
    public static final int COL_DUP_COUNT = 10;
    public static final int COL_DUP_SIZE = 11;
    public static final int COL_FILELEN = 7;
    public static final int COL_NAME = 5;
    public static final int COL_DIR = 6;
    public static final int COL_TITLE = 19;

    public ThumbTableModel(List<AbstractLeafNode> nodes) {
        super(nodes, prepareColumnList());
    }

    private static String[] prepareColumnList() {
        ArrayList<String> colList = new ArrayList<>();
        colList.addAll(Arrays.asList(COLNAMES));

        // add all the attributes:
        colList.addAll(CachedHints.getEntryAttrKeys());
        return colList.toArray(new String[colList.size()]);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AbstractLeafNode node = nodes.get(rowIndex);
        return getValueAt(columnIndex, (MovieNode) node);
    }

    private Object getValueAt(int columnIndex, MovieNode node) {
        switch (columnIndex) {
            case 0:
                return node; // renderer must draw icon from that
            case 1:
                return node;
            case 2:
                return node;
            case 3:
                return node;
            case 4:
                return node;

            case 5:
                return node.getMovieEntry().getName();
            case 6:
                return node.getMovieEntry().getCanonicalDir();
            case COL_FILELEN:
                return node.getMovieEntry().getFileLength();
            case 8:
                return node.getMovieEntry().getChecksumCRC32();
            case 9:
                return Helper.createTokenText(node.getMovieEntry());
            case COL_DUP_COUNT:
                return node.getCondensedDuplicatesSize();
            case COL_DUP_SIZE:
                return Utils.humanReadableByteCount(node.getCondensedDuplicatesSize() * node.getMovieEntry().getFileLength());
            case 12:
                return node.getMovieEntry().getRating() != null ? node.getMovieEntry().getRating().getName() : null;
            case 13:
                return Helper.prepareFormat(node.getMovieEntry().getMovieAttrs().getFormat());
            case 14:
                return node.getMovieEntry().getMovieAttrs().getWidth();
            case 15:
                return node.getMovieEntry().getMovieAttrs().getHeight();
            case 16:
                String fmtLen = org.apache.commons.lang.time.DurationFormatUtils.formatDurationHMS(node.getMovieEntry().getMovieAttrs().getLength() * 1000);
                return fmtLen;
            case 17:
                return node.getMovieEntry().getMovieAttrs().getFps();
            case 18:
                return node.getMovieEntry().getMovieAttrs().getBitrate();
            case COL_TITLE:
                return node.getTitle();
            case 20:
                return node.getComment();
            case 21:
                return node.getMovieEntry().getExifData().getExifCreateDate();
            default:
                String attrName = getColumnName(columnIndex);
                return node.getMovieEntry().getAttributes().get(attrName);

        }
    }

    void updateNodes(ChangeSet e) {
        WmEntitySet currentModel = CacheManager.getCacheActor().getImmutableModel();
        for (int i = 0; i < nodes.size(); i++) {
            MovieNode node = ((MovieNode) nodes.get(i));
            MovieEntry entry = node.getMovieEntry();

            MovieEntry changedEntry = currentModel.getMovieEntrySet().find(entry);
            if (changedEntry != null) {
                node.updateNode(changedEntry);
                if (e.contains(entry)) {
                    // event feuern, fÃ¼r alle spalten, die info enthalten:
                    for (int row = 5; row < COLNAMES.length; row++) {
                        fireTableCellUpdated(i, row);
                    }
                }
            }

        }
    }

    void updateRemovedNodes(MoviesRemovedEvent mre) {
        WmEntitySet currentModel = CacheManager.getCacheActor().getImmutableModel();

        for (int i = nodes.size() - 1; i >= 0; i--) {
            MovieNode node = ((MovieNode) nodes.get(i));
            MovieEntry entry = node.getMovieEntry();

            MovieEntry changedEntry = currentModel.getMovieEntrySet().find(entry);
            if (changedEntry != null) {
                node.updateNode(changedEntry);
                if (mre.contains(entry)) {
                    // remove from the model:
                    nodes.remove(i);
                    fireTableRowsDeleted(i, i);
                }
            }
        }
    }
}
