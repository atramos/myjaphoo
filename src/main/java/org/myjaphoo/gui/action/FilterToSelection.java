/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.xml.bind.JAXBException;
import org.myjaphoo.MovieNode;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.util.Filtering;

/**
 * Sets the filter to filter exactly the current selected files.
 * @author lang
 */
public class FilterToSelection extends AbstractWankmanAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/FilterToSelection");

    public FilterToSelection(MyjaphooController controller, ViewContext context) {
        super(controller, localeBundle.getString("FILTER TO CURRENT SELECTED FILES"), null, context);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws IOException, JAXBException {
        // erst die selektierten holen, bevor sie durch den dialog evtl. wieder
        // deselektiert werden
        final List<MovieNode> selMovies = context.getSelMovies();

        String expr = createFilterExpression(selMovies);

        controller.getFilter().setFilterPattern(expr);
        controller.getView().updateMovieAndThumbViews();

    }

    private String createFilterExpression(List<MovieNode> selMovies) {
        ArrayList<MovieEntry> entries = Filtering.nodes2Entries(selMovies);
        StringBuilder b = new StringBuilder();
        for (MovieEntry entry : entries) {
            if (entry.getChecksumCRC32() != null) {
                if (b.length() > 0) {
                    b.append(" | "); //NOI18N
                } else {
                    b.append("checksum = "); //NOI18N
                }
                b.append(Integer.toString(entry.getChecksumCRC32().intValue()));
            }
        }
        return b.toString();
    }
}
