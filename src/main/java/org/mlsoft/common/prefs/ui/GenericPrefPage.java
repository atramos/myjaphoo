package org.mlsoft.common.prefs.ui;

/**
 * <p>�berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import org.jdesktop.swingx.JXColorSelectionButton;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXTitledPanel;
import org.mlsoft.common.prefs.model.edit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class GenericPrefPage extends JXTitledPanel {

    private static final Logger logger = LoggerFactory.getLogger(GenericPrefPage.class.getName());
    private EditableGroup prefGroup;
    private static final JFileChooser downloadLocationChooser = new JFileChooser();
    FormLayout layout = new FormLayout(
            "right:max(40dlu;p), 4dlu, 110dlu:grow, 2dlu, " // 1st major column
            + " right:max(20dlu;p)", // 2nd major column
            "");                                      // add rows dynamically
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);

    /**
     * The PreferencesPanel constructor creates the Preferences window and
     * delegates the creation of each Preferences component.
     */
    public GenericPrefPage(EditableGroup prefGroup) {
        this.prefGroup = prefGroup;

        setTitle(prefGroup.getName());
        JXHeader titlesep = new JXHeader(prefGroup.getDisplayname(), prefGroup.getDescrlong());
        builder.append(titlesep, builder.getColumnCount());

        Iterator iter = prefGroup.childIterator();
        while (iter.hasNext()) {
            EditableItem item = (EditableItem) iter.next();

            if (item instanceof EditableDirectoryVal) {
                createDirectoryChoice((EditableDirectoryVal) item);
            } else if (item instanceof EditableFileVal) {
                createFileChoice((EditableFileVal) item);
            } else if (item instanceof EditableBooleanVal) {
                createBooleanChoice((EditableBooleanVal) item);
            } else if (item instanceof EditableStringSelectionVal) {
                createStringSelectionChoice((EditableStringSelectionVal) item);
            } else if (item instanceof EditableStringVal) {
                createStringChoice((EditableStringVal) item);
            } else if (item instanceof EditableIntegerVal) {
                createIntegerChoice((EditableIntegerVal) item);
            } else if (item instanceof EditableColorVal) {
                createColorChoice((EditableColorVal) item);
            }

        }
        setContentContainer(new JScrollPane(builder.getPanel()));
    }

    public void addHeader(EditableItem item) {
        builder.appendSeparator(item.getDisplayname());
        JXHeader titlesep = new JXHeader("", item.getDescrshort());
        builder.append(titlesep, builder.getColumnCount());
    }

    private JTextField createTextField(EditableStringVal prefVal) {
        final JTextField textField = new JTextField();
        textField.setText(prefVal.getVal());
        textField.setToolTipText(prefVal.getDescrshort());
        return textField;
    }

    /**
     * This method creates the "download location" option
     */
    private void createDirectoryChoice(final EditableDirectoryVal prefVal) {
        addHeader(prefVal);
        final JTextField strDirTextField = createTextField(prefVal);
        strDirTextField.setEditable(false);

        builder.append(createLabel(prefVal), strDirTextField);
        JButton downloadLocationButton = createFileChooserButton("choose directory");
        downloadLocationButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                downloadLocationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = downloadLocationChooser.showOpenDialog(GenericPrefPage.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = downloadLocationChooser.getSelectedFile();

                    try {
                        prefVal.setVal(file.getCanonicalPath());
                    } catch (IOException e) {
                        logger.error("fehler", e);
                    }
                }
                strDirTextField.setText(prefVal.getVal());
            }
        });
        builder.append(downloadLocationButton);
        builder.nextLine();
    }

    private JButton createFileChooserButton(String toolTip) {
        JButton downloadLocationButton = new JButton();
        downloadLocationButton.setText("...");
        downloadLocationButton.setPreferredSize(new Dimension(20, 20));
        downloadLocationButton.setMinimumSize(new Dimension(20, 20));
        downloadLocationButton.setMaximumSize(new Dimension(20, 20));
        downloadLocationButton.setFocusable(false);
        downloadLocationButton.setToolTipText(toolTip);
        return downloadLocationButton;
    }

    private void createColorChoice(final EditableColorVal prefVal) {
        addHeader(prefVal);
        final JXColorSelectionButton colorselectionbutton = new JXColorSelectionButton(prefVal.getVal());
        colorselectionbutton.setPreferredSize(new Dimension(20, 20));
        colorselectionbutton.setMinimumSize(new Dimension(20, 30));
        colorselectionbutton.setMaximumSize(new Dimension(20, 20));
        builder.append(createLabel(prefVal), colorselectionbutton);
        colorselectionbutton.addPropertyChangeListener("background", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                prefVal.setVal(colorselectionbutton.getBackground());
            }
        });
        builder.nextLine();
    }

    private void createFileChoice(final EditableFileVal prefVal) {
        addHeader(prefVal);

        final JTextField fileTextField = createTextField(prefVal);
        fileTextField.setEditable(false);
        builder.append(createLabel(prefVal), fileTextField);

        JButton downloadLocationButton = createFileChooserButton("choose File");
        downloadLocationButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                downloadLocationChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal = downloadLocationChooser.showOpenDialog(GenericPrefPage.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = downloadLocationChooser.getSelectedFile();

                    try {
                        prefVal.setVal(file.getCanonicalPath());
                    } catch (IOException e) {
                        logger.error("fehler", e);
                    }
                }
                fileTextField.setText(prefVal.getVal());
            }
        });
        builder.append(downloadLocationButton);
        builder.nextLine();
    }

    private String createLabel(EditableVal item) {
        return item.getDisplayname() + ":";
    }

    /**
     * This method creates the "download location" option
     */
    private void createStringChoice(final EditableStringVal prefVal) {
        addHeader(prefVal);
        final JTextField strTextField = createTextField(prefVal);
        builder.append(createLabel(prefVal), strTextField);
        strTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                prefVal.setVal(strTextField.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                prefVal.setVal(strTextField.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                prefVal.setVal(strTextField.getText());

            }
        });
        builder.nextLine();
    }

    private void createIntegerChoice(final EditableIntegerVal prefVal) {
        addHeader(prefVal);
        final JTextField intInputTextField = new JTextField();
        intInputTextField.setText(Integer.toString(prefVal.getVal()));
        builder.append(createLabel(prefVal), intInputTextField);
        intInputTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                try {
                    prefVal.setVal(Integer.parseInt(intInputTextField.getText()));
                } catch (NumberFormatException ex) {
                }
            }

            public void removeUpdate(DocumentEvent e) {
                try {
                    prefVal.setVal(Integer.parseInt(intInputTextField.getText()));
                } catch (NumberFormatException ex) {
                }
            }

            public void changedUpdate(DocumentEvent e) {
                try {
                    prefVal.setVal(Integer.parseInt(intInputTextField.getText()));
                } catch (NumberFormatException ex) {
                }

            }
        });
        builder.nextLine();
    }

    /**
     * This method creates the "preserve directory structure" option
     */
    private void createBooleanChoice(final EditableBooleanVal prefVal) {
        addHeader(prefVal);
        JCheckBox checkBox = new JCheckBox();
        checkBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    prefVal.setVal(true);
                } else {
                    prefVal.setVal(false);
                }
            }
        });
        checkBox.setSelected(prefVal.getVal());
        builder.append(createLabel(prefVal), checkBox);
        builder.nextLine();
    }

    /**
     * This method creates the "Spoof User Agent" option
     */
    private void createStringSelectionChoice(final EditableStringSelectionVal prefVal) {
        addHeader(prefVal);

        JComboBox comboBox = new JComboBox(prefVal.getAuswahl());
        comboBox.setSelectedIndex(prefVal.getIndex());
        comboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JComboBox cb = (JComboBox) event.getSource();
                prefVal.setIndex(cb.getSelectedIndex());
            }
        });
        builder.append(prefVal.getDisplayname(), comboBox);
        builder.nextLine();
    }

    /**
     * damit DefaultMutableTreeNode den Namen der prefGroup anzeigt:
     * @return Name der PrefGroup
     */
    @Override
    public String toString() {
        return prefGroup.getDisplayname();
    }
}
