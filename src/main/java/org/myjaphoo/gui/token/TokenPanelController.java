/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.token;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.myjaphoo.model.db.Token;

import javax.swing.*;

/**
 * Controller interface für das TokenPanel
 * @author mla
 */
public interface TokenPanelController {

    public void setCurrentToken(Token token);

    /**
     * erzeuge einen Transferhandler für Dnd zw. Token u. Thumb-panels.
     * Wenn null zurückgeliefert wird, dann wird kein DnD für diese Komponente aktiviert.
     * @return
     */
    public TransferHandler createTransferHandler(TokenTree tokenTree);

    /**
     * Erzeuge popup menü für die komponente. Oder null, wenn kein popup aktiv sein soll.
     */
    public JPopupMenu createTokenTreePopupMenu();

    public AbstractTreeTableModel createTokenTreeModel();

    public boolean isFlatView();

    public void setFlatView(boolean newVal);

    public void doubleClicked();

    void onTokenSelected(Token token);
}
