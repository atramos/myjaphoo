/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.swing;

import java.awt.Component;
import java.util.Vector;
import javax.swing.JComponent;

/**
 * Factory class to create the component for the ResizeLayout
 * that shows all the rest of components, for which no place is to
 * display them.
 * @author lang
 */
public interface ComponentForRestCreator {

    public JComponent createOtherComponent(Vector<Component> theRest);
}
