/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.token;

/**
 * Basisklasse für Controller für Tokenpanels.
 * @author mla
 */
public abstract class AbstractTokenPanelController implements TokenPanelController {

    private boolean flatView = false;

    /**
     * @return the flatView
     */
    @Override
    public boolean isFlatView() {
        return flatView;
    }

    /**
     * @param flatView the flatView to set
     */
    @Override
    public void setFlatView(boolean flatView) {
        this.flatView = flatView;
    }
}
