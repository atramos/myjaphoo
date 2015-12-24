/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui;

import net.infonode.gui.icon.button.DropDownIcon;
import net.infonode.util.Direction;
import org.mlsoft.swing.ComponentForRestCreator;
import org.mlsoft.swing.ResizeLayout;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.AddBookmark;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.gui.util.*;
import org.myjaphoo.model.db.BookMark;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 *
 * @author mla
 */
public class BookMarkMenuCreator {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/resources/BookMarkMenuCreator");
    private MyjaphooController controller;
    /**
     * creates a menu for all the "rest" of components, that could not be shown
     * in the bookmark toolbar.
     */
    private ComponentForRestCreator restAsMenuCreator = new ComponentForRestCreator() {

        @Override
        public JComponent createOtherComponent(Vector<Component> theRest) {
            JMenu rest = new JMenu();
            DropDownIcon i = new DropDownIcon(12, Direction.DOWN);
        
            rest.setIcon(i);
            for (Component c : theRest) {
                // we need to copy the items because we insert them into another component:
                final JMenuItem item = (JMenuItem) c;
                JMenuItem restItem = new JMenuItem();
                restItem.setText(item.getText());
                restItem.setIcon(item.getIcon());
                restItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        item.doClick();
                    }
                });
                rest.add(restItem);
            }
            return rest;
        }
    };

    public BookMarkMenuCreator(MyjaphooController controller) {
        this.controller = controller;
    }

    enum BookMarkToolbarGrouping {
        PathGrouped(false) {
            @Override
            public MenuStructurizer createMenuStructurizer() {
                MenuStructurizer m = new MenuPathStructurizer(false);
                m.setFolderIcon(Icons.IR_BOOKMARK_FOLDER.icon);
                return m;
            }
        },
        NormalGrouped(false) {

            @Override
            public MenuStructurizer createMenuStructurizer() {
                MenuStructurizer m = new MenuABCStructurizer(false);
                m.setFolderIcon(Icons.IR_BOOKMARK_FOLDER.icon);
                return m;
            }
        },
        ABCGrouped(true) {

            @Override
            public MenuStructurizer createMenuStructurizer() {
                MenuStructurizer m = new MenuDirectABCStructurizer(true);
                m.setFolderIcon(Icons.IR_BOOKMARK_FOLDER.icon);
                return m;
            }
        };
;
        private boolean useLargeText;

        private BookMarkToolbarGrouping(boolean useLargeText) {
            this.useLargeText = useLargeText;
        }

        abstract public MenuStructurizer createMenuStructurizer();

        public boolean isUseLargeText() {
            return useLargeText;
        }
    }
    /** default is path grouped. */
    private BookMarkToolbarGrouping bmGrouping = BookMarkToolbarGrouping.PathGrouped;

    /**
     * Build the bookmarks menu and the bookmark toolbar:
     */
    public void updateBookmark(final JMenu jMenuBookmarks, final JMenuBar bookmarkToolbar) {

        jMenuBookmarks.removeAll();
        jMenuBookmarks.add(new AddBookmark(controller));
        // zwei separatoren zur besseren abhebung:
        jMenuBookmarks.addSeparator();
        jMenuBookmarks.addSeparator();


        MenuPathStructurizer structurizer = new MenuPathStructurizer(true);
        structurizer.setFolderIcon(Icons.IR_BOOKMARK_FOLDER.icon);

        List<BookMark> bmList =  MainApplicationController.getInstance().getBookmarkList();
        for (final BookMark bm : bmList) {
            JMenuItem item = createBookMarkMenuItem(bm, true);
            structurizer.add(item, bm.getName(), bm.getMenuPath());
        }

        structurizer.structurize(jMenuBookmarks, 10);

        // build the bookmarktoolbar list up:
        bookmarkToolbar.removeAll();
        final FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        bookmarkToolbar.setLayout(new ResizeLayout(restAsMenuCreator));

        // first create the switch-menu for structure-changes:
        JMenu switcher = new JMenu(localeBundle.getString("SWITCH"));

        switcher.add(new AbstractAction(localeBundle.getString("PATH")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                bmGrouping = BookMarkToolbarGrouping.PathGrouped;
                updateBookmark(jMenuBookmarks, bookmarkToolbar);
            }
        });
        switcher.add(new AbstractAction(localeBundle.getString("GROUPED")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                bmGrouping = BookMarkToolbarGrouping.NormalGrouped;
                updateBookmark(jMenuBookmarks, bookmarkToolbar);
            }
        });
        switcher.add(new AbstractAction(localeBundle.getString("ABC")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                bmGrouping = BookMarkToolbarGrouping.ABCGrouped;
                updateBookmark(jMenuBookmarks, bookmarkToolbar);
            }
        });
        bookmarkToolbar.add(switcher);

        MenuStructurizer tstructurizer = bmGrouping.createMenuStructurizer();
        tstructurizer.setFolderIcon(Icons.IR_BOOKMARK_FOLDER.icon);
        for (final BookMark bm : bmList) {
            JMenuItem item = createBookMarkMenuItem(bm, bmGrouping.isUseLargeText());
            tstructurizer.add(item, bm.getName(), bm.getMenuPath());
        }
        tstructurizer.structurize(bookmarkToolbar, 20);
        bookmarkToolbar.setBorderPainted(false);
        bookmarkToolbar.repaint();
    }

    private JMenuItem createBookMarkMenuItem(final BookMark bm, boolean largeText) {
        JMenuItem item = new JMenuItem();
        item.setIcon(Icons.IR_BOOKMARK.icon);
        if (largeText) {
            item.setText(TextRepresentations.getTextForBookmark(bm));
        } else {
            item.setText(TextRepresentations.getShortTextForBookmark(bm));
        }
        item.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showBookMark(bm);
            }
        });
        return item;
    }
}
