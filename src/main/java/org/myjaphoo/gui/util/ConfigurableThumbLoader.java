package org.myjaphoo.gui.util;

import org.myjaphoo.gui.ThumbTypeDisplayMode;

import java.awt.image.BufferedImage;

/**
 * ConfigurableThumbLoader
 * @author lang
 * @version $Id$
 */
public interface ConfigurableThumbLoader {

    /**
     * Try to load a thumb nail. If its not possible for this thumb loader it should return null, which
     * indicates to try out the next configured thumb loader.
     * @param movieId
     * @param column
     * @param mode
     * @return
     */
    BufferedImage load(long movieId, int column, ThumbTypeDisplayMode mode);
}
