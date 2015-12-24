/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewTagDialog.java
 *
 * Created on 16.08.2011, 15:57:19
 */
package org.myjaphoo.gui.newtagdlg;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.mlsoft.swing.EventDispatchTools;
import org.myjaphoo.MyjaphooApp;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.Token;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author mla
 */
public class NewTagDialog extends javax.swing.JDialog {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/newtagdlg/resources/NewTagDialog");

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    /**
     * Creates new form NewTagDialog
     */
    public NewTagDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();


        // Close the dialog when Esc is pressed
        String cancelName = localeBundle.getString("CANCEL");
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    public static class NewTagDlgResult {

        public String name;
        public String descr;
        public Token parentToken;
        public MetaToken parentMetaToken;
    }

    public static NewTagDlgResult newTag(final String title, final List<Token> tagsForParentSelection, final Token preselectedParent) {
        final NewTagDlgResult[] result = new NewTagDlgResult[1];
        // must be on EDT, insubstantial will fail otherwise
        EventDispatchTools.onEDTWait(new Runnable() {
            @Override
            public void run() {
                result[0] = newTagIntern(title, tagsForParentSelection, preselectedParent);
            }
        });
        return result[0];
    }

    private static NewTagDlgResult newTagIntern(String title, List<Token> tagsForParentSelection, Token preselectedParent) {

        NewTagDialog dlg = new NewTagDialog(null, true);
        dlg.jTextFieldName.setText(""); //NOI18N
        dlg.jTextArea1.setText(""); //NOI18N
        Collections.sort(tagsForParentSelection);
        dlg.jComboBoxParentTags.setModel(new DefaultComboBoxModel(tagsForParentSelection.toArray()));
        dlg.jComboBoxParentTags.setSelectedItem(preselectedParent);
        AutoCompleteDecorator.decorate(dlg.jComboBoxParentTags);
        dlg.setTitle(title);
        dlg.jTextFieldName.requestFocusInWindow();

        MyjaphooApp.getApplication().show(dlg);
        if (dlg.getReturnStatus() == RET_OK) {
            NewTagDlgResult result = new NewTagDlgResult();
            result.name = dlg.jTextFieldName.getText();
            result.descr = dlg.jTextArea1.getText();
            result.parentToken = (Token) dlg.jComboBoxParentTags.getSelectedItem();
            return result;
        } else {
            return null;
        }
    }

    public static NewTagDlgResult newMetaTag(final String title, final List<MetaToken> tagsForParentSelection, final MetaToken preselectedParent) {
        final NewTagDlgResult[] result = new NewTagDlgResult[1];
        // must be on EDT, insubstantial will fail otherwise
        EventDispatchTools.onEDTWait(new Runnable() {
            @Override
            public void run() {
                result[0] = newMetaTagIntern(title, tagsForParentSelection, preselectedParent);
            }
        });
        return result[0];
    }

    private static NewTagDlgResult newMetaTagIntern(String title, List<MetaToken> tagsForParentSelection, MetaToken preselectedParent) {
        NewTagDialog dlg = new NewTagDialog(null, true);
        dlg.jTextFieldName.setText(""); //NOI18N
        dlg.jTextArea1.setText(""); //NOI18N
        Collections.sort(tagsForParentSelection);
        dlg.jComboBoxParentTags.setModel(new DefaultComboBoxModel(tagsForParentSelection.toArray()));
        dlg.jComboBoxParentTags.setSelectedItem(preselectedParent);
        AutoCompleteDecorator.decorate(dlg.jComboBoxParentTags);
        dlg.setTitle(title);
        dlg.jTextFieldName.requestFocusInWindow();

        MyjaphooApp.getApplication().show(dlg);
        if (dlg.getReturnStatus() == RET_OK) {
            NewTagDlgResult result = new NewTagDlgResult();
            result.name = dlg.jTextFieldName.getText();
            result.descr = dlg.jTextArea1.getText();
            result.parentMetaToken = (MetaToken) dlg.jComboBoxParentTags.getSelectedItem();
            return result;
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

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxParentTags = new javax.swing.JComboBox();
        jTextFieldName = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.myjaphoo.MyjaphooApp.class).getContext().getResourceMap(NewTagDialog.class);
        okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jComboBoxParentTags.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
        jComboBoxParentTags.setName("jComboBoxParentTags"); // NOI18N

        jTextFieldName.setText(resourceMap.getString("jTextFieldName.text")); // NOI18N
        jTextFieldName.setName("jTextFieldName"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel1))
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextFieldName, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                                        .addComponent(jComboBoxParentTags, 0, 297, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE))
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(252, Short.MAX_VALUE)
                                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton)
                                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[]{cancelButton, okButton});

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jComboBoxParentTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cancelButton)
                                        .addComponent(okButton))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
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

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                NewTagDialog dialog = new NewTagDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox jComboBoxParentTags;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;
}
