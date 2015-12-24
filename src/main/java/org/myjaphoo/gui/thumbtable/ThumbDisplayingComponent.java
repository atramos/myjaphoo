/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.gui.thumbtable;

import java.util.ArrayList;
import org.myjaphoo.gui.movietree.AbstractLeafNode;

/**
 * interface für alle komponenten, die thumbs anzeigen.
 * @author 
 */
public interface ThumbDisplayingComponent {

    /**
     * Liefert alle momentan selektierten ndoes.
     */
    public ArrayList<AbstractLeafNode> getAllSelectedNodes();
}
