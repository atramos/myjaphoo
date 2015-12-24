/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EditDatabaseConnectionDialog.java
 *
 * Created on 27.02.2012, 14:56:19
 */
package org.myjaphoo.gui.db;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.view.ValidationComponentUtils;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.myjaphoo.gui.icons.Icons;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.dbconfig.DatabaseDriver;
import org.myjaphoo.model.dbconfig.DatabaseDriverUrlParameter;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

/**
 * @author lang
 */
public class EditDatabaseConnectionDialog extends javax.swing.JDialog {

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;
    private FormLayout layout = new FormLayout(
            "right:max(40dlu;p), 4dlu, 110dlu:grow, 2dlu, " // 1st major column
                    + " right:max(20dlu;p)", // 2nd major column
            "");                                      // add rows dynamically
    private DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    private DatabaseConfiguration dbconfig;
    private DatabaseConnectionController controller;
    private JTextField jtextFieldName = new JTextField();
    private EnumComboBoxModel<DatabaseDriver> databaseDriverModel = new EnumComboBoxModel(DatabaseDriver.class);
    private JComboBox jcomboBoxDriver = new JComboBox(databaseDriverModel);
    private JTextField jtextFieldServer = new JTextField();
    private JFormattedTextField jtextFieldPort = new JFormattedTextField();
    private JTextField jtextFieldFileName = new JTextField();
    private JTextField jtextFieldDatabaseName = new JTextField();
    private JTextField jtextFieldUsername = new JTextField();
    private JTextField jtextFieldPassword = new JTextField();
    private JTextField jtextFieldSID = new JTextField();
    private JCheckBox jcheckboxCreateDB = new JCheckBox();
    private JLabel labelCreateDB;
    private JLabel labelDatabase;
    private JLabel labelFilename;
    private JLabel labelPort;
    private JLabel labelSID;
    private JLabel labelServer;
    private JLabel availableIndicatorIcon = new JLabel(Icons.IR_ERROR.icon);

    /**
     * Creates new form EditDatabaseConnectionDialog
     */
    public EditDatabaseConnectionDialog(DatabaseConnectionController controller, DatabaseConfiguration dbconfig, boolean modal) {
        super((JFrame) null, modal);
        this.controller = controller;
        this.dbconfig = dbconfig;
        initComponents();
        prepareForm();
        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
        setSize(400, 600);
    }

