/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BookMarkPanel.java
 *
 * Created on 05.03.2010, 12:42:25
 */
package org.myjaphoo.gui.bookmark;

import org.mlsoft.swing.annotation.ContextMenuAction;
import org.mlsoft.swing.annotation.ToolbarAction;
import org.mlsoft.swing.jtable.JXTableSupport;
import org.myjaphoo.MyjaphooApp;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.MyjaphooView;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.panel.AbstractEmbeddablePanel;
import org.myjaphoo.model.db.BookMark;
import org.myjaphoo.model.db.ChronicEntry;
import org.myjaphoo.model.db.DataView;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * @author mla
 */
public class BookMarkPanel extends AbstractEmbeddablePanel {
    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/bookmark/resources/BookMarkPanel");

    private MyjaphooController controller;
    private BookmarkTable bookmarkTable;
    private JXTableSupport<BookMark> tableSupport;


    /**
     * Creates new form BookMarkPanel
     */
    public BookMarkPanel(MyjaphooController controller) {
        this.controller = controller;
        bookmarkTable = new BookmarkTable(controller);
        bookmarkTable.refreshModel();
        tableSupport = new JXTableSupport<>(bookmarkTable, bookmarkTable.getBookMarkModel());
        tableSupport.setConfiguration(this, localeBundle);
        setLayout(new java.awt.BorderLayout());
        JPanel panel = tableSupport.createTableWithToolbar();
        add(panel, java.awt.BorderLayout.CENTER);
        setMinimumSize(new java.awt.Dimension(99, 25));
        setName("Form"); // NOI18N
    }

    @ToolbarAction(name = "jButtonRefresh.text", contextRelevant = false)
    @ContextMenuAction(name = "jButtonRefresh.text", contextRelevant = false)
    public void refreshButtonAction() {
        MainApplicationController.getInstance().reloadBookmarkList();
    }

    @ToolbarAction(name = "jButtonDelete.text")
    @ContextMenuAction(name = "jButtonDelete.text")
    public void deleteButtonAction(BookMark bm) {
        if (controller.confirm("Really delete bookmark '" + bm.getName() + "'?")) {
            controller.deleteBookMark(bm);
            ((BookmarkTable) bookmarkTable).refreshModel();
        }
    }

    @ToolbarAction(name = "jButtonShow.text")
    @ContextMenuAction(name = "jButtonShow.text")
    public void showButtonAction(BookMark bm) {
        controller.showBookMark(bm);
    }

    @ToolbarAction(name = "jButtonShowInNewView.text")
    @ContextMenuAction(name = "jButtonShowInNewView.text")
    public void showInNewViewButtonAction(BookMark bm) {
        MyjaphooApp.getApplication().show(new MyjaphooView(MyjaphooApp.getApplication(), bm.toChronic()));
    }

    @ToolbarAction(name = "jButtonUpdate.text")
    @ContextMenuAction(name = "jButtonUpdate.text")
    public void updateButtonAction(BookMark bm) {
        if (controller.confirm("Really update bookmark '" + bm.getName() + "'?")) {

            BookMark bmcurrent = controller.getFilter().createBookMarkFromCurrentChronic();
            bm.setView((DataView) bmcurrent.getView().clone());
            controller.updateBookMark(bm);
        }
    }

    public void onDoubleClickAction(BookMark node) {
        ChronicEntry entry = node.toChronic();
        controller.getFilter().popChronic(entry);
        controller.getView().updateMovieAndThumbViews();
    }



    @Override
    public String getTitle() {
        return "Bookmarks";
    }

    @Override
    public void refreshView() {
    }

    @Override
    public void clearView() {
    }
}
