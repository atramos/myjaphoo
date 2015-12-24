/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.thumbtable.groupedthumbs.GroupedThumbView;
import org.myjaphoo.gui.thumbtable.groupedthumbs.ThumbStripe;

/**
 *
 * @author lang
 */
public class DistinctFilter {

    HashSet<String> paths = new HashSet<String>(50000);

    public DistinctFilter() {
    }

    public boolean isNew(AbstractLeafNode node) {
        return paths.contains(node.getCanonicalPath());
    }

    public boolean add(AbstractLeafNode node) {
        return paths.add(node.getCanonicalPath());
    }

    public List<AbstractLeafNode> filter(List<AbstractLeafNode> currentDisplayedMovieNodes) {
        ArrayList<AbstractLeafNode> result = new ArrayList<AbstractLeafNode>(currentDisplayedMovieNodes.size());
        for (AbstractLeafNode node : currentDisplayedMovieNodes) {
            if (add(node)) {
                result.add(node);
            }
        }
        return result;
    }

    public void filter(GroupedThumbView group) {
        for (ThumbStripe stripe : group.getStripes()) {
            stripe.setMovieNodes(filter(stripe.getMovieNodes()));
        }
    }
}
