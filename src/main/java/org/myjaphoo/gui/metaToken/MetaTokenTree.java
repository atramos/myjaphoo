/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.metaToken;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.util.Helper;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.MetaToken;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

/**
 *
 * @author mla
 */
public class MetaTokenTree extends JXTreeTable {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/metaToken/resources/MetaTokenTree");
    private Icon movieIcon = new ImageIcon("org/myjaphoo/gui/icons/Movie16.gif"); //NOI18N
    private MetaTokenPanelController controller;
    private boolean showThumbs = false;

    public MetaTokenTree(final MetaTokenPanelController controller) {
        this.controller = controller;

        setDropMode(DropMode.ON);
        setDragEnabled(true);
        setEditable(true);
        MetaTokenTreeCellRenderer renderer = new MetaTokenTreeCellRenderer(this);
        setTreeCellRenderer(renderer);
        setColumnControlVisible(true);
        setHorizontalScrollEnabled(true);
        getActionMap().put("column.toggleOnlyCategory", //NOI18N
                new AbstractActionExt(localeBundle.getString("ONLY CATEGORY ON/OFF")) {

            boolean active = true;

            @Override
            public void actionPerformed(ActionEvent e) {

                active = !active;
                getColumnExt(MetaTokenTreeModel.COLUMNS[0]).setVisible(true);
                getColumnExt(MetaTokenTreeModel.COLUMNS[1]).setVisible(active);
            }
        });

        getActionMap().put("column.toggleThumbPreview", //NOI18N
                new AbstractActionExt(localeBundle.getString("SHOW THUMBS ON/OFF")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                MetaTokenTree.this.showThumbs = !MetaTokenTree.this.isShowThumbs();
            }
        });
        Helper.initHeightMenusForJXTreeTable(this);

        TransferHandler transferHandler = controller.createTransferHandler(this);

        if (transferHandler != null) {
            setTransferHandler(transferHandler);
        }

        setRolloverEnabled(true);
        addHighlighter(Commons.ROLLOVER_ROW_HIGHLIGHTER);
        setOverwriteRendererIcons(true);
        setLeafIcon(movieIcon);

        addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Object value = e.getPath().getLastPathComponent();
                MetaToken token= MetaTokenTreeCellRenderer.getToken(value);
                controller.onMetaTokenSelected(token);
            }
        });
    }

    /**
     * @return the showThumbs
     */
    public boolean isShowThumbs() {
        return showThumbs;
    }

    public void setShowPreviewBigMode() {
        getColumnExt(MetaTokenTreeModel.COLUMNS[0]).setVisible(true);
        getColumnExt(MetaTokenTreeModel.COLUMNS[1]).setVisible(false);
        setRowHeight(MyjaphooAppPrefs.PRF_THUMBSIZE.getVal());
        showThumbs = true;
    }

    public void refreshTree() {
        final AbstractTreeTableModel tokenTreeModel = controller.createMetaTokenTreeModel();
        TreeTableModel oldModel = getTreeTableModel();
        if (oldModel != null && oldModel instanceof MetaTokenTreeModel) {
            ((MetaTokenTreeModel) oldModel).dispose();
        }
        setTreeTableModel(tokenTreeModel);
        initColumnWidth();
    }

    private void initColumnWidth() {
        // set prototype widths for the columns:
        for (int i = 0; i < MetaTokenTreeModel.COLUMNS.length; i++) {
            int width = i == 0 ? 200 : 40;
            getColumnExt(MetaTokenTreeModel.COLUMNS[i]).setPreferredWidth(width);
        }

    }

    protected void selectToken(MetaToken tok) {
        // wir mÃ¼ssen erst das objekt zu diesem token im modell finden;
        // es kann ein anderes sein, durch attachen an die db (obwohl es eigentlich die identische entity in der db ist):
        // das machen wir Ã¼ber den tokencache, der in diesem fall aktuell sein muss (sprich die gleichen objekte
        // beinhaltent, aus denen das tokentree model geildet wurde)
        MetaToken aktuellesToken = CacheManager.getCacheActor().getImmutableModel().getMetaTokenSet().find(tok);

        if (aktuellesToken != null) {
            final TreePath treePath = ((MetaTokenTreeModel) getTreeTableModel()).findTreePathForWrappedObject(aktuellesToken);

            getTreeSelectionModel().setSelectionPath(treePath);


            scrollPathToVisible(treePath);
            setExpandsSelectedPaths(true);
            setScrollsOnExpand(true);
        }
    }
}
