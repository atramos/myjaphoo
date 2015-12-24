package org.myjaphoo.gui.filtereditor;


import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.mlsoft.swing.JPopupMenuButton;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.operator.AbstractBoolResultOperator;
import org.myjaphoo.model.filterparser.operator.AbstractOperator;
import org.myjaphoo.model.filterparser.operator.Operators;

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
public class AttributeComboboxFilterChooser extends ComboBoxFilterChooser {

    private String attrFunctionName;
    private String attributeName;
    private AbstractOperator operator = Operators.LIKE;

    private List<AbstractOperator> operatorChooseList = new ArrayList<>();
    JPopupMenuButton operatorChooser = new JPopupMenuButton();
    private final JPopupMenu opPopupMenu = new JPopupMenu();

    public AttributeComboboxFilterChooser(final PreFilterToolbar preFilterToolbar, String attrFunctionName, String attributeName, List list, Object value) {
        this(preFilterToolbar, attrFunctionName, attributeName, list, value, Operators.LIKE);
    }

    public AttributeComboboxFilterChooser(final PreFilterToolbar preFilterToolbar, String attrFunctionName, String attributeName, List list, Object value, AbstractOperator predefinedOp) {
        super(preFilterToolbar, attrFunctionName + "#" + attributeName, list, value);
        this.attrFunctionName = attrFunctionName;
        this.attributeName = attributeName;
        this.operator = predefinedOp;

        for (AbstractOperator op : AbstractOperator.getList()) {
            if (op instanceof AbstractBoolResultOperator && op.worksWithTypes().contains(ExprType.TEXT)) {
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
    }

    @Override
    public String createFilterExpression(Object selectedItem) {
        if (selectedItem != null && !StringUtils.isEmpty(selectedItem.toString())) {
            String escapedValue = StringEscapeUtils.escapeJava(selectedItem.toString());
            String escapedAttributeName = StringEscapeUtils.escapeJava(attributeName);
            return " " + attrFunctionName + "(\"" + escapedAttributeName + "\") "+ operator.getName() + " \"" + escapedValue + "\" ";
        } else {
            return null;
        }
    }
}
