/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.myjaphoo.gui.action.AddNewTokenActionInTokenTree;
import org.myjaphoo.gui.action.FilterToFindUnassigned;
import org.myjaphoo.gui.action.RemoveMetatokenRelationFromToken;
import org.myjaphoo.gui.action.RemoveToken;
import org.myjaphoo.gui.action.scriptactions.TagContextAction;
import org.myjaphoo.gui.action.scriptactions.TagScriptContextAction;
import org.myjaphoo.gui.token.AbstractTokenPanelController;
import org.myjaphoo.gui.token.TokenTransferHandler;
import org.myjaphoo.gui.token.TokenTree;
import org.myjaphoo.gui.token.TokenTreeModel;
import org.myjaphoo.gui.util.TokenMenuCreation;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.registry.ComponentRegistry;

import javax.swing.*;
import java.util.*;

/**
 * @author mla
 */
public class MainTokenPanelController extends AbstractTokenPanelController {

    private MyjaphooController controller;

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/resources/MainTokenPanelController");

    public MainTokenPanelController(MyjaphooController controller) {
        this.controller = controller;
    }

    @Override
    public void setCurrentToken(Token token) {
        controller.getFilter().setCurrentToken(token);
    }

    @Override
    public TransferHandler createTransferHandler(TokenTree tokenTree) {
        return new TokenTransferHandler(tokenTree, controller);
    }

    @Override
    public JPopupMenu createTokenTreePopupMenu() {
        JPopupMenu m = new JPopupMenu();
        Token token = controller.getFilter().getCurrentToken();
        List<Token> tags = Collections.emptyList();

        if (token != null) {
            JMenu openViewMenu = new JMenu(localeBundle.getString("OPEN VIEW"));
            m.add(openViewMenu);
            TokenMenuCreation.addMenusToFilterToTokens(controller, openViewMenu, Arrays.asList(token));

            // menus to assign to meta tags:
            TokenMenuCreation.addMetaTokenAssignments(controller, m, Arrays.asList(token));
            m.addSeparator();

            tags = Arrays.asList(token);
        }

        m.add(new FilterToFindUnassigned(controller));
        m.addSeparator();
        m.add(new AddNewTokenActionInTokenTree(controller));
        m.addSeparator();
        m.add(new RemoveToken(controller));
        m.addSeparator();
        m.add(new RemoveMetatokenRelationFromToken(controller));

        // add user defined actions from scripts:
        Set<Map.Entry<String, TagContextAction>> entries = ComponentRegistry.registry.getEntries(TagContextAction.class);
        if (entries.size() > 0) {
            m.addSeparator();
            for (Map.Entry<String, TagContextAction> entry : entries) {
                TagScriptContextAction action = new TagScriptContextAction(controller, null, entry.getValue(), tags);
                m.add(action);
            }
        }
        return m;
    }

    @Override
    public AbstractTreeTableModel createTokenTreeModel() {
        final TokenTreeModel tokenTreeModel = new TokenTreeModel(CacheManager.getCacheActor().getImmutableModel().getRootToken(), isFlatView());
        return tokenTreeModel;
    }

    @Override
    public void doubleClicked() {
        // do not react on double click
    }

    @Override
    public void onTokenSelected(Token token) {
        controller.getView().getPropertiesPanel().currSelectionChanged(token, null);
    }
}
