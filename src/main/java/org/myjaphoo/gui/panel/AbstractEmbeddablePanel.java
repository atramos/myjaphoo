/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui.panel;

import javax.swing.JPanel;
import org.jdesktop.swingx.JXPanel;

/**
 *
 * @author mla
 */
public abstract class AbstractEmbeddablePanel extends JPanel {

    public abstract String getTitle();

    /**
     * Stellt den View neu dar: Daten werden erneut vom Controller geholt u. die Ansicht neu dargestellt.
     */
    public abstract void refreshView();

    /**
     * Der View wird geupdated u. es wird "nichts" bzw. "leer" dargestellt.
     * Wichtig, insbesondere bei manchen views, die JPA-Entities darstellen, dass ggf. mal kurzzeitig keine Verweise
     * im View-Modell existieren. Muss nicht von allen Views implementiert werden.
     */
    public abstract void clearView();
}
