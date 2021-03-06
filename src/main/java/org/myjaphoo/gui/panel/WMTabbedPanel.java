/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WM TabbedPanel, welches eine liste von AbstractEmbeddablePanels als Tabs darstellt.
 *
 * Created on 27.10.2009, 13:06:35
 */
package org.myjaphoo.gui.panel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * @author mla
 */
public class WMTabbedPanel extends javax.swing.JPanel {

    public static final Logger LOGGER = LoggerFactory.getLogger(WMTabbedPanel.class.getName());
    private List<AbstractEmbeddablePanel> embeddedPanels;

    /** Creates new form TokenTreePanel */
    public WMTabbedPanel(List<AbstractEmbeddablePanel> embeddedPanels) {
        this.embeddedPanels = embeddedPanels;
        initComponents();
        for (AbstractEmbeddablePanel panel : embeddedPanels) {
            jTabbedPane1.addTab(panel.getTitle(), panel);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();

        setBorder(new org.jdesktop.swingx.border.DropShadowBorder());
        setMinimumSize(new java.awt.Dimension(99, 25));
        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N
        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    public void refreshView() {
        for (AbstractEmbeddablePanel panel : embeddedPanels) {
            panel.refreshView();
        }
    }

    public void clearView() {
        for (AbstractEmbeddablePanel panel : embeddedPanels) {
            panel.clearView();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
