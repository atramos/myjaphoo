/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.tokenselection;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.mlsoft.eventbus.GlobalBus;
import org.mlsoft.eventbus.Subscribe;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.AddNewTokenActionInTokenTree;
import org.myjaphoo.gui.token.AbstractTokenPanelController;
import org.myjaphoo.gui.token.TokenTree;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.cache.ChangeSet;
import org.myjaphoo.model.db.Token;

import javax.swing.*;

/**
 *
 * @author mla
 */
public class TokenSelectionController extends AbstractTokenPanelController {

    private String typedText;
    private Token token;
    private TokenSelectionDialog dialog;
    private MyjaphooController mainController;

    public TokenSelectionController(MyjaphooController mainController, TokenSelectionDialog dialog) {
        this.dialog = dialog;
        this.mainController = mainController;
        GlobalBus.bus.register(this);
    }

    @Subscribe(onETD = true)
    public void databaseCacheChanged(ChangeSet e) {
        if (e.getTokenSet().size() > 0) {
            TokenSelectionController.this.dialog.getTokenPanel().refreshView();
        }
    }

    @Override
    public void setCurrentToken(Token token) {
        this.token = token;
    }

    @Override
    public TransferHandler createTransferHandler(TokenTree tokenTree) {
        return null;
    }

    @Override
    public JPopupMenu createTokenTreePopupMenu() {
        JPopupMenu m = new JPopupMenu();
        m.add(new AddNewTokenActionInTokenTree(mainController));
        return m;
    }

    void setTypedText(String typedText) {
        this.typedText = typedText;
    }

    @Override
    public AbstractTreeTableModel createTokenTreeModel() {
        final AbstractTreeTableModel tokenTreeModel = new FilteredTokenTreeModel(CacheManager.getCacheActor().getImmutableModel().getRootToken(), isFlatView(), typedText);
        return tokenTreeModel;
    }

    /**
     * @return the token
     */
    public Token getToken() {
        return token;
    }

    @Override
    public void doubleClicked() {
        dialog.okAndClose();
    }

    @Override
    public void onTokenSelected(Token token) {
        // do nothing
    }
}
