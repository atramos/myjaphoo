/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;

import org.myjaphoo.model.FileType;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.picmode.FullPictureView;


import org.myjaphoo.gui.textviewer.TextViewer;
import org.myjaphoo.model.logic.FileSubstitutionImpl;
import org.myjaphoo.model.player.Player;

/**
 * Handles (delegates) the playing and viewing of media.
 * @author lang
 */
public class PlayerHandler {

    public static void playMovies(final List<AbstractLeafNode> nodes, Player player,
            List<AbstractLeafNode> allMediaForContinuousPictureViewing,
            JFrame frame) {
        if (AbstractLeafNode.any(FileType.Pictures, nodes)) {
            handlePictures(nodes, allMediaForContinuousPictureViewing, allMediaForContinuousPictureViewing, frame);
        } else if (AbstractLeafNode.any(FileType.Text, nodes)) {
            handleText(nodes, frame);
        } else {
            player.playMovies(nodes);
        }
    }

    private static void handlePictures(final List<AbstractLeafNode> nodes, List<AbstractLeafNode> allMediaForContinuousPictureViewing, List<AbstractLeafNode> allM, JFrame frame) {
        ArrayList<AbstractLeafNode> entries = new ArrayList<AbstractLeafNode>();
        if (nodes.size() == 1) {
            // wenn nur eins selektiert ist, dann nimm alle im view:
            for (AbstractLeafNode movieNode : allMediaForContinuousPictureViewing) {
                entries.add(movieNode);
            }
            Collections.rotate(entries, -allM.indexOf(nodes.get(0)));
        } else {
            for (AbstractLeafNode movieNode : nodes) {
                entries.add(movieNode);
            }
        }
        FullPictureView view = new FullPictureView(frame, entries);
    }

    private static void handleText(List<AbstractLeafNode> nodes, JFrame frame) {
        AbstractLeafNode node = nodes.get(0);
        if (node.is(FileType.Text)) {
            FileSubstitutionImpl subst = new FileSubstitutionImpl();
            String locatedFile = subst.locateFileOnDrive(node.getCanonicalPath());
            if (locatedFile != null) {
                TextViewer viewer = new TextViewer(locatedFile);
                viewer.setVisible(true);

            }
        }

    }
}
