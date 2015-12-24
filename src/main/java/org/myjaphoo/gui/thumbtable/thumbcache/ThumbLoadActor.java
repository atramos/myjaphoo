/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable.thumbcache;

import groovyx.gpars.actor.DynamicDispatchActor;
import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.gui.ThumbTypeDisplayMode;
import org.myjaphoo.gui.picmode.Picture;
import org.myjaphoo.gui.util.ThumbImageCreation;
import org.myjaphoo.gui.util.ThumbLoader;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

/**
 * Actor, der das Laden von Thumbs übernimmt.
 * Schickt nach dem Laden eine Message an den Sender als benachrichtigung,
 * dass der Thumb geladen ist.
 * @author lang
 */
public class ThumbLoadActor extends DynamicDispatchActor {

    private static int THUMB_HEIGHT = MyjaphooAppPrefs.PRF_THUMBSIZE.getVal();
    private static int THUMB_WIDTH = MyjaphooAppPrefs.PRF_THUMBSIZE.getVal();
    private static final Logger logger = LoggerFactory.getLogger(ThumbLoadActor.class);

    private ThumbLoader thumbLoader;

    public ThumbLoadActor(DBConnection dBConnection) {
        thumbLoader = new ThumbLoader(dBConnection);
    }


    public void onMessage(ThumbLoadMsg msg) {

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("actor loading thumb " + msg.getKey().toString()); //NOI18N
            }

            BufferedImage bi = load(msg.getId(), msg.getColumn(), msg.isCenterThumb(), msg.getSize(), msg.getMode());

            getSender().send(new ThumbNowLoadedMsg(msg.getKey(), bi, msg.getId(), msg.getLoadCallBack()));
        } catch (Exception e) {
            logger.error("error processing thumb load message!", e);
        }
    }

    public BufferedImage load(final long movieId, final int column, final boolean center, final Integer size, ThumbTypeDisplayMode mode) {

        BufferedImage bi = thumbLoader.load(movieId, column, mode);
        if (bi != null) {
            if (center) {
                bi = ThumbImageCreation.makeCenteredImg(bi);
            }
            if (size != null) {
                // sondergrösse
                bi = Picture.scaleToWidth(bi, size);
            } else {
                // standardgrösse:
                bi = checkCorrectScaling(bi);
            }
        }
        return bi;
    }

    /**
     * In der Vergangenheit wurden Thumbs nicht immer auf die richtige Grösse skaliert wenn sie erzeugt und gespeichert wurden.
     * D.h. teilweise waren sie grösser, und passten deshalb nicht ganz auf den
     * JLabel des Thumbs. Die Ränder waren nicht sichtbar und insbesondere auch nicht
     * die ganzen Info-Icons und die Beschriftung.
     * Hier wird geprüft, ob die Grösse passt, und wenn es zu gross ist,
     * dann wird es nochmals verkleinert.
     * @param bi buffered image
     * @return
     */
    private BufferedImage checkCorrectScaling(BufferedImage bi) {
        if (bi == null) {
            return null;
        }
        if (bi.getWidth() > THUMB_WIDTH || bi.getHeight() > THUMB_HEIGHT) {
            return Picture.scaleToStdThumbSize(bi);
        } else {
            return bi;
        }
    }
}
