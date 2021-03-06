/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LogPanel.java
 *
 * Created on 25.10.2009, 17:54:43
 */
package org.myjaphoo.gui.logpanel;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.mlsoft.common.acitivity.events.ActivityFinishedEvent;
import org.mlsoft.common.acitivity.events.ActivityStartedEvent;
import org.mlsoft.common.acitivity.events.ErrorMessageEvent;
import org.mlsoft.common.acitivity.events.MessageEvent;
import org.mlsoft.eventbus.GlobalBus;
import org.mlsoft.eventbus.Subscribe;
import org.myjaphoo.gui.panel.AbstractEmbeddablePanel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author mla
 */
public class LogPanel extends AbstractEmbeddablePanel {

    private static final String OFF = "WARN";
    /**
     * hier werden alle logpanels gehalten, damit der LogPanelAppender auf
     * diese zugreifen kann, und auf alle loggen kann.
     * Jedes Panel selbst hat nochmals einen internen Appender an den delegiert
     * wird. Damit kann dann für jedes Panel separat nochmals level, filter, etc
     * festgelegt werden.
     */
    private static ArrayList<LogPanel> createdLogPanels = new ArrayList<LogPanel>();

    static void appendLogEvent(LoggingEvent le) {
        for (LogPanel logPanel : createdLogPanels) {
            logPanel.appenderOfThisPanel.doAppend(le);
        }
    }

    public static void write(int i) {
        for (LogPanel logPanel : createdLogPanels) {
            logPanel.writeChar(i);
        }
    }

    public static void writeln(String line) {
        for (LogPanel logPanel : createdLogPanels) {
            logPanel.log(Color.black, line);
        }
    }

    private JTextPane p;
    private AppenderSkeleton appenderOfThisPanel = new AppenderSkeleton() {

        @Override
        protected void append(LoggingEvent le) {
            String time = org.apache.commons.lang.time.DateFormatUtils.ISO_TIME_FORMAT.format(le.getTimeStamp());
            StringBuilder b = new StringBuilder();
            if (!simpleOutput) {
                b.append(le.getThreadName());
                b.append(" ");
                b.append(le.getLocationInformation().getFileName());
                b.append(".");
                b.append(le.getLocationInformation().getMethodName());
                b.append(":");
                b.append(le.getLocationInformation().getLineNumber());
                b.append(" ");
            }
            b.append(le.getMessage());
            String msg = b.toString();
            logEvent(time, le, le.getLevel().toString(), msg);
        }

        @Override
        public void close() {
        }

        @Override
        public boolean requiresLayout() {
            return false;
        }
    };

    public AppenderSkeleton getPrivateAppender() {
        return appenderOfThisPanel;
    }

    private boolean simpleOutput = false;


    @Subscribe()
    public void logStart(ActivityStartedEvent event) {
        logWithTime("started " + event.getActivityName());
    }

    @Subscribe()
    public void logStop(ActivityFinishedEvent event) {
        logWithTime("finished " + event.getActivityName());
    }

    @Subscribe()
    public void logMessage(MessageEvent event) {
        if (event.isEmphasised()) {
            log(Color.blue, event.getMessage());
        } else {
            logWithTime(event.getMessage());
        }
    }

    @Subscribe()
    public void logErrorMessage(ErrorMessageEvent event) {
        logWithTime(Color.red, event.getErrorMessage());
        if (event.getT() != null) {
            String stcktrace = org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(event.getT());
            log(Color.red, stcktrace);
        }
    }


    /** Creates new form LogPanel */
    public LogPanel() {
        initComponents();
        GlobalBus.bus.register(this);

        createdLogPanels.add(this);
        appenderOfThisPanel.setThreshold(Level.WARN);
    }

