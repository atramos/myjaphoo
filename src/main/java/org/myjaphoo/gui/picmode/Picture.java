package org.myjaphoo.gui.picmode;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.gui.thumbtable.thumbcache.TokenThumbCache;

/**
 * <p>
 * �berschrift:
 * </p>
 * <p>
 * Beschreibung:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Organisation:
 * </p>
 *
 * @author unbekannt
 * @version 1.0
 */
public class Picture {

    public static BufferedImage loadimage(File file) throws Exception {
        return javax.imageio.ImageIO.read(file);
    }

    public static BufferedImage scaleToStdThumbSize(BufferedImage img) {
        if (img.getWidth() > img.getHeight()) {
            // scale by width
            double factor = ((double) MyjaphooAppPrefs.PRF_THUMBSIZE.getVal()) / img.getWidth();
            return getScaledInstance(img, factor);
        } else {
            // scale by height:
            // bei der höhe müssen wir noch die schrift einberechnen. deshabl eine schrifthöhe abnehmen:
            double schrifthoehe = 25.0;
            double factor = ((double) MyjaphooAppPrefs.PRF_THUMBSIZE.getVal() - schrifthoehe) / img.getHeight();
            return getScaledInstance(img, factor);
        }

    }

    public static BufferedImage scaleToWidth(BufferedImage img, int toSize) {
        double maxDim = Math.max(img.getWidth(), img.getHeight());
        double factor = ((double) toSize) / maxDim;
        return getScaledInstance(img, factor);
    }

    public static BufferedImage getScaledInstance(BufferedImage img, double factor) {
        AffineTransform tr = AffineTransform.getScaleInstance(factor, factor);
        AffineTransformOp op = new AffineTransformOp(tr, AffineTransformOp.TYPE_BICUBIC);
        return op.filter(img, null);
    }

    public static BufferedImage getScaledInstance(BufferedImage img, double xfactor, double yfactor) {
        AffineTransform tr = AffineTransform.getScaleInstance(xfactor, yfactor);
        AffineTransformOp op = new AffineTransformOp(tr, AffineTransformOp.TYPE_BICUBIC);
        return op.filter(img, null);
    }

    public static byte[] toByte(BufferedImage img) throws IOException {
        java.io.ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "JPG", baos); //NOI18N
        return baos.toByteArray();
    }

    public static InputStream toInputStream(ImageIcon icon) throws IOException{
        BufferedImage bi = Picture.toBufferedImage(icon.getImage());
        InputStream fis = new ByteArrayInputStream(toByte(bi));
        return fis;
    }


    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }

        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    /**
     * Create a buffered image from an image.
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        return toRGBBufferedImage(image);
    }

    /**
     * Create a buffered image from an image.
     */
    public static BufferedImage toRGBBufferedImage(Image image) {

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.getGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    /**
     * Creates a image from a icon.
     * @param icon
     * @return 
     */
    public static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }
}
