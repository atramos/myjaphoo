/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.token;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.AddTokenAction;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.gui.action.metatoken.MetaTokenAssignment;
import org.myjaphoo.gui.metaToken.MetaTokenTransferHandler;
import org.myjaphoo.gui.thumbtable.ThumbTable;
import org.myjaphoo.gui.util.WrappedNode;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.Token;

/**
 *
 * @author mla
 */
public class TokenTransferHandler extends TransferHandler {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/token/resources/TokenTransferHandler");

    private TokenTree tokenTree;
    private MyjaphooController controller;

    public TokenTransferHandler(TokenTree tokenTree, MyjaphooController controller) {
        this.tokenTree = tokenTree;
        this.controller = controller;
    }
    /** flavor for token dnd. */
    public static final DataFlavor TOKENFLAVOR = new DataFlavor(Token.class,
            "Wankman Token"); //NOI18N

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

        // in the flat view we do not support dnd from token to token,
        // as this is for changing the treestructure, which makes no sens in flat view:
        if (tokenTree.getController().isFlatView() && info.isDataFlavorSupported(TOKENFLAVOR)) {
            return false;
        }

        // we only import Strings
        if (!info.isDataFlavorSupported(TOKENFLAVOR)
                && !info.isDataFlavorSupported(ThumbTable.SELMOVIESFLAVOR)
                && !info.isDataFlavorSupported(MetaTokenTransferHandler.METATOKENFLAVOR)) {
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

        if (info.isDataFlavorSupported(TOKENFLAVOR)) {
            return handleTokenMovement(info);
        } else if (info.isDataFlavorSupported(ThumbTable.SELMOVIESFLAVOR)) {
            return handleMovieMovement(info);
        } else if (info.isDataFlavorSupported(MetaTokenTransferHandler.METATOKENFLAVOR)) {
            return handleMetaTokenMovement(info);
        } else {
            return false;
        }


    }

    class TransferableImpl implements Transferable {

        private Token token = null;

        public TransferableImpl() {
            TreePath path = tokenTree.getTreeSelectionModel().getSelectionPath();
            if (path != null) {
                WrappedNode<Token> node = (WrappedNode<Token>) path.getLastPathComponent();
                token = node.getRef();
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{TOKENFLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(TOKENFLAVOR);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) {
            return token;
        }
    }

    private boolean handleMetaTokenMovement(TransferHandler.TransferSupport info) {
        // fetch the drop location
        final Token token = fetchTokenFromDropLocation(info);
        if (token == null) {
            return false;
        }
        final MetaToken metaToken;
        try {
            metaToken = (MetaToken) info.getTransferable().getTransferData(MetaTokenTransferHandler.METATOKENFLAVOR);
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        if (metaToken == null) {
            return false;
        }
        MetaTokenAssignment action = new MetaTokenAssignment(controller, token, metaToken);
        action.actionPerformed(new ActionEvent(this, 0, localeBundle.getString("DROP METATOKEN TO TOKEN")));
        return true;

    }

    private boolean handleMovieMovement(TransferHandler.TransferSupport info) {
        // fetch the drop location
        final Token token = fetchTokenFromDropLocation(info);
        if (token == null) {
            return false;
        }
        final ArrayList<MovieNode> nodes;
        try {
            nodes = (ArrayList<MovieNode>) info.getTransferable().getTransferData(ThumbTable.SELMOVIESFLAVOR);
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        if (nodes == null) {
            return false;
        }

        AddTokenAction action = new AddTokenAction(controller, token, null, new ViewContext(ViewContext.asNodes(nodes)));
        action.actionPerformed(new ActionEvent(this, 0, localeBundle.getString("DROP MOVIES TO TOKEN")));
        return true;

    }

    private Token fetchTokenFromDropLocation(TransferHandler.TransferSupport info) {
        // fetch the drop location
        JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
        int row = dl.getRow();
        TreePath path = tokenTree.getPathForRow(row);
        // fetch the path and child index from the drop location
        WrappedNode<Token> node = (WrappedNode<Token>) path.getLastPathComponent();
        final Token token = node.getRef();
        return token;
    }

    private boolean handleTokenMovement(TransferHandler.TransferSupport info) {

        // fetch the data and bail if this fails
        final Token token2Move;
        try {
            token2Move = (Token) info.getTransferable().getTransferData(TOKENFLAVOR);
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        if (token2Move == null) {
            return false;
        }

        final Token oldParent = token2Move.getParent();

        final Token tokenParent = fetchTokenFromDropLocation(info);
        if (token2Move == tokenParent) {
            return false;
        }
        if (tokenParent == oldParent) {
            return false;
        }

        controller.moveTokens(tokenParent, token2Move);
        controller.getView().addUndoableEdit(new AbstractUndoableEdit() {

            @Override
            public void undo() throws CannotUndoException {
                super.undo();
                controller.moveTokens(oldParent, token2Move);
                tokenTree.refreshTree();
                tokenTree.selectToken(token2Move);
            }

            @Override
            public void redo() throws CannotRedoException {
                super.redo();
                controller.moveTokens(tokenParent, token2Move);
                tokenTree.refreshTree();
                tokenTree.selectToken(token2Move);
            }

            @Override
            public String getPresentationName() {
                return MessageFormat.format(localeBundle.getString("MOVE TOKEN"), token2Move.getName(), tokenParent.getName());
            }
        });


        tokenTree.refreshTree();
        tokenTree.selectToken(token2Move);
        return true;
    }
}
