/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ImportDialog.java
 *
 * Created on 11.01.2009, 17:37:14
 */
package org.myjaphoo.gui.movimp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.MyjaphooDBPreferences;
import org.myjaphoo.gui.logpanel.LogPanel;
import org.myjaphoo.model.logic.imp.ImportDelegator;
import org.myjaphoo.model.logic.imp.ImportTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ResourceBundle;

/**
 *
 * @author lang
 */
public class ImportDialog extends javax.swing.JDialog implements ImportDialogFeedback{

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/movimp/resources/ImportDialog");

    public static final String WMIMPORT = "wankman.import"; //NOI18N
    private MyjaphooController controller;
    private Logger logger = Logger.getLogger(ImportDialog.class.getName());
    public static Logger IMPLOGGER = Logger.getLogger(WMIMPORT);
    private EnumComboBoxModel<ImportTypes> importComboBoxModel;
    private Timer messageTimer;
    private Timer busyIconTimer;
    private final Icon[] busyIcons = new Icon[15];
    private Icon idleIcon;
    private int busyIconIndex = 0;
    private ImportWithActorsSwingWorker worker = null;

    /** Creates new form ImportDialog */
    public ImportDialog(java.awt.Frame parent, MyjaphooController controller) {
        this(parent, controller, MyjaphooDBPreferences.PRF_PRESELECTEDDIR_IN_IMPORTFILEDIALOG.getVal());
    }

    /** Creates new form ImportDialog */
    public ImportDialog(java.awt.Frame parent, MyjaphooController controller, String preselectedDir) {
        super(parent, true);
        this.controller = controller;
        setTitle(localeBundle.getString("IMPORT"));
        initComponents();
        initStatusBarThings();

        // set preferences:
        importComboBoxModel = new EnumComboBoxModel(ImportTypes.class);

        importComboBoxModel.setSelectedItem(MyjaphooAppPrefs.PRF_IMPORTMODE.getSelectedEnum());

        jComboBoxPicOrMov.setModel(importComboBoxModel);
        jFileChooser1.setSelectedFile(new File(preselectedDir));

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                if (!isRunning()) {
                    ImportDialog.this.setVisible(false);
                }
            }
        });

        getLogPanel().getPrivateAppender().addFilter(new Filter() {

            @Override
            public int decide(LoggingEvent le) {
                if (WMIMPORT.equals(le.getLoggerName()) || le.getLevel() == Level.ERROR || le.getLevel() == Level.FATAL) {
                    return Filter.ACCEPT;
                } else {
                    return Filter.DENY;
                }
            }
        });
        getLogPanel().setSimpleOutput(true);
        getLogPanel().setLevel(Level.INFO);
    }

    public void setStatusBarActionStarted() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (!busyIconTimer.isRunning()) {
                    statusAnimationLabel.setIcon(busyIcons[0]);
                    busyIconIndex = 0;
                    busyIconTimer.start();
                }
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                progressBar.setMinimum(0);
                progressBar.setMaximum(100);
            }
        });


    }

    public void setStatusBarActionStopped() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                busyIconTimer.stop();
                statusAnimationLabel.setIcon(idleIcon);
                progressBar.setVisible(false);
                progressBar.setValue(0);
                jButton1.setText(localeBundle.getString("IMPORT"));
            }
        });
    }

    public void setStatusBarMessage(final String msg) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                String abbrmsg = StringUtils.abbreviate(msg, 65, 70);
                statusMessageLabel.setText(abbrmsg);
                messageTimer.restart();
            }
        });
    }

    public void setStatusBarProgress(final int value) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                progressBar.setVisible(true);
                progressBar.setIndeterminate(false);
                progressBar.setValue(value);
            }
        });
    }

    private LogPanel getLogPanel() {
        return (LogPanel) jPanel4;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new LogPanel();
        jPanel3 = new javax.swing.JPanel();
        jComboBoxPicOrMov = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(700, 600));
        setName("Form"); // NOI18N

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jFileChooser1.setControlButtonsAreShown(false);
        jFileChooser1.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        jFileChooser1.setMinimumSize(new java.awt.Dimension(425, 345));
        jFileChooser1.setName("jFileChooser1"); // NOI18N
        jSplitPane1.setLeftComponent(jFileChooser1);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jComboBoxPicOrMov.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Movies", "Pictures" }));
        jComboBoxPicOrMov.setName("jComboBoxPicOrMov"); // NOI18N
        jPanel3.add(jComboBoxPicOrMov);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.myjaphoo.MyjaphooApp.class).getContext().getResourceMap(ImportDialog.class);
        jButton1.setText(resourceMap.getString("jButtonImp2.text")); // NOI18N
        jButton1.setName("jButtonImp2"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        jPanel1.add(jPanel3, java.awt.BorderLayout.NORTH);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 763, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 593, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        jPanel1.add(statusPanel, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setRightComponent(jPanel1);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void updatefileAndRemainingTime(final String absolutePath, final String formatDurationWords) {
        String fmtText = absolutePath + ": " + formatDurationWords; //NOI18N
        setStatusBarMessage(fmtText);
    }

    private boolean isRunning() {
        if (worker == null) {
            return false;
        }
        return !worker.isDone() && !worker.isCancelled();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (!isRunning()) {
            final File dir = jFileChooser1.getSelectedFile();
            MyjaphooDBPreferences.PRF_PRESELECTEDDIR_IN_IMPORTFILEDIALOG.setVal(dir.getAbsolutePath());
            
            setStatusBarActionStarted();

            if (dir != null) {
                ImportDelegator delegator = importComboBoxModel.getSelectedItem().createImportDelegator();
                worker = new ImportWithActorsSwingWorker(this, delegator, dir);
                worker.execute();
            }
            jButton1.setText(localeBundle.getString("CANCEL"));
        } else {
            jButton1.setText(localeBundle.getString("CANCELLING..."));
            worker.abortImport();
            jButton1.setText(localeBundle.getString("IMPORT"));

        }
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBoxPicOrMov;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private void initStatusBarThings() {
        ResourceMap resourceMap = controller.getView().getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout"); //NOI18N
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText(""); //NOI18N
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate"); //NOI18N
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]"); //NOI18N
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon"); //NOI18N
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

    }
}
