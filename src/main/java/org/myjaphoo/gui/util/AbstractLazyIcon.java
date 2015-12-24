/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.util;

import java.awt.Component;
import java.awt.Graphics;
import java.lang.ref.SoftReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.myjaphoo.gui.thumbtable.thumbcache.ThumbIsLoadedCallback;
import org.myjaphoo.gui.thumbtable.thumbcache.ThumbNowLoadedMsg;

/**
 * Ein ImageIcon, dass erst verzögert erzeugt/geladen wird.
 * Es wird erst bei der ersten Verwendung/zeichnen zum erzeugen getriggert.
 *
 * Das Image wird als Softreferenz gehalten u. damit bei Speicherbedarf wieder
 * freigegeben.
 * Wenn es anschliessend wieder für die GUI benötigt wird, so wird es erneut
 * nachgeladen.
 * @author mla
 */
public abstract class AbstractLazyIcon implements Icon {

    ExecutorService service = Executors.newFixedThreadPool(5);
    private volatile SoftReference<ImageIcon> delayedIconRef = null;
    private int cachedWidth = 0;
    private int cachedHeight = 0;

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (isDelayedIconReady()) {
            ImageIcon icon = deRef();
            if (icon != null) {
                icon.paintIcon(c, g, x, y);
            }
        }
    }

    private ImageIcon deRef() {
        if (delayedIconRef == null) {
            return null;
        }
        return delayedIconRef.get();
    }

    @Override
    public int getIconWidth() {
        if (cachedWidth == 0) {
            if (isDelayedIconReady()) {
                ImageIcon icon = deRef();
                if (icon != null) {
                    cachedWidth = icon.getIconWidth();
                }
            }
        }
        return cachedWidth;
    }

    @Override
    public int getIconHeight() {
        if (cachedHeight == 0) {
            if (isDelayedIconReady()) {
                ImageIcon icon = deRef();
                if (icon != null) {
                    return icon.getIconHeight();
                }
            }
        }
        return cachedHeight;
    }

    private boolean isDelayedIconReady() {
        if (deRef() == null) {
            triggerCreationOfIcon();
            return false;
        } else {
            return true;
        }

    }

    private void triggerCreationOfIcon() {
        service.submit(new Runnable() {

            @Override
            public void run() {
                ImageIcon icon = createIcon(new ThumbIsLoadedCallback() {

                    @Override
                    public void notifyIsLoaded(ThumbNowLoadedMsg msg) {
                        ImageIcon icon = new ImageIcon(msg.getBi());
                        delayedIconRef = new SoftReference<ImageIcon>(icon);
                    }
                });
                delayedIconRef = new SoftReference<ImageIcon>(icon);
            }
        });

    }

    /**
     * Erzeugt das Icon. Diese Methode wird in einem Hintergrundprozess
     * ausgeführt, um im Background das Image zu erzeugen.
     */
    protected abstract ImageIcon createIcon(ThumbIsLoadedCallback loadedCallBack);
}
