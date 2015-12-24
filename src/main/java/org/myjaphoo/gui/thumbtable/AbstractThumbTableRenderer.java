/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.PinstripePainter;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;

/**
 * Basisklasse für die Thumb-Table Renderer.
 * @author mla
 */
public class AbstractThumbTableRenderer extends DefaultTableRenderer {

    private ThumbRendering thumbRendering;
    private ThumbPanelController controller;

    public AbstractThumbTableRenderer(ThumbPanelController controller) {
        this.controller = controller;
        thumbRendering = new ThumbRendering(controller);
    }

    /**
     * @return the controller
     */
    public ThumbPanelController getController() {
        return controller;
    }

    /**
     * @return the thumbRendering
     */
    public ThumbRendering getThumbRendering() {
        return thumbRendering;
    }
}
