/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.swing;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Simple layout that flows the same way as a FlowLayout, but if there is
 * no place anymore for the components, puts them in a separate "other" component.
 * The way how this is put into another component is delegated to a
 * <code>ComponentForRestCreate</code> class, as this depends most time on
 * the type of components used.
 * A Default implementation just shows them as a combobox (without any actions).
 * Based on a forum article at http://www.java-forum.org/awt-swing-swt/6039-bestes-layout-fuer-toolbar.html.
 * 
 * @author lang
 */
public class ResizeLayout implements LayoutManager2 {
    // Unsichtbare Komponenten liegen hier

    java.awt.FlowLayout f;
    private JComponent restComp = null;
    private int otherCompWidth = 16; // not good! we need to make it depending on the "rest" component

    // Konstruktor
    public ResizeLayout() {
    }

    // Konstruktor
    public ResizeLayout(ComponentForRestCreator componentForRestCreator) {
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
        int maxHeight = parent.getHeight()
                - (insets.top + insets.bottom);

        int x = insets.left; // Position X für Komponent
        int y = insets.bottom; // Position Y für Komponent

        Vector<Component> theRest = new Vector<Component>();

        for (int i = 0; i < parent.getComponentCount(); i++) {
            Component c = parent.getComponent(i);
            c.setVisible(true);

            // Sichtbarkeit prüfen
            if (!c.isVisible()) {
                continue;
            }

            c.setVisible(false);

            // Größe ermitteln
            int width = c.getPreferredSize().width;
            int height = c.getPreferredSize().height;

            if (width + x + otherCompWidth < maxWidth) {
                // Komponent Position u. Größe setzen
                c.setVisible(true);
                c.setBounds(x, y, width, height);
            } else {

                for (int z = i; z < parent.getComponentCount(); z++) {
                    Component rc = parent.getComponent(z);
                    theRest.add(rc);
                }

                restComp = componentForRestCreator.createOtherComponent(theRest);

                parent.add(restComp);
                final int restCompWidth = restComp.getPreferredSize().width;
                restComp.setBounds(maxWidth - restCompWidth, y, restCompWidth, restComp.getPreferredSize().height);

                // diese Methode verlassen
                return;
            }

            // position X verschieben für nächste Komponent
            x = x + width;

        }
    }

    // Empfohlene Größe ermitteln
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Component c = null;
        int width = 0;
        int height = 0;

        for (int i = 0; i < parent.getComponentCount(); i++) {
            c = parent.getComponent(i);

            if (!c.isVisible()) {
                continue;
            }

            width = width + c.getPreferredSize().width;
            if (height < c.getPreferredSize().height) {
                height = c.getPreferredSize().height;
            }
        }

        return new Dimension(width, height);
    }

    // maximale Größe ermitteln
    @Override
    public Dimension maximumLayoutSize(Container target) {

        int width = target.getFocusCycleRootAncestor().getWidth();
        int height = preferredLayoutSize(target).height;
        return new Dimension(width, height);
    }

    // minimale Größe ermitteln
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        //return new Dimension(50, 12);
        return preferredLayoutSize(parent);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    // Unwichtige Methoden ... 
    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    // Test main methode zum Testen
    public static void main(String[] args) {
        JFrame frame = new JFrame("ResizeLayout");
        JPanel toolbar = new JPanel(new ResizeLayout());

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
