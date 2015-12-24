/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.myjaphoo.MyjaphooApp;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.grouping.GroupingDim;

import javax.xml.bind.JAXBException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Öffnet einen neuen View mit einem anfänglich gesetzten Filter.
 *
 * @author lang
 */
public class OpenViewWithFilter extends AbstractWankmanAction implements DisplayAsLastUsedActions {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/OpenViewWithFilter");

    private String filterExpression;
    private List<GroupingDim> hierarchy;

    public OpenViewWithFilter(MyjaphooController controller, String filterExpression) {
        super(controller, MessageFormat.format(localeBundle.getString("OPEN VIEW WITH"), filterExpression), null, ViewContext.EMPTYCONTEXT);
        this.filterExpression = filterExpression;
    }

    public OpenViewWithFilter(MyjaphooController controller, String filterExpression, List<GroupingDim> hierarchy) {
        super(controller, MessageFormat.format(localeBundle.getString("OPEN VIEW WITH"), filterExpression), null, ViewContext.EMPTYCONTEXT);
        this.filterExpression = filterExpression;
        this.hierarchy = hierarchy;
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) throws IOException, JAXBException {
        if (hierarchy == null) {
            MyjaphooApp.getApplication().startNewView(filterExpression);
        } else {
            MyjaphooApp.getApplication().startNewView(filterExpression, hierarchy);
        }
    }
}
