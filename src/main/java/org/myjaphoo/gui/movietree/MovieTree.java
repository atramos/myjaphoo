/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.ListSelectionModel;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.myjaphoo.MovieTreeModel;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.util.Helper;

/**
 * Der movie tree.
 * @author mla
 */
public class MovieTree extends JXTreeTable {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/movietree/resources/MovieTree");

    private MoviePanelController controller;

    public MovieTree(final MoviePanelController controller) {
        this.controller = controller;
        setRolloverEnabled(true);
        addHighlighter(Commons.ROLLOVER_ROW_HIGHLIGHTER);
        addHighlighter(Commons.DBCOMPARE_HIGHLIGHTER);
        setColumnControlVisible(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setHorizontalScrollEnabled(true);
        setLargeModel(true);
        setRootVisible(true);

        getActionMap().put("column.toggleOnlyCategory",  //NOI18N
                new AbstractActionExt(localeBundle.getString("ONLY CATEGORY ON/OFF")) {

            boolean active = true;

            @Override
            public void actionPerformed(ActionEvent e) {

                active = !active;
                getColumnExt(MovieTreeModel.COLUMNS[0]).setVisible(true);
                getColumnExt(MovieTreeModel.COLUMNS[1]).setVisible(active);
                getColumnExt(MovieTreeModel.COLUMNS[2]).setVisible(active);
            }
        });

        getActionMap().put("column.toggleThumbPreview",  //NOI18N
                new AbstractActionExt(localeBundle.getString("SHOW THUMBS ON/OF")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setPreviewThumbsInMovieTree(!controller.isPreviewThumbsInMovieTree());
            }
        });
        Helper.initHeightMenusForJXTreeTable(this);

    }
}