    public void setLevel(Level newLevel) {
        appenderOfThisPanel.setThreshold(newLevel);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jToolBar1 = new javax.swing.JToolBar();
        jComboBoxLogLevel = new javax.swing.JComboBox();
        jButtonClear = new javax.swing.JButton();

        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextPane1.setName("jTextPane1"); // NOI18N
        jScrollPane1.setViewportView(jTextPane1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jComboBoxLogLevel.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"OFF", "ALL", "ERROR", "WARN", "INFO", "DEBUG", "TRACE", " "}));
        jComboBoxLogLevel.setSelectedItem(OFF);
        jComboBoxLogLevel.setName("jComboBoxLogLevel"); // NOI18N
        jComboBoxLogLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxLogLevelActionPerformed(evt);
            }
        });
        jToolBar1.add(jComboBoxLogLevel);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.myjaphoo.MyjaphooApp.class).getContext().getResourceMap(LogPanel.class);
        jButtonClear.setText(resourceMap.getString("jButtonClear.text")); // NOI18N
        jButtonClear.setFocusable(false);
        jButtonClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonClear.setName("jButtonClear"); // NOI18N
        jButtonClear.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonClear);

        add(jToolBar1, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxLogLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxLogLevelActionPerformed
        Level level = Level.toLevel((String) jComboBoxLogLevel.getSelectedItem());
        appenderOfThisPanel.setThreshold(level);
    }//GEN-LAST:event_jComboBoxLogLevelActionPerformed

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearActionPerformed
        jTextPane1.setText("");

    }//GEN-LAST:event_jButtonClearActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClear;
    private javax.swing.JComboBox jComboBoxLogLevel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    private void logWithTime(final String txt) {
        logWithTime(jTextPane1.getForeground(), txt);

    }

    private void logWithTime(final Color color, final String txt) {
        String time = DateFormatUtils.ISO_DATETIME_FORMAT.format(System.currentTimeMillis());
        log(color, time + ": " + txt);
    }

    private void log(final Color color, final String txt) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    SimpleAttributeSet s = new SimpleAttributeSet();
                    StyleConstants.setForeground(s, color);
                    jTextPane1.getStyledDocument().insertString(jTextPane1.getStyledDocument().getLength(), "\n" + txt, s);
                } catch (BadLocationException ex) {
                    Logger.getLogger(LogPanel.class.getName()).error("error", ex);
                }
            }
        });
    }

    @Override
    public String getTitle() {
        return "Log";
    }

    @Override
    public void refreshView() {
    }

    @Override
    public void clearView() {
    }

    private void logEvent(final String time, final LoggingEvent le, final String level, final String msg) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    SimpleAttributeSet s = new SimpleAttributeSet();
                    StyleConstants.setForeground(s, Color.ORANGE);
                    jTextPane1.getStyledDocument().insertString(jTextPane1.getStyledDocument().getLength(), "\n" + time, s);
                    SimpleAttributeSet s2 = new SimpleAttributeSet();
                    StyleConstants.setForeground(s2, Color.BLUE);
                    jTextPane1.getStyledDocument().insertString(jTextPane1.getStyledDocument().getLength(), " " + level, s2);
                    SimpleAttributeSet s3 = new SimpleAttributeSet();
                    StyleConstants.setForeground(s3, jTextPane1.getForeground());
                    jTextPane1.getStyledDocument().insertString(jTextPane1.getStyledDocument().getLength(), " " + msg, s3);
                    if (le != null && le.getThrowableInformation() != null && le.getThrowableInformation().getThrowable() != null) {
                        jTextPane1.getStyledDocument().insertString(jTextPane1.getStyledDocument().getLength(), "\n " + le.getThrowableInformation().getThrowable().getMessage(), s3);
                        if (le.getThrowableStrRep() != null) {
                            for (String thrstr : le.getThrowableStrRep()) {
                                jTextPane1.getStyledDocument().insertString(jTextPane1.getStyledDocument().getLength(), "\n " + thrstr, s3);
                            }
                        }
                    }

                } catch (BadLocationException ex) {
                    Logger.getLogger(LogPanel.class.getName()).error("error", ex);
                }
            }
        });
    }

    /**
     * @return the simpleOutput
     */
    public boolean isSimpleOutput() {
        return simpleOutput;
    }

    /**
     * @param simpleOutput the simpleOutput to set
     */
    public void setSimpleOutput(boolean simpleOutput) {
        this.simpleOutput = simpleOutput;
    }

    private void writeChar(final int i) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    char ch = (char) i;
                    SimpleAttributeSet s = new SimpleAttributeSet();
                    StyleConstants.setForeground(s, Color.black);
                    jTextPane1.getStyledDocument().insertString(jTextPane1.getStyledDocument().getLength(), new String(new char[]{ch}), s);
                } catch (BadLocationException ex) {
                    Logger.getLogger(LogPanel.class.getName()).error("error", ex);
                }
            }
        });
    }

}
