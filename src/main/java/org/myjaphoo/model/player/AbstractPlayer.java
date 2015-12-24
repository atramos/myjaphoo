/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.player;

import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.model.logic.FileSubstitutionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author mla
 */
public abstract class AbstractPlayer extends Player {

    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractPlayer.class.getName());
    private FileSubstitutionImpl fileSubstitution = new FileSubstitutionImpl();

    protected void addSubstitutedMovieFileNames(ArrayList<String> args, Collection<AbstractLeafNode> entries) {
        for (AbstractLeafNode entry : entries) {
            args.add(fileSubstitution.substitude(entry.getCanonicalPath()));
        }
    }

}
