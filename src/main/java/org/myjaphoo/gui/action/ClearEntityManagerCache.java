/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.logic.MyjaphooDB;

/**
 * Clears the entity manager cache.
 * @author lang
 */
public class ClearEntityManagerCache extends AbstractWankmanAction {
    
    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/ClearEntityManagerCache");

    public ClearEntityManagerCache(MyjaphooController controller) {
        super(controller, localeBundle.getString("CLEAR ENTITY MANAGER CACHE"), null);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context)  {
        MyjaphooDB.singleInstance().emClear();

    }
}