    private void prepareForm() {
        jtextFieldName.setText(dbconfig.getName());
        ValidationComponentUtils.setMandatory(jtextFieldName, true);
        ValidationComponentUtils.setMessageKey(jtextFieldName, "Database Configuration.Name");
        ValidationComponentUtils.setSeverity(jtextFieldName, Severity.ERROR);


        databaseDriverModel.setSelectedItem(dbconfig.getDatabaseDriver());
        jtextFieldServer.setText(dbconfig.getServer());
        jtextFieldPort.setFormatterFactory(new AbstractFormatterFactory() {

            @Override
            public AbstractFormatter getFormatter(JFormattedTextField tf) {
                DecimalFormat portFormat = new DecimalFormat("0000");
                NumberFormatter nf = new NumberFormatter(portFormat);
                return nf;
            }
        });
        if (dbconfig.getPort() != null) {
            jtextFieldPort.setValue(dbconfig.getPort());
        }
        jtextFieldFileName.setText(dbconfig.getFilename());
        jtextFieldDatabaseName.setText(dbconfig.getDatabasename());
        jtextFieldSID.setText(dbconfig.getSid());
        jcheckboxCreateDB.setSelected(dbconfig.isCreateDb());
        jtextFieldUsername.setText(dbconfig.getUsername());
        jtextFieldPassword.setText(dbconfig.getPassword());


        builder.appendSeparator("Connection Info");

        builder.append("Name", jtextFieldName);
        builder.nextLine();
        builder.append("Driver", jcomboBoxDriver);
        builder.append(availableIndicatorIcon);
        builder.nextLine();

        builder.appendSeparator("Credentials");
        builder.append("User Name", jtextFieldUsername);
        builder.nextLine();
        builder.append("Password", jtextFieldPassword);
        builder.nextLine();

        builder.appendSeparator("Server Connection");
        labelServer = new JLabel("Server");

        builder.append(labelServer, jtextFieldServer);
        builder.nextLine();
        labelPort = new JLabel("Port");
        builder.append(labelPort, jtextFieldPort);
        builder.nextLine();
        labelFilename = new JLabel("Filename");

        builder.append(labelFilename, jtextFieldFileName);
        builder.nextLine();
        labelDatabase = new JLabel("Databasename");
        builder.append(labelDatabase, jtextFieldDatabaseName);
        builder.nextLine();
        labelSID = new JLabel("SID");


        builder.append(labelSID, jtextFieldSID);
        builder.nextLine();
        labelCreateDB = new JLabel("create DB");
        builder.append(labelCreateDB, jcheckboxCreateDB);

        jcomboBoxDriver.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dbconfig.setDatabaseDriver(databaseDriverModel.getSelectedItem());
                enableDisable();
            }
        });
        jMainPanel.setLayout(new BorderLayout());

        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(builder.getPanel());
        //ValidationComponentUtils.updateComponentTreeMandatoryBackground(builder.getPanel());
        //ValidationComponentUtils.updateComponentTreeMandatoryBorder(builder.getPanel());


        ((JXTitledPanel) jMainPanel).setContentContainer(new JScrollPane(builder.getPanel()));
        ((JXTitledPanel) jMainPanel).setTitle("Edit Database Connection Parameter");
        enableDisable();

    }

    private void enableDisable() {

        final String urlFormat = dbconfig.getDatabaseDriver().getUrlFormat();
        jtextFieldServer.setVisible(DatabaseDriverUrlParameter.SERVER.hasParameter(urlFormat));
        labelServer.setVisible(DatabaseDriverUrlParameter.SERVER.hasParameter(urlFormat));
        jtextFieldPort.setVisible(DatabaseDriverUrlParameter.PORT.hasParameter(urlFormat));
        labelPort.setVisible(DatabaseDriverUrlParameter.PORT.hasParameter(urlFormat));
        jtextFieldFileName.setVisible(DatabaseDriverUrlParameter.FILENAME.hasParameter(urlFormat));
        labelFilename.setVisible(DatabaseDriverUrlParameter.FILENAME.hasParameter(urlFormat));
        jtextFieldDatabaseName.setVisible(DatabaseDriverUrlParameter.DATABASENAME.hasParameter(urlFormat));
        labelDatabase.setVisible(DatabaseDriverUrlParameter.DATABASENAME.hasParameter(urlFormat));
        jtextFieldSID.setVisible(DatabaseDriverUrlParameter.SID.hasParameter(urlFormat));
        labelSID.setVisible(DatabaseDriverUrlParameter.SID.hasParameter(urlFormat));
        jcheckboxCreateDB.setVisible(DatabaseDriverUrlParameter.CREATEDB.hasParameter(urlFormat));
        labelCreateDB.setVisible(DatabaseDriverUrlParameter.CREATEDB.hasParameter(urlFormat));

        availableIndicatorIcon.setVisible(!dbconfig.getDatabaseDriver().isDriverAvailable());
        pack();
    }

    private void copyBackValues() {
        dbconfig.setName(jtextFieldName.getText());
        dbconfig.setServer(jtextFieldServer.getText());

        //if (StringUtils.isNumeric(jtextFieldPort.getText())) {
        dbconfig.setPort(asInteger(jtextFieldPort.getValue()));
        //}
        dbconfig.setFilename(jtextFieldFileName.getText());
        dbconfig.setDatabasename(jtextFieldDatabaseName.getText());
        dbconfig.setSid(jtextFieldSID.getText());
        dbconfig.setCreateDb(jcheckboxCreateDB.isSelected());
        dbconfig.setUsername(jtextFieldUsername.getText());
        dbconfig.setPassword(jtextFieldPassword.getText());
    }

    private Integer asInteger(Object value) {
        if (value instanceof Number) {
            return ((Number)value).intValue();
        } else {
            return null;
        }
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMainPanel = new org.jdesktop.swingx.JXTitledPanel();
        jActionPanel = new javax.swing.JPanel();
        jButtonTestConnection = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jMainPanel.setName("jMainPanel"); // NOI18N

        javax.swing.GroupLayout jMainPanelLayout = new javax.swing.GroupLayout(jMainPanel);
        jMainPanel.setLayout(jMainPanelLayout);
        jMainPanelLayout.setHorizontalGroup(
                jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        jMainPanelLayout.setVerticalGroup(
                jMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 267, Short.MAX_VALUE)
        );

        getContentPane().add(jMainPanel, java.awt.BorderLayout.CENTER);

        jActionPanel.setName("jActionPanel"); // NOI18N
        jActionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.myjaphoo.MyjaphooApp.class).getContext().getResourceMap(EditDatabaseConnectionDialog.class);
        jButtonTestConnection.setText(resourceMap.getString("jButtonTestConnection.text")); // NOI18N
        jButtonTestConnection.setName("jButtonTestConnection"); // NOI18N
        jButtonTestConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTestConnectionActionPerformed(evt);
            }
        });
        jActionPanel.add(jButtonTestConnection);

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jActionPanel.add(cancelButton);

        okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        jActionPanel.add(okButton);
        getRootPane().setDefaultButton(okButton);

        getContentPane().add(jActionPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        copyBackValues();
        ValidationResult validationResult = new DbConfigValidator().validate(dbconfig);
        ValidationComponentUtils.updateComponentTreeSeverity(builder.getPanel(), validationResult);
        ValidationComponentUtils.updateComponentTreeSeverityBackground(builder.getPanel(), validationResult);

        if (validationResult.hasErrors()) {
            showValidationMessage(evt,
                    "To save the connection, fix the following errors:",
                    validationResult);
            return;
        }
        if (validationResult.hasWarnings()) {
            showValidationMessage(evt,
                    "Note: some fields are invalid.",
                    validationResult);
        }

        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jButtonTestConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTestConnectionActionPerformed
        // test, if the connection works:
        copyBackValues();

        DBConnection testConnection = new DBConnection(dbconfig);
        try {
            if (!dbconfig.getDatabaseDriver().isDriverAvailable()) {
                throw new RuntimeException("The databasedriver " + dbconfig.getDatabaseDriver().getJdbcDriverClass() + " could not be found in the classpath!");
            }
            testConnection.open();
            controller.getController().message("Connection successfull!");
            testConnection.close();
        } catch (Exception e) {
            controller.getController().showAndLogErroDlg("Connection failed!", "Connection to '" + dbconfig.getFilledConnectionUrl() + "' has thrown an error!\n" + e.getMessage(), e);
        }

    }//GEN-LAST:event_jButtonTestConnectionActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel jActionPanel;
    private javax.swing.JButton jButtonTestConnection;
    private javax.swing.JPanel jMainPanel;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

    boolean isOk() {
        return returnStatus == RET_OK;
    }


    public static void showValidationMessage(ActionEvent e,
                                             String headerText, ValidationResult validationResult) {
        if (validationResult.isEmpty()) {
            throw new IllegalArgumentException(
                    "The validation result must not be empty.");
        }
        Object eventSource = e.getSource();
        Component parent = null;
        if (eventSource instanceof Component) {
            parent = SwingUtilities
                    .windowForComponent((Component) eventSource);
        }
        boolean error = validationResult.hasErrors();
        String messageText = headerText + "\n\n"
                + validationResult.getMessagesText() + "\n\n";
        String titleText = "Validation "
                + (error ? "Error" : "Warning");
        int messageType = error ? JOptionPane.ERROR_MESSAGE
                : JOptionPane.WARNING_MESSAGE;
        JOptionPane.showMessageDialog(parent, messageText, titleText,
                messageType);
    }
}
