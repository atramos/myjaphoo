package org.myjaphoo.gui.filtereditor;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import org.myjaphoo.gui.icons.Icons;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * a filter chooser brick that uses a combobox to display selectable elements.
 * It is designed as toolbar (instead of a panel) to use the specific toolbar ui laf.
 *
 * @author lang
 * @version $Id$
 */
public abstract class ComboBoxFilterChooser extends JToolBar implements FilterChooser {

    private final PreFilterToolbar preFilterToolbar;
    protected final JComboBox comboBox;
    private final ListComboBoxModel model;

    public ComboBoxFilterChooser(final PreFilterToolbar preFilterToolbar, String title, java.util.List list) {
        this(preFilterToolbar, title, list, null);
    }

    public ComboBoxFilterChooser(final PreFilterToolbar preFilterToolbar, String title, List list, Object value) {
        this.preFilterToolbar = preFilterToolbar;
        this.setFloatable(false);

        setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        JLabel titleLabel = new JLabel(StringUtils.capitalize(title));
        add(titleLabel);

        model = new ListComboBoxModel(list);
        comboBox = new JXComboBox(model);
        // set fix layout, otherwise the layouting of the whole frame slows extremely down
        comboBox.setMaximumSize(new Dimension(120, 24));
        comboBox.setMinimumSize(new Dimension(120, 24));
        comboBox.setPreferredSize(new Dimension(120, 24));
        comboBox.setEditable(true);
        AutoCompleteDecorator.decorate(comboBox);
        comboBox.setSelectedItem(value);
        add(comboBox);

        final JButton closeButton = new JButton(Icons.IR_WINDOW_CLOSE.icon);
        add(closeButton);

        closeButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == closeButton) {
                    // remove me from the toolbar:
                    preFilterToolbar.remove(ComboBoxFilterChooser.this);
                    preFilterToolbar.repaint();
                    preFilterToolbar.getFilterEditorPanel().updateViewWithCurrentFilter();
                }
            }
        });

        comboBox.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == comboBox) {
                    preFilterToolbar.getFilterEditorPanel().updateViewWithCurrentFilter();
                }
            }
        });
    }

    @Override
    public String getFilterExpression() {
        return createFilterExpression(model.getSelectedItem());
    }

    public abstract String createFilterExpression(Object selectedItem);

    public void clearFilter() {
        comboBox.setSelectedItem(null);
    }
}
