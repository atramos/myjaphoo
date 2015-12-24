package org.myjaphoo.gui.filtereditor;


import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.mlsoft.swing.JPopupMenuButton;
import org.myjaphoo.model.filterparser.idents.FixIdentifier;
import org.myjaphoo.model.filterparser.idents.Identifiers;
import org.myjaphoo.model.filterparser.operator.AbstractBoolResultOperator;
import org.myjaphoo.model.filterparser.operator.AbstractOperator;
import org.myjaphoo.model.filterparser.operator.Operators;

import javax.accessibility.Accessible;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * IdentifierComboboxFilterChooser
 *
 * @author lang
 * @version $Id$
 */
public class IdentifierComboboxFilterChooser extends ComboBoxFilterChooser {


    private FixIdentifier ident;
    private AbstractOperator operator = Operators.LIKE;

    private List<AbstractOperator> operatorChooseList = new ArrayList<>();
    JPopupMenuButton operatorChooser = new JPopupMenuButton();
    private final JPopupMenu opPopupMenu = new JPopupMenu();

    public IdentifierComboboxFilterChooser(final PreFilterToolbar preFilterToolbar, FixIdentifier ident, String title, List list, Object value) {
        this(preFilterToolbar, ident, title, list, value, Operators.LIKE);
    }

    public IdentifierComboboxFilterChooser(final PreFilterToolbar preFilterToolbar, FixIdentifier ident, String title, List list, Object value, AbstractOperator predefinedOp) {
        super(preFilterToolbar, title, list, value);
        this.ident = ident;
        this.operator = predefinedOp;

        for (AbstractOperator op : AbstractOperator.getList()) {
            if (op instanceof AbstractBoolResultOperator && op.worksWithTypes().contains(ident.getType())) {
                operatorChooseList.add(op);
            }
        }

        add(operatorChooser, 1);
        operatorChooser.setText(operator.getName());
        operatorChooser.setPopupmenu(opPopupMenu);
        for (final AbstractOperator op : operatorChooseList) {
            opPopupMenu.add(new AbstractAction(op.getName()) {

                @Override
                public void actionPerformed(ActionEvent e) {
                    operator = op;
                    operatorChooser.setText(op.getName());
                    preFilterToolbar.getFilterEditorPanel().updateViewWithCurrentFilter();
                }
            });
        }

        if (ident == Identifiers.TAG || ident == Identifiers.TOKEN) {
            //comboBox.setPreferredSize(new Dimension(20*24, 24));

            comboBox.setPrototypeDisplayValue("abcdefghijklmnopqrst");
            // need to set the prototypedisplayvalue also to the used jlist. therefore
            // we have to use this workaround. see
            // http://stackoverflow.com/questions/5896282/how-to-prevent-jcombobox-from-becoming-unresponsive-when-using-a-custom-listcell?rq=1
            Accessible a = comboBox.getUI().getAccessibleChild(comboBox, 0);
            if (a instanceof javax.swing.plaf.basic.ComboPopup) {
                JList popupList = ((javax.swing.plaf.basic.ComboPopup) a).getList();
                // route the comboBox' prototype to the list
                // should happen in BasicComboxBoxUI
                popupList.setPrototypeCellValue(comboBox.getPrototypeDisplayValue());
            }
            comboBox.setRenderer(new TagNameComboboxRenderer());
        }

    }

    @Override
    public String createFilterExpression(Object selectedItem) {
        if (selectedItem != null && !StringUtils.isEmpty(selectedItem.toString())) {
            String escapedValue = StringEscapeUtils.escapeJava(selectedItem.toString());
            return " " + ident.getName() + " " + operator.getName() + " \"" + escapedValue + "\" ";
        } else {
            return null;
        }
    }
}
