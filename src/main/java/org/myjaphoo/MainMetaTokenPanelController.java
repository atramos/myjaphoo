/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.myjaphoo.gui.action.metatoken.AddNewMetaToken;
import org.myjaphoo.gui.action.metatoken.RemoveMetaToken;
import org.myjaphoo.gui.action.metatoken.RemoveTokenRelationFromMetaToken;
import org.myjaphoo.gui.action.scriptactions.MetaTagScriptContextAction;
import org.myjaphoo.gui.action.scriptactions.MetatagContextAction;
import org.myjaphoo.gui.metaToken.MetaTokenPanelController;
import org.myjaphoo.gui.metaToken.MetaTokenTransferHandler;
import org.myjaphoo.gui.metaToken.MetaTokenTree;
import org.myjaphoo.gui.metaToken.MetaTokenTreeModel;
import org.myjaphoo.gui.util.TokenMenuCreation;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.registry.ComponentRegistry;

import javax.swing.*;
import java.util.*;

/**
 * @author mla
 */
public class MainMetaTokenPanelController implements MetaTokenPanelController {

    private MyjaphooController controller;
    private boolean flatView = false;

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/resources/MainMetaTokenPanelController");

    public MainMetaTokenPanelController(MyjaphooController controller) {
        this.controller = controller;
    }

    @Override
    public void setCurrentMetaToken(MetaToken token) {
        controller.getFilter().setCurrentMetaToken(token);
    }

    @Override
    public TransferHandler createTransferHandler(MetaTokenTree tokenTree) {
        return new MetaTokenTransferHandler(tokenTree, controller);
    }

    @Override
    public JPopupMenu createTokenTreePopupMenu() {
        JPopupMenu m = new JPopupMenu();
        MetaToken token = controller.getFilter().getCurrentMetaToken();
        if (token != null) {
            JMenu openViewMenu = new JMenu(localeBundle.getString("OPEN VIEW"));
            m.add(openViewMenu);
            openViewMenu.add(TokenMenuCreation.createMetaTagOV(controller, token));
        }

        m.add(new AddNewMetaToken(controller));
        m.addSeparator();
        m.add(new RemoveTokenRelationFromMetaToken(controller));
        m.add(new RemoveMetaToken(controller));

        Collection<MetatagContextAction> entries = ComponentRegistry.registry.getEntryCollection(MetatagContextAction.class);
        if (entries.size() > 0) {
            m.addSeparator();
            for (MetatagContextAction entry : entries) {
                MetaTagScriptContextAction action = new MetaTagScriptContextAction(controller, null, entry, Arrays.asList(token));
                m.add(action);
            }
        }

        return m;
    }

    /**
     * @return the flatView
     */
    @Override
    public boolean isFlatView() {
        return flatView;
    }

    /**
     * @param flatView the flatView to set
     */
    @Override
    public void setFlatView(boolean flatView) {
        this.flatView = flatView;
    }

    @Override
    public AbstractTreeTableModel createMetaTokenTreeModel() {
        final MetaTokenTreeModel tokenTreeModel = new MetaTokenTreeModel(CacheManager.getCacheActor().getImmutableModel().getRootMetaToken(), isFlatView());
        return tokenTreeModel;
    }

    @Override
    public void doubleClicked() {
        // do not react on double click
    }

    @Override
    public void onMetaTokenSelected(MetaToken token) {
        controller.getView().getPropertiesPanel().currSelectionChanged(token, null);
    }
}
