/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.mlsoft.swing.EventDispatchTools;
import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooApp;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.gui.removeElementsDialog.RemoveElementsDialog;
import org.myjaphoo.model.db.ChangeLogType;
import org.myjaphoo.model.db.Token;

import javax.xml.bind.JAXBException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.*;

/**
 * Removes token associaton (or relation) for certain tokens from a list of selected entries.
 *
 * @author lang
 */
public class RemoveTokenRelations extends AbstractWankmanAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/RemoveTokenRelations");

    public RemoveTokenRelations(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("REMOVE TAG RELATION FROM ENTRIE(S)"), Icons.IR_TAG_DEL.icon, context);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws IOException, JAXBException {
        // erst die selektierten holen, bevor sie durch den dialog evtl. wieder
        // deselektiert werden
        final List<MovieNode> selMovies = context.getSelMovies();
        Set<Token> tokens = new HashSet<Token>();
        for (MovieNode node : selMovies) {
            if (node.getMovieEntry() != null) {
                tokens.addAll(node.getMovieEntry().getTokens());
            }
        }
        ArrayList<Token> orderedList = new ArrayList<Token>(tokens);
        Collections.sort(orderedList);
        RemoveElementsDialog<Token> dlg = openDialog(controller, orderedList);
        if (dlg.isOk()) {
            List<Token> tokens2RemoveRelations = dlg.getCheckedElements();
            controller.removeTokenRelations(selMovies, tokens2RemoveRelations);
            controller.createChangeLog(ChangeLogType.REMOVETOKENRELATION, "remove tag relations: " + tokList(tokens2RemoveRelations), selMovies); //NOI18N
        }

    }

    private RemoveElementsDialog<Token> openDialog(final MyjaphooController controller, final ArrayList<Token> orderedList) {
        final RemoveElementsDialog<Token>[] dlg = new RemoveElementsDialog[1];

        EventDispatchTools.onEDTWait(new Runnable() {
            @Override
            public void run() {
                dlg[0] = new RemoveElementsDialog<>(controller.getView().getFrame(),
                        localeBundle.getString("SELECT WHICH TAG RELATIONS"),
                        localeBundle.getString("CHOOSE TAG RELATIONS TO REMOVE"), orderedList);
                MyjaphooApp.getApplication().show(dlg[0]);
            }
        });
        return dlg[0];
    }

    public static String tokList(List<Token> tokens2RemoveRelations) {
        StringBuilder b = new StringBuilder();
        for (Token token : tokens2RemoveRelations) {
            b.append(token.getName());
            b.append(";"); //NOI18N
        }
        return b.toString();
    }
}
