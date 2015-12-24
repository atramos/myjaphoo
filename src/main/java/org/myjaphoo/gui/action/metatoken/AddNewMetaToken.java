/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action.metatoken;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.myjaphoo.gui.action.AbstractUndoAction;
import org.myjaphoo.gui.action.ViewContext;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.newtagdlg.NewTagDialog;
import org.myjaphoo.gui.newtagdlg.NewTagDialog.NewTagDlgResult;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.ChangeLogType;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.logic.MetaTokenJpaController;

/**
 *
 * @author lang
 */
public class AddNewMetaToken extends AbstractUndoAction {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/metatoken/resources/AddNewMetaToken");

    private MetaTokenJpaController tokenjpa = new MetaTokenJpaController();

    public AddNewMetaToken(MyjaphooController controller) {
        super(controller, localeBundle.getString("ADD NEW META-TAG"), null);
    }

    @Override
    public UndoableEdit runUndoAction(final MyjaphooController controller, ActionEvent e, ViewContext context) {

        final MetaToken parentPreselected = controller.getFilter().getCurrentMetaToken();
        List<MetaToken> parensList = CacheManager.getCacheActor().getImmutableModel().getMetaTokenSet().asList();
        final NewTagDlgResult result = NewTagDialog.newMetaTag(localeBundle.getString("ADD NAME FOR THE NEW META TAG"), parensList, parentPreselected);

        if (result != null) {
            MetaToken token = controller.createNewMetaToken(result.name, result.descr, result.parentMetaToken);
            controller.createChangeLog(ChangeLogType.NEWMETATOK, "new metatag " + token.getName(), null); //NOI18N
            return new AbstractUndoableEdit() {

                @Override
                public void undo() throws CannotUndoException {
                    super.undo();
                    MetaToken token = tokenjpa.findTokenByName(result.name);
                    controller.removeMetaToken(token);
                }

                @Override
                public void redo() throws CannotRedoException {
                    super.redo();
                    MetaToken token = controller.createNewMetaToken(result.name, result.descr, result.parentMetaToken);
                }

                @Override
                public String getPresentationName() {
                    return MessageFormat.format(localeBundle.getString("CREATE META TAG"), result.name);
                }
            };

        }
        return null;
    }
}
