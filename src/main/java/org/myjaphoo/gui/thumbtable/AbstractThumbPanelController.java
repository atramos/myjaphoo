/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;


import org.myjaphoo.gui.ThumbTypeDisplayMode;
import org.myjaphoo.gui.util.Colorization;
import org.myjaphoo.model.ThumbMode;

import javax.swing.*;
import java.awt.*;


/**
 *
 * @author mla
 */
public abstract class AbstractThumbPanelController implements ThumbPanelController {

    private final int fontHeight;
    /** zoom einstellungen für thumb-ansicht. */
    private Zooming zoom = new Zooming();
    private Colorization colorization = new Colorization();
    private ThumbMode mode = ThumbMode.ALTTHUMB;
    /** wenn gesetzt, dann werden identische entries (die entstehen,
     * wenn durch gruppierungen, diese mehrfach aufgelistet werden) ausgefiltert.*/
    private boolean preventGroupingDups;
    /** wether thumbnail card view is activated or not. */
    private boolean cardView = false;
    private ThumbTypeDisplayMode thumbTypeDisplayMode = ThumbTypeDisplayMode.NORMAL;

    public AbstractThumbPanelController() {
        JLabel label =  new JLabel();
        FontMetrics fm = label.getFontMetrics(label.getFont());
        fontHeight = fm.getHeight();
    }

    @Override
    public Zooming getZoom() {
        return zoom;
    }

    @Override
    public Colorization getColorization() {
        return colorization;
    }

    @Override
    public void setThumbMode(ThumbMode mode) {
        this.mode = mode;
    }

    @Override
    public ThumbMode getThumbMode() {
        return mode;
    }

    /**
     * @return the preventGroupingDups
     */
    @Override
    public boolean isPreventGroupingDups() {
        return preventGroupingDups;
    }

    /**
     * @param preventGroupingDups the preventGroupingDups to set
     */
    @Override
    public void setPreventGroupingDups(boolean preventGroupingDups) {
        this.preventGroupingDups = preventGroupingDups;
    }



    /**
     * @return the cardView
     */
    @Override
    public boolean isCardView() {
        return cardView;
    }

    /**
     * @param cardView the cardView to set
     */
    @Override
    public void setCardView(boolean cardView) {
        this.cardView = cardView;
    }

    /**
     * @return the thumbTypeDisplayMode
     */
    public ThumbTypeDisplayMode getThumbTypeDisplayMode() {
        return thumbTypeDisplayMode;
    }

    /**
     * @param thumbTypeDisplayMode the thumbTypeDisplayMode to set
     */
    public void setThumbTypeDisplayMode(ThumbTypeDisplayMode thumbTypeDisplayMode) {
        this.thumbTypeDisplayMode = thumbTypeDisplayMode;
    }

    /**
     * Returns the height fora thumb label component which gets used e.g. in the thumb views.
     * This is the height of a thumb + one row for displaying the thumb name.
     * @return
     */
    public int getHeightForThumbLabelComponent() {
        return getZoom().getEffectiveHeight() + /*5 +*/ fontHeight + 5;
    }
}
