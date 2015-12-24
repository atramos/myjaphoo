/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import org.myjaphoo.gui.picmode.Picture;
import org.myjaphoo.gui.util.CachingHashMap;
import org.myjaphoo.model.logic.FileSubstitutionImpl;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Liefert hochskalierte thumb nails direkt aus den bild-dateien. Das liefert schönere thumbs für
 * grössere thumb grössen, allerdings dauert das laden dadurch auch länger.
 * Die Daten werden gecached u. entsprechend an die aktuelle Größe angepasst.
 *
 * @author mla
 */
public class ScaledThumbCache {

    private static ScaledThumbCache singleton = new ScaledThumbCache();
    private FileSubstitutionImpl fileSubstitution = new FileSubstitutionImpl();

    public static ScaledThumbCache getSingleton() {
        return singleton;

    }

    private ScaledThumbCache() {
    }

    /**
     * cache beinhaltet "vor-verkleinerte" thumbs in dieser grösse
     */
    private static final int WIDTH_OF_CACHED_THUMBS = 300;

    /**
     * max. cache grösse.
     */
    private static final int MAX_CACHE_SIZE = 400;

    private CachingHashMap<String, BufferedImage> cache = new CachingHashMap<String, BufferedImage>(MAX_CACHE_SIZE);
    ExecutorService service = Executors.newFixedThreadPool(10);


    public ImageIcon getThumbImage(String canonicalPath, int size) {

        return getHighQualityScaledThumb(canonicalPath, size);
    }

    private ImageIcon getHighQualityScaledThumb(String canonicalPath, int size) {
        BufferedImage cachedImage = getCachedThumb(canonicalPath);
        if (cachedImage == null) {
            // erzeugen im background laufen lasssen (um es gecached für nächsten aufruf zu haben)
            runCachedThumbCreation(canonicalPath);
            return null;
        } else {
            // scale the "pre-scaled" cached image to the needed size:
            BufferedImage thumb = Picture.scaleToWidth(cachedImage, size);
            return new ImageIcon(thumb);
        }
    }

    private synchronized BufferedImage getCachedThumb(String canonicalPath) {
        return cache.get(canonicalPath);
    }

    private synchronized void putInCache(BufferedImage thumb, String canonicalPath) {
        cache.put(canonicalPath, thumb);
    }

    /**
     * erzeugt einen thread, um im hintergrund ein hi-res thumb bild zu erzeugen und in den cache abzulegen.
     */
    private void runCachedThumbCreation(final String canonicalPath) {
        final String locatedPath = fileSubstitution.locateFileOnDrive(canonicalPath);
        if (locatedPath != null) {
            Runnable runner = new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedImage image = Picture.loadimage(new File(locatedPath));
                        BufferedImage thumb = Picture.scaleToWidth(image, WIDTH_OF_CACHED_THUMBS);
                        putInCache(thumb, canonicalPath);
                    } catch (Exception ex) {
                        LoggerFactory.getLogger(ScaledThumbCache.class.getName()).error("error", ex); //NOI18N

                    }
                }
            };
            service.submit(runner);
        }
    }
}
