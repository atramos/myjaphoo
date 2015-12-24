/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree;

import java.awt.EventQueue;
import java.util.List;
import javax.swing.Icon;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.gui.ThumbTypeDisplayMode;
import org.myjaphoo.model.FileType;
import org.myjaphoo.MovieNode;
import org.myjaphoo.gui.WmTreeTableModel;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.gui.thumbtable.thumbcache.ThreadedThumbCache;
import org.myjaphoo.gui.thumbtable.thumbcache.ThumbIsLoadedCallback;
import org.myjaphoo.gui.thumbtable.thumbcache.ThumbNowLoadedMsg;
import org.myjaphoo.model.db.MovieEntry;



/**
 * Class to generate a icon for the movie tree for a tree node.
 * @author lang
 */
public class MovieTreeIconCreator {

    public static Icon createIcon(Object value, boolean previewThumbs, int rowHeight, final TreeTableModel model) {
        if (previewThumbs) {
            if (value instanceof MovieNode) {
                MovieNode node = (MovieNode) value;
                MovieEntry entry = node.getMovieEntry();
                return ThreadedThumbCache.getInstance().getThumb(entry, 0, true, rowHeight, ThumbTypeDisplayMode.NORMAL, null);
            } else if (value instanceof MovieStructureNode) {
                MovieStructureNode snode = (MovieStructureNode) value;
                List<? extends AbstractTreeNode> children = snode.getChildren();
                if (children != null && children.size() > 0 && children.get(0) instanceof MovieNode) {
                    final MovieNode node = (MovieNode) children.get(0);
                    MovieEntry entry = node.getMovieEntry();
                    return ThreadedThumbCache.getInstance().getThumb(entry, 0, true, rowHeight, ThumbTypeDisplayMode.NORMAL, new ThumbIsLoadedCallback() {

                        @Override
                        public void notifyIsLoaded(ThumbNowLoadedMsg msg) {

                            EventQueue.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    if (model instanceof WmTreeTableModel) {
                                        ((WmTreeTableModel) model).nodeChanged(node);
                                    }
                                }
                            });


                        }
                    });
                }
            }
        }
        if (value instanceof AbstractLeafNode) {
            AbstractLeafNode node = (AbstractLeafNode) value;
            return createLeafNodeIcon(node);
        }
        return null;
    }

    private static Icon createLeafNodeIcon(AbstractLeafNode node) {
        if (node.is(FileType.Movies)) {
            return Icons.IR_MOVIE.icon;
        } else if (node.is(FileType.Pictures)) {
            return Icons.IR_PICTURE.icon;
        } else if (node.is(FileType.Text)) {
            return Icons.IR_TEXT.icon;
        } else if (node.is(FileType.CompressedFiles)) {
            return Icons.IR_COMPRESSED.icon;
        } else {
            return null;
        }
    }
}
