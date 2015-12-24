/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.Icon;
import org.myjaphoo.MyjaphooController;

/**
 * Ein Wrapper für die Funktion, dass die letzten Kommandos
 * im Thumbpanel oben angezeigt werden, um sie erneut auszuführen.
 * Für die Kontextkommandos sollen diese ja mit einem neuen Kontext
 * (den gerade angewählten movies) ausgeführt werden.
 * Dieser Wrapper ermöglich das Aufrufen der Kommandos mit den
 * gerade gesetzten Kontext aus dem Thumbpanel fenster.
 * 
 * @author mla
 */
public class LastActionContextActionWrapper extends AbstractWankmanAction {

    private AbstractWankmanAction contextAction;

    public LastActionContextActionWrapper(MyjaphooController controller, AbstractWankmanAction contextAction) {
        super(controller, (String) contextAction.getValue(Action.NAME), (Icon) contextAction.getValue(Action.SMALL_ICON));
        this.contextAction = contextAction;
    }

    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        // erneut aufrufen mit neuen aktuellen Kontext:
        contextAction.run(controller, e, new ViewContext(controller.getAllSelectedNodes()));
    }
}
