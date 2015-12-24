/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.player;

import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.model.externalPrograms.ExternalPrograms;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author mla
 */
public class KMPlayerPlayer extends AbstractPlayer {

    @Override
    public void playMovies(final Collection<AbstractLeafNode> entries) {
        ArrayList<String> args = new ArrayList<String>();

        addSubstitutedMovieFileNames(args, entries);
        ExternalPrograms.KMPLAYER.startNoWait(args);
    }
}
