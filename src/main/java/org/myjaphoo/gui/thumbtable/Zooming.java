/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.gui.ThumbTypeDisplayMode;
import org.myjaphoo.gui.movietree.DiffNode;
import org.myjaphoo.gui.thumbtable.thumbcache.ThreadedThumbCache;
import org.myjaphoo.gui.thumbtable.thumbcache.ThumbIsLoadedCallback;
import org.myjaphoo.model.FileType;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.dbcompare.ComparisonSetGenerator;
import org.myjaphoo.model.dbcompare.DatabaseComparison;

import javax.swing.*;


/**
 *
 * @author mla
 */
public class Zooming {

    private int Thumb_height = MyjaphooAppPrefs.PRF_THUMBSIZE.getVal();
    private int Thumb_width = MyjaphooAppPrefs.PRF_THUMBSIZE.getVal();
    public static final int STD_ZOOM = 100;
    private int zoomPercentage = STD_ZOOM;
    /** wenn true, dann versuche direkt vom file zu laden, anstatt die vorschau-thumbs zu benutzen,
     * um high quality gezoomte vorschaubilder zu erhalten.
    Dies wird allerdings nur versucht, wenn zoom eingeschaltet ist. Geht momentan auch nur für Bilder.
    Damit kann dann ein schärferes, größeres vorschaubild erreicht werden.
     */
    private boolean loadDirectFromFile = false;

    public void setZoomPercentage(int zoomPercentage) {
        this.zoomPercentage = zoomPercentage;
    }

    public int getEffectiveHeight() {
        int effHeight = Thumb_height * getZoomPercentage() / 100;
        return effHeight;
    }

    public int getEffectiveWidth() {
        int effWidth = Thumb_width * getZoomPercentage() / 100;
        return effWidth;
    }

    /**
     * @return the zoomPercentage
     */
    public int getZoomPercentage() {
        return zoomPercentage;
    }

    public boolean isZoomed() {
        return zoomPercentage != STD_ZOOM;
    }

    public ImageIcon getThumbImage(MovieNode node, int column, ThumbTypeDisplayMode mode, ThumbIsLoadedCallback loadedCallBack) {
        if (node.getMovieEntry() == null) {
            return null;
        }

        MovieEntry entry = node.getMovieEntry();
        ThreadedThumbCache cache = ThreadedThumbCache.getInstance();
        if (node instanceof DiffNode && node.getMovieEntry() == ComparisonSetGenerator.NULL_ENTRY) {
            // a diff node where the entry is not existing in our db, so display the thumb from the other db:
            cache = DatabaseComparison.getInstance().getThumbCache();
            entry = ((DiffNode)node).getDbdiff().getCDBEntry();
        }

        if (!isZoomed()) {
            return cache.getThumb(entry, column, false, null, mode, loadedCallBack);
        } else {
            if (loadDirectFromFile && node.is(FileType.Pictures)) {
                ImageIcon ii =  ScaledThumbCache.getSingleton().getThumbImage(entry.getCanonicalPath(), getEffectiveHeight());
                // if not yet loaded or could not loaded, fall back to regular cache:
                if (ii == null) {
                    return cache.getThumb(entry, column, false, getEffectiveHeight(), mode, loadedCallBack);
                } else {
                    return ii;
                }
            } else {
                return cache.getThumb(entry, column, false, getEffectiveHeight(), mode, loadedCallBack);
            }
        }
    }

    /**
     * @return the loadDirectFromFile
     */
    public boolean isLoadDirectFromFile() {
        return loadDirectFromFile;
    }

    /**
     * @param loadDirectFromFile the loadDirectFromFile to set
     */
    public void setLoadDirectFromFile(boolean loadDirectFromFile) {
        this.loadDirectFromFile = loadDirectFromFile;
    }
}
