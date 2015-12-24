/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.token;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.util.Helper;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.db.TokenType;

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
public class TokenTree extends JXTreeTable {
    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/movietree/resources/MovieTree");
    
    private Icon movieIcon = new ImageIcon("org/myjaphoo/gui/icons/Movie16.gif");
    private TokenPanelController controller;
    private boolean showThumbs = false;

    public TokenTree(final TokenPanelController controller) {
        this.controller = controller;

        setDropMode(DropMode.ON);
        setDragEnabled(true);
        setEditable(true);
        TokenTreeCellRenderer renderer = new TokenTreeCellRenderer(this);
        setTreeCellRenderer(renderer);
        JComboBox typeEditcombobox = new JComboBox(new EnumComboBoxModel(TokenType.class));
        setDefaultEditor(TokenType.class, new DefaultCellEditor(typeEditcombobox));
        setColumnControlVisible(true);
        setHorizontalScrollEnabled(true);
        getActionMap().put("column.toggleOnlyCategory", new AbstractActionExt(localeBundle.getString("ONLY CATEGORY ON/OFF")) {

            boolean active = true;

            @Override
            public void actionPerformed(ActionEvent e) {

                active = !active;
                getColumnExt(AbstractTokenTreeModel.COLUMNS[0]).setVisible(true);
                getColumnExt(TokenTreeModel.COLUMNS[1]).setVisible(active);
                getColumnExt(TokenTreeModel.COLUMNS[2]).setVisible(active);
            }
        });

        getActionMap().put("column.toggleThumbPreview", new AbstractActionExt(localeBundle.getString("SHOW THUMBS ON/OF")) {

            @Override
            public void actionPerformed(ActionEvent e) {
                TokenTree.this.showThumbs = !TokenTree.this.isShowThumbs();
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
                Token token= TokenTreeCellRenderer.getToken(value);
                controller.onTokenSelected(token);
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
        getColumnExt(TokenTreeModel.COLUMNS[0]).setVisible(true);
        getColumnExt(TokenTreeModel.COLUMNS[1]).setVisible(false);
        getColumnExt(TokenTreeModel.COLUMNS[2]).setVisible(false);
        setRowHeight(MyjaphooAppPrefs.PRF_THUMBSIZE.getVal());
        showThumbs = true;
    }

    public void refreshTree() {
        TreeTableModel oldModel = getTreeTableModel();
        if (oldModel != null && oldModel instanceof TokenTreeModel) {
            ((TokenTreeModel)getTreeTableModel()).dispose();
        }
        final AbstractTreeTableModel tokenTreeModel = getController().createTokenTreeModel();
        setTreeTableModel(tokenTreeModel);
        initColumnWidth();
    }

    private void initColumnWidth() {
        // set prototype widths for the columns:
        for (int i = 0; i < TokenTreeModel.COLUMNS.length; i++) {
            int width = i == 0 ? 200 : 40;
            getColumnExt(TokenTreeModel.COLUMNS[i]).setPreferredWidth(width);
        }

    }

    protected void selectToken(Token tok) {
        // wir mÃ¼ssen erst das objekt zu diesem token im modell finden;
        // es kann ein anderes sein, durch attachen an die db (obwohl es eigentlich die identische entity in der db ist):
        // das machen wir Ã¼ber den tokencache, der in diesem fall aktuell sein muss (sprich die gleichen objekte
        // beinhaltent, aus denen das tokentree model geildet wurde)
        Token aktuellesToken = CacheManager.getCacheActor().getImmutableModel().getTokenSet().find(tok);

        if (aktuellesToken != null) {
            final TreePath treePath = ((TokenTreeModel) getTreeTableModel()).findTreePathForWrappedObject(aktuellesToken);

            getTreeSelectionModel().setSelectionPath(treePath);


            scrollPathToVisible(treePath);
            setExpandsSelectedPaths(true);
            setScrollsOnExpand(true);
        }
    }

    /**
     * @return the controller
     */
    public TokenPanelController getController() {
        return controller;
    }
}
