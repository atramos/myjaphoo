/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import org.mlsoft.common.acitivity.events.ActivityFinishedEvent;
import org.mlsoft.common.acitivity.events.ErrorMessageEvent;
import org.mlsoft.common.acitivity.events.MessageEvent;
import org.mlsoft.eventbus.GlobalBus;
import org.mlsoft.eventbus.Subscribe;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.model.db.BookMark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author mla
 */
public class MyjaphooTrayIcon {

    // delay in ms after which a activity finish msg should be displayed
    // also as emphasised message.
    private static long EMPHASISE_DELAY = 1000 * 5;
    private static Logger logger = LoggerFactory.getLogger(MyjaphooTrayIcon.class);
    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/resources/MyjaphooTrayIcon");
    private TrayIcon trayIcon;

    private Menu bmsubmenu;

    public MyjaphooTrayIcon() {
    }

    void initTrayIcon() {
        if (SystemTray.isSupported()) {

            SystemTray tray = SystemTray.getSystemTray();

            ActionListener exitListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting..."); //NOI18N
                    MyjaphooApp.getApplication().exit();
                    //System.exit(0);
                }
            };

            PopupMenu popup = new PopupMenu();
            popup.addSeparator();
            bmsubmenu = new Menu(localeBundle.getString("OPENVIEWWITHBOOKMARK"));
            popup.add(bmsubmenu);
            updateBookmarks(bmsubmenu);
            popup.addSeparator();
            MenuItem defaultItem = new MenuItem(localeBundle.getString("EXIT"));
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);

            trayIcon = new TrayIcon(((ImageIcon) Icons.IR_APP16.icon).getImage(), "MyJaPhoO", popup); //NOI18N

            trayIcon.setImageAutoSize(true);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                logger.error("TrayIcon could not be added.", e); //NOI18N
            }
            initializeChannelListening();

            MainApplicationController.getInstance().getBookmarkList().addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    updateBookmarks(bmsubmenu);
                }
            });
        } else {
            //  System Tray is not supported
        }

    }

    @Subscribe(onETD = true)
    public void errormessage(ErrorMessageEvent event) {
        trayIcon.displayMessage(event.getChannel().getActivityTitle(), event.getErrorMessage(), TrayIcon.MessageType.ERROR);
    }

    @Subscribe(onETD = true)
    public void message(MessageEvent event) {
        if (event.isEmphasised()) {
            trayIcon.displayMessage(event.getChannel().getActivityTitle(), event.getMessage(), TrayIcon.MessageType.INFO);
        }
    }

    @Subscribe(onETD = true)
    public void activityStopped(ActivityFinishedEvent event) {
        // if the activity has taken longer than a certain amount of time, then
        // do also set a empasised message.
        // this will e.g. displayed as info message in the tray icon.
        if (event.getChannel().getTime() > EMPHASISE_DELAY) {
            trayIcon.displayMessage(event.getChannel().getActivityTitle(), "finished " + event.getChannel().getActivityTitle(), TrayIcon.MessageType.INFO);
        }
    }

    private void initializeChannelListening() {
        GlobalBus.bus.register(this);
    }

    private void updateBookmarks(Menu bmsubmenu) {
        List<BookMark> bookmarks = MainApplicationController.getInstance().getBookmarkList();
        bmsubmenu.removeAll();
        for (final BookMark bm : bookmarks) {
            MenuItem bmmenuitem = new MenuItem(bm.getName());

            ActionListener actionListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    MyjaphooApp.getApplication().show(new MyjaphooView(MyjaphooApp.getApplication(), bm.toChronic()));
                }
            };

            bmmenuitem.addActionListener(actionListener);
            bmsubmenu.add(bmmenuitem);
        }

    }
}
