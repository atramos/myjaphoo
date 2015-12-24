package org.myjaphoo.gui;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Property change listener which dispatches its events on the swing event dispatch thread.
 *
 * @author mla
 * @version $Id$
 */
public abstract class EdtDispatcherPropertyChangeListener implements PropertyChangeListener {
    @Override
    public final void propertyChange(final PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                propertyChangeDispatchedOnEdt(evt);
            }
        });
    }

    public abstract void propertyChangeDispatchedOnEdt(PropertyChangeEvent evt);
}
