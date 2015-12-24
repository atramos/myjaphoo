/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action.dbcompare;

import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.MyjaphooDBPreferences;
import org.myjaphoo.gui.action.AbstractSelectedMoviesContextAction;
import org.myjaphoo.gui.action.ViewContext;

import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Integrates the changes of the selected diff nodes into the current database,
 * so that for those nodes, the data in the current and the compare database are identical.
 * @author lang
 */
public class IntegrateChangesFromComparisonDatabase extends AbstractSelectedMoviesContextAction {

    private static String REALLY_TXT = "Really integrate changes from differences? This could include adding or removing of data in the current database!";

    private static String NOT_ALLOWED = "The preferences do not allow to make changes to the database!";

    public IntegrateChangesFromComparisonDatabase(MyjaphooController controller, ViewContext context) {
        super(controller, "Integrate changes from Comparison DB", context, null);
    }

    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        if (MyjaphooDBPreferences.PRF_FO_REMOVING_ALLOWED.getVal()) {
            List<MovieNode> movies = confirmSelectionOfNodes(REALLY_TXT, context);
            if (movies != null) {
                getController().integrateComparisonDifferences(context.getSelDiffNodes());
                //controller.createChangeLog2(ChangeLogType.REMOVEENTRIES, "remove entries from db", movies); //NOI18N
            }
        } else {
            controller.message(NOT_ALLOWED);
        }
    }
}
