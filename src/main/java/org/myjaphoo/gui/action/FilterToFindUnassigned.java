/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.myjaphoo.MyjaphooController;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;
import org.myjaphoo.model.db.Token;

/**
 *
 * @author mla
 */
public class FilterToFindUnassigned extends AbstractWankmanAction {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/FilterToFindUnassigned");

    public FilterToFindUnassigned(MyjaphooController controller) {
        super(controller, localeBundle.getString("FILTER: FINDE MISSING ASSIGNEMENTS"), null);
    }

    @Override
    public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception {
        Token token = controller.getFilter().getCurrentToken();
        if (token == null) {
            return;
        }
        String expr = "not( tok = '" + token.getName() + "') and ("; //NOI18N
        expr += getPathExpr(token) + ")"; //NOI18N
        controller.getFilter().setFilterPattern(expr);
        controller.getView().updateMovieAndThumbViews();
    }

    private String getPathExpr(Token token) {
        StringBuilder b = new StringBuilder();
        String[] parts = StringUtils.split(token.getName(), " "); //NOI18N
        for (String part : parts) {
            if (b.length() > 0) {
                b.append(" or "); //NOI18N
            }
            b.append(" path like '" + part + "'"); //NOI18N
        }
        return b.toString();
    }
}
