/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.swing;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Simple layout that flows the same way as a FlowLayout, but if there is no
 * place anymore for the components, puts them in a separate "other" component.
 * The way how this is put into another component is delegated to a
 * <code>ComponentForRestCreate</code> class, as this depends most time on the
 * type of components used. A Default implementation just shows them as a
 * combobox (without any actions). Based on a forum article at
 * http://www.java-forum.org/awt-swing-swt/6039-bestes-layout-fuer-toolbar.html.
 *
 * @author lang
 */
public class ResizeLayout1 implements LayoutManager {
    // Unsichtbare Komponenten liegen hier

    private LayoutManager delegation;
    private JComponent restComp = null;
    private int otherCompWidth = 16; // not good! we need to make it depending on the "rest" component

    // Konstruktor
    public ResizeLayout1(LayoutManager delegation) {
        this.delegation = delegation;
    }

    // Konstruktor
    public ResizeLayout1(LayoutManager delegation, ComponentForRestCreator componentForRestCreator) {
        this.delegation = delegation;
        this.componentForRestCreator = componentForRestCreator;
    }

    /**
     * @param componentForRestCreator the componentForRestCreator to set
     */
    public void setComponentForRestCreator(ComponentForRestCreator componentForRestCreator) {
        this.componentForRestCreator = componentForRestCreator;
    }

    private ComponentForRestCreator componentForRestCreator = new ComponentForRestCreator() {

        @Override
        public JComponent createOtherComponent(Vector<Component> theRest) {
            Vector<String> txts = new Vector<String>();
            txts.add(">>");

            for (Component c : theRest) {
                if (!(c instanceof JButton)) {
                    continue;
                }

                JButton bt = (JButton) c;
                txts.add(bt.getText());
            }
            JComboBox cb_otherComp = new JComboBox();
            DefaultComboBoxModel cbm = new DefaultComboBoxModel(txts);
            cb_otherComp.setModel(cbm);
            return cb_otherComp;
        }
    };

    @Override
    public void layoutContainer(Container parent) {

        // set "other" component invisible, if we already have one:

        if (restComp != null) {
            restComp.setVisible(false);
            otherCompWidth = restComp.getPreferredSize().width;
            parent.remove(restComp);
        }

        Insets insets = parent.getInsets();
        int maxWidth = parent.getWidth()
                - (insets.left + insets.right);

        int y = insets.bottom; // Position Y für Komponent

        // set all components visible to let them lay out by the delegated layout manager
        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component c = parent.getComponent(i);
            c.setVisible(true);
        }
        // let the delegator lay out the components:        
        delegation.layoutContainer(parent);


        // set all components that flow out of the parent comp. size to the
        // "rest" component and make them invisible in the parent component:
        Vector<Component> theRest = new Vector<Component>();

        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component c = parent.getComponent(i);

            int lastpixOfComponent = c.getBounds().x + c.getBounds().width;

            if (lastpixOfComponent + otherCompWidth > maxWidth) {

                for (int z = i; z < parent.getComponentCount(); z++) {
                    Component rc = parent.getComponent(z);
                    theRest.add(rc);
                    rc.setVisible(false);
                }

                restComp = componentForRestCreator.createOtherComponent(theRest);

                parent.add(restComp);
                final int restCompWidth = restComp.getPreferredSize().width;
                restComp.setBounds(maxWidth - restCompWidth, y, restCompWidth, restComp.getPreferredSize().height);

                // diese Methode verlassen
                return;
            }
        }
    }

    // Empfohlene Größe ermitteln
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return delegation.preferredLayoutSize(parent);
    }



    // minimale Größe ermitteln
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return delegation.minimumLayoutSize(parent);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        delegation.addLayoutComponent(name, comp);
    }



    @Override
    public void removeLayoutComponent(Component comp) {
        delegation.removeLayoutComponent(comp);
    }




    // Test main methode zum Testen
    public static void main(String[] args) {
        JFrame frame = new JFrame("ResizeLayout");
        JPanel toolbar = new JPanel(new ResizeLayout1(new BoxLayout(frame, BoxLayout.LINE_AXIS)));

        toolbar.add(new JButton("Neu..."));
        toolbar.add(new JButton("Bearbeiten"));
        toolbar.add(new JButton("Einstellungen"));
        toolbar.add(new JButton("Hilfe"));
        toolbar.add(new JButton("Forum"));
        toolbar.add(new JButton("Java"));
        toolbar.add(new JButton("Echt grovy?"));
        toolbar.add(new JButton("Praktisch"));
        toolbar.add(new JButton("Aha"));
        toolbar.add(new JButton("Blabla"));
        toolbar.add(new JButton("so funzt!"));

        frame.getContentPane().add(toolbar);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}
