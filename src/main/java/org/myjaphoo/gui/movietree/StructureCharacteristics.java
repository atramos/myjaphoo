package org.myjaphoo.gui.movietree;

import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.action.ViewContext;

import javax.swing.*;
import java.util.List;

/**
 * object that contains certain characteristics for a particular object,
 * e.g. special menu entries, etc.
 * Its delegated to this object to make it lightweight, as many particular objects
 * have the same characteristics.
 *
 * @author mla
 * @version $Id$
 */
public interface StructureCharacteristics {

    /**
     * returns individual menu items particular for this ui object.
     *
     * @param controller
     * @param structureNode
     *@param context  @return
     */
    List<JMenuItem> getMenuItems(MyjaphooController controller, MovieStructureNode structureNode, ViewContext context);
}
