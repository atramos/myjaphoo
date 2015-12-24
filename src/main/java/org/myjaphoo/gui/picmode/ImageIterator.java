/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.picmode;

import org.myjaphoo.MovieNode;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author mla
 */
public class ImageIterator {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/picmode/resources/ImageIterator");

    private PicturePreLoadingCache cache = new PicturePreLoadingCache();
    public static final Logger LOGGER = LoggerFactory.getLogger(ImageIterator.class.getName());

    public boolean isCurrent(String path) {
        AbstractLeafNode node = imgList.get(index);
        return node.getCanonicalPath().equals(path);
    }

    void destruct() {
        cache.destruct();
    }

    public String getcurrentToolTipText() {
        AbstractLeafNode node = imgList.get(index);

        if (node instanceof MovieNode) {
            return Helper.createThumbTipText((MovieNode) node);
        } else {
            return ""; //NOI18N
        }
    }

    public static class ImageInfo {

        String descr;
        BufferedImage image;
        String toolTipText;
    }
    private List<AbstractLeafNode> imgList;
    private int index = 0;

    public void addListener(PicturePreLoadingCache.PictureLoadingEventListener listener) {
        cache.addListener(listener);
    }

    public void removeListener(PicturePreLoadingCache.PictureLoadingEventListener listener) {
        cache.removeListener(listener);
    }

    public ImageIterator(List<AbstractLeafNode> imgList) {
        this.imgList = imgList;
    }

    public ImageInfo next(int width, int height) {
        index = nextIndex();
        return getCurrent(width, height);
    }

    private int nextIndex() {
        return bounds(index + 1);
    }

    private int prevIndex() {
        return bounds(index - 1);
    }

    private int bounds(int i) {
        if (i >= imgList.size()) {
            i = 0;
        }
        if (i < 0) {
            i = imgList.size() - 1;
        }
        return i;
    }

    public ImageInfo prev(int width, int height) {
        index = prevIndex();
        return getCurrent(width, height);
    }

    public ImageInfo getCurrent(int width, int height) {
        AbstractLeafNode node = imgList.get(index);
        ImageInfo ii = new ImageInfo();
        ii.descr = MessageFormat.format(localeBundle.getString("IMAGE DESC"), node.getCanonicalPath(), index, imgList.size());
        // get tool tip text:
        if (node instanceof MovieNode) {
            ii.toolTipText = Helper.createThumbTipText((MovieNode) node);
        }

        try {
            BufferedImage img = cache.getImage(node.getCanonicalPath(), width, height);
            ii.image = img;
            cache.triggerPreLoading(imgList.get(bounds(index + 1)).getCanonicalPath(), width, height);
            cache.triggerPreLoading(imgList.get(bounds(index + 2)).getCanonicalPath(), width, height);
            cache.triggerPreLoading(imgList.get(bounds(index + 3)).getCanonicalPath(), width, height);
            cache.triggerPreLoading(imgList.get(bounds(index + 4)).getCanonicalPath(), width, height);
            cache.triggerPreLoading(imgList.get(bounds(index + 5)).getCanonicalPath(), width, height);
            cache.triggerPreLoading(imgList.get(bounds(index - 1)).getCanonicalPath(), width, height);
            cache.triggerPreLoading(imgList.get(bounds(index - 2)).getCanonicalPath(), width, height);
        } catch (Exception ex) {
            LOGGER.warn("could not load image", ex); //NOI18N
        }

        return ii;
    }
}
