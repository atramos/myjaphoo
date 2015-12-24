package org.myjaphoo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * LookAndFeels. we use insubstantial substance themes.
 *
 * @author lang
 * @version $Id$
 */
public class LookAndFeels {
    /**
     * logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(LookAndFeels.class.getName());

    public static void setPlaf() {
        AvailableLookAndFeels availableLookAndFeels = (AvailableLookAndFeels) MyjaphooAppPrefs.PRF_PLAF_CLASS.getSelectedEnum();
        if (availableLookAndFeels != null) {
            availableLookAndFeels.activate();
        } else {
            AvailableLookAndFeels.CREMECOFFEESKIN.activate();
        }

    }


    public static void prepareLaFMenus(JMenu menu) {
        for (AvailableLookAndFeels availableLookAndFeels : AvailableLookAndFeels.values()) {
            menu.add(createPlafChangeAction(availableLookAndFeels));
        }
    }

    private static Action createPlafChangeAction(final AvailableLookAndFeels availableLookAndFeels) {
        return new AbstractAction(availableLookAndFeels.getUIName()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                availableLookAndFeels.activate();
            }
        };
    }
}
