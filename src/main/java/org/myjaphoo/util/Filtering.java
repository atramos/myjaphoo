/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.util;

import org.myjaphoo.MovieNode;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.movietree.DiffNode;
import org.myjaphoo.model.db.MovieEntry;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author mla
 */
public class Filtering {


    public static ArrayList<MovieEntry> entries(Collection<AbstractLeafNode> nodes) {
        return nodes2Entries(filterMovieNodes(nodes));
    }

    public static ArrayList<MovieEntry> nodes2Entries(Collection<MovieNode> nodes) {
        if (nodes == null) {
            return null;
        }
        ArrayList<MovieEntry> movies = new ArrayList<MovieEntry>();
        for (MovieNode node : nodes) {
            if (node != null) {
                // scheinbar kann es vorkommen, dass nodes auch keinen movie enthalten können:
                // (kann durchs destruct entstehen).
                if (node.getMovieEntry() != null) {
                    movies.add(node.getMovieEntry());
                }
            }
        }
        return movies;
    }

    public static ArrayList<MovieNode> filterMovieNodes(Collection<AbstractLeafNode> nodes) {
        ArrayList<MovieNode> allmovNodes = new ArrayList<MovieNode>();
        for (AbstractLeafNode node : nodes) {
            if (node instanceof MovieNode) {
                allmovNodes.add((MovieNode) node);
            }
        }
        return allmovNodes;
    }

    public static ArrayList<DiffNode> filterDiffNodes(Collection<AbstractLeafNode> nodes) {
        ArrayList<DiffNode> allmovNodes = new ArrayList<DiffNode>();
        for (AbstractLeafNode node : nodes) {
            if (node instanceof DiffNode) {
                allmovNodes.add((DiffNode) node);
            }
        }
        return allmovNodes;
    }
}
