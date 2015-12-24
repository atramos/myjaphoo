/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.metaToken;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.metatoken.MetaTokenAssignment;
import org.myjaphoo.gui.token.TokenTransferHandler;
import org.myjaphoo.gui.util.WrappedNode;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.Token;

/**
 *
 * @author mla
 */
public class MetaTokenTransferHandler extends TransferHandler {

    private MetaTokenTree metaTokenTree;
    private MyjaphooController controller;

    public MetaTokenTransferHandler(MetaTokenTree metaTokenTree, MyjaphooController controller) {
        this.metaTokenTree = metaTokenTree;
        this.controller = controller;
    }
    /** flavor for token dnd. */
    public static final DataFlavor METATOKENFLAVOR = new DataFlavor(MetaToken.class,
            "Wankman MetaToken"); //NOI18N

    @Override
    protected Transferable createTransferable(JComponent c) {
        return new TransferableImpl();
    }

    @Override
    public int getSourceActions(JComponent c) {
        return this.MOVE;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        // for the demo, we'll only support drops (not clipboard paste)
        if (!info.isDrop()) {
            return false;
        }

        info.setShowDropLocation(true);

        // we only import Strings
        if (!info.isDataFlavorSupported(METATOKENFLAVOR)
                && !info.isDataFlavorSupported(TokenTransferHandler.TOKENFLAVOR)) {
            return false;
        }

        // fetch the drop location
        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();

        if (dl.getColumn() >= 0 && dl.getRow() >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        // if we can't handle the import, say so
        if (!canImport(info)) {
            return false;
        }

        if (info.isDataFlavorSupported(METATOKENFLAVOR)) {
            return handleTokenMovement(info);
        } else if (info.isDataFlavorSupported(TokenTransferHandler.TOKENFLAVOR)) {
            return handleMetaTokenAssignment(info);
        } else {
            return false;
        }


    }

    private boolean handleMetaTokenAssignment(TransferSupport info) {
        // fetch the drop location
        final MetaToken metaToken = fetchTokenFromDropLocation(info);
        if (metaToken == null) {
            return false;
        }
        final Token token;
        try {
            token = (Token) info.getTransferable().getTransferData(TokenTransferHandler.TOKENFLAVOR);
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        if (token == null) {
            return false;
        }
        MetaTokenAssignment action = new MetaTokenAssignment(controller, token, metaToken);
        action.actionPerformed(new ActionEvent(this, 0, "drop metatag to tag")); //NOI18N
        return true;
    }

    class TransferableImpl implements Transferable {

        private MetaToken token = null;

        public TransferableImpl() {
            TreePath path = metaTokenTree.getTreeSelectionModel().getSelectionPath();
            if (path != null) {
                token = ((WrappedNode<MetaToken>) path.getLastPathComponent()).getRef();
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{METATOKENFLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(METATOKENFLAVOR);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) {
            return token;
        }
    }

    private MetaToken fetchTokenFromDropLocation(TransferHandler.TransferSupport info) {
        // fetch the drop location
        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        int row = dl.getRow();
        TreePath path = metaTokenTree.getPathForRow(row);
        // fetch the path and child index from the drop location
        final MetaToken token = ((WrappedNode<MetaToken>) path.getLastPathComponent()).getRef();
        return token;
    }

    private boolean handleTokenMovement(TransferHandler.TransferSupport info) {


        // fetch the data and bail if this fails
        final MetaToken token2Move;
        try {
            token2Move = (MetaToken) info.getTransferable().getTransferData(METATOKENFLAVOR);
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        if (token2Move == null) {
            return false;
        }

        final MetaToken oldParent = token2Move.getParent();

        final MetaToken tokenParent = fetchTokenFromDropLocation(info);
        if (token2Move == tokenParent) {
            return false;
        }
        if (tokenParent == oldParent) {
            return false;
        }
        if (token2Move.equals(tokenParent)) {
            return false;
        }

        controller.moveMetaTokens(tokenParent, token2Move);
        controller.getView().addUndoableEdit(new AbstractUndoableEdit() {

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                controller.moveMetaTokens(oldParent, token2Move);
                metaTokenTree.refreshTree();
                metaTokenTree.selectToken(token2Move);
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                controller.moveMetaTokens(tokenParent, token2Move);
                metaTokenTree.refreshTree();
                metaTokenTree.selectToken(token2Move);
            }

            @Override
            public String getPresentationName() {
                return "move meta tag " + token2Move.getName() + " under " + tokenParent.getName(); //NOI18N
            }
        });

        metaTokenTree.refreshTree();
        metaTokenTree.selectToken(token2Move);

        return true;
    }
}
