/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import org.myjaphoo.model.db.Thumbnail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Funktionen, um Thumb-Images aus den Thumb-Entitäten zu erzeugen.
 * @author mla
 */
public class ThumbImageCreation {

    public static final Logger LOGGER = LoggerFactory.getLogger(ThumbImageCreation.class.getName());

    public static BufferedImage createBIThumbImage(Thumbnail thumb) {
        try {
            InputStream input = new java.io.ByteArrayInputStream(thumb.getThumbnail());
            BufferedImage i = javax.imageio.ImageIO.read(input);
            return i;
        } catch (IOException ex) {
            LOGGER.error("error creating thumb image", ex); //NOI18N
            return null;
        }
    }

    public static BufferedImage makeCenteredImg(BufferedImage i) {
        int w = i.getWidth();
        int h = i.getHeight();
        // try to get the image part, that contains the "face":
        // we just get the upper center part of the image as subimage:
        int wd = w / 3 * 2;
        int hd = h / 3 * 2;
        int x = (w - wd) / 2;
        int y = (h - hd) / 2;

        return i.getSubimage(x, y, wd, hd);
    }
}
