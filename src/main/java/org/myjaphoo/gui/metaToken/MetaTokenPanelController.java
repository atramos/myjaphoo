/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.metaToken;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.myjaphoo.model.db.MetaToken;

import javax.swing.*;

/**
 * Controller interface für das TokenPanel
 * @author mla
 */
public interface MetaTokenPanelController {

    /**
     * erzeuge einen Transferhandler für Dnd zw. Token u. Thumb-panels.
     * Wenn null zurückgeliefert wird, dann wird kein DnD für diese Komponente aktiviert.
     * @return
     */
    public TransferHandler createTransferHandler(MetaTokenTree tokenTree);

    /**
     * Erzeuge popup menü für die komponente. Oder null, wenn kein popup aktiv sein soll.
     */
    public JPopupMenu createTokenTreePopupMenu();

    public AbstractTreeTableModel createMetaTokenTreeModel();

    public void setCurrentMetaToken(MetaToken metaToken);

    public boolean isFlatView();

    public void setFlatView(boolean newVal);

    public void doubleClicked();

    void onMetaTokenSelected(MetaToken token);
}
