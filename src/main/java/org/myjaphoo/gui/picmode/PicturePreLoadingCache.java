/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.picmode;

import org.myjaphoo.model.logic.FileSubstitutionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Cache, der die nächsten Bilder bereits vorlädt, um Zeit zu sparen.
 * Wahlweise aktivierbar und nutzbar über den ImageIterator.
 * @author mla
 */
public class PicturePreLoadingCache {

    private FileSubstitutionImpl fileSubstitution = new FileSubstitutionImpl();
    public static final Logger LOGGER = LoggerFactory.getLogger(PicturePreLoadingCache.class.getName());
    private ArrayList<PictureLoadingEventListener> listenerList = new ArrayList<PictureLoadingEventListener>();

    public static interface PictureLoadingEventListener {

        void pictureLoadedEvent(String path, BufferedImage image);
    }

    public void addListener(PictureLoadingEventListener listener) {
        listenerList.add(listener);
    }

    public void removeListener(PictureLoadingEventListener listener) {
        listenerList.remove(listener);
    }

    private void raiseEvent(String path, BufferedImage image) {
        for (PictureLoadingEventListener listener : listenerList) {
            listener.pictureLoadedEvent(path, image);
        }
    }

    public PicturePreLoadingCache() {
    }
    /** max. cache grösse. */
    private static final int MAX_CACHE_SIZE = 10;

    static class ThumbCache<K, V> extends LinkedHashMap<K, V> {

        public ThumbCache() {
            // access ordered linked map erzeugen.
            super(MAX_CACHE_SIZE * 3, 0.75f, true);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            // bedeuted hierbei, dass das am wenigsten zugegriffene element gelöscht wird,
            // wenn wir den maximal wert überschreiten.
            return size() > MAX_CACHE_SIZE;
        }
    }

    static class ImageEntry {

        private SoftReference<BufferedImage> softimage;
        private BufferedImage fixReference = null;

        private ImageEntry(BufferedImage img) {
            softimage = new SoftReference<BufferedImage>(img);
        }

        /**
         * workaround, um den Loading- marker mit einem fixen reference zu fixieren.
         */
        private ImageEntry(BufferedImage img, boolean fixRef) {
            if (fixRef) {
                fixReference = img;
            }
            softimage = new SoftReference<BufferedImage>(img);
        }

        public BufferedImage getImage() {
            if (fixReference != null) {
                return fixReference;
            } else {
                return softimage.get();
            }
        }
    }
    private ThumbCache<String, ImageEntry> cache = new ThumbCache<String, ImageEntry>();
    /** marker, wenn das bild gerade im hintergrund geladen wird. */
    private static final BufferedImage LOADING = new BufferedImage(1, 1, ColorSpace.TYPE_RGB);
    private static final ImageEntry LOADINGENTRY = new ImageEntry(LOADING, true);
    /** service zum laden der bilder im hintergrund. */
    private static ExecutorService service = Executors.newFixedThreadPool(10);

    public BufferedImage getImage(final String path, final int width, final int height) {
        BufferedImage cachedImage = getCachedThumb(path);
        if (cachedImage == null) {
            triggerPreLoading(path, width, height);
            return delayABitAndTryCacheAgain(path);
//            return null;
            //return getImageInForeGround(path, width, height);
        } else if (cachedImage == LOADING) {
            return delayABitAndTryCacheAgain(path);
            //return null;
            //return waitInForeGroundForFinishingBackgroundLoading(path);
        } else {
            return cachedImage;
        }
    }

    private BufferedImage delayABitAndTryCacheAgain(String path) {
        try {
            Thread.sleep(50);
            BufferedImage cachedImage = getCachedThumb(path);
            if (cachedImage == null || cachedImage == LOADING) {
                return null;
            }
            return cachedImage;
        } catch (InterruptedException ex) {
            LOGGER.error("cant sleep thread!", ex); //NOI18N
        }
        return null;
    }

    private synchronized BufferedImage getCachedThumb(String canonicalPath) {
        ImageEntry entry = cache.get(canonicalPath);
        if (entry == null) {
            return null;
        }
        return entry.getImage();
    }

    private synchronized void putInCache(BufferedImage thumb, String canonicalPath) {
        cache.put(canonicalPath, new ImageEntry(thumb));
    }

    synchronized void  destruct() {
        cache.clear();
    }

    /**
     * erzeugt einen thread, um im hintergrund ein hi-res thumb bild zu erzeugen und in den cache abzulegen.
     */
    public void triggerPreLoading(final String path, final int width, final int height) {
        LOGGER.debug("Prüfe ob Laden notwendig ist für " + path); //NOI18N
        if (getCachedThumb(path) == null) {
            final String locatedPath = fileSubstitution.locateFileOnDrive(path);
            if (locatedPath != null) {
                Runnable runner = new Runnable() {

                    @Override
                    public void run() {
                        try {
                            LOGGER.debug("Hintergrund job startet zum laden von " + path); //NOI18N
                            // marker für loading:
                            synchronized (PicturePreLoadingCache.this) {
                                // checke zuerst, obs nicht schon in loading ist (oder fertig ist):
                                BufferedImage imgTst = getCachedThumb(path);
                                if (imgTst != null && imgTst != LOADING) {
                                    // bereits geladen:
                                    LOGGER.debug("beende job: " + path + " ist bereits im cache"); //NOI18N
                                    return;
                                }
                                if (imgTst == LOADING) {
                                    LOGGER.debug("beende job: " + path + " wird von anderem job geladen..."); //NOI18N
                                    return; // vorder- oder anderer hintergrund job lädt bereits.
                                }
                                // andernfalls markiere als "zu ladend":
                                LOGGER.debug("markiere " + path + " als 'wird gerade geladen'"); //NOI18N
                                putInCache(LOADING, path);
                            }

                            // und beginne nun mit dem laden (unsynchronisiert):
                            BufferedImage image = loadAndScale(locatedPath, width, height);
                            LOGGER.debug("fertig geladen füge in cache ein: " + path); //NOI18N
                            putInCache(image, path);
                            raiseEvent(path, image);
                        } catch (Exception ex) {
                            LOGGER.error("error während hintergrund laden!", ex); //NOI18N
                            // "lade" markierung wieder zurücksetzen:
                            putInCache(null, path);
                        }
                    }
                };
                service.submit(runner);
            }
        }
    }

    private BufferedImage loadAndScale(String path, int width, int height) throws Exception {
        BufferedImage img = Picture.loadimage(new File(path));
        if (img == null) {
            return null;
        }
        float factorw = (float) width / img.getWidth();
        float factorh = (float) height / img.getHeight();
        float factor = factorw > factorh ? factorh : factorw;

        LOGGER.info("scaling to target window size, factor:" + factor); //NOI18N
        img = Picture.getScaledInstance(img, factor);
        return img;
    }
}
