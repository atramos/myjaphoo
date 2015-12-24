/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.player;

import java.util.Collection;
import org.myjaphoo.gui.movietree.AbstractLeafNode;

/**
 *
 * @author mla
 */
public abstract class Player {

    abstract public void playMovies(Collection<AbstractLeafNode> entries);
}
