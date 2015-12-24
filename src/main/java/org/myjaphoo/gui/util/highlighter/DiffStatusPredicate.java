package org.myjaphoo.gui.util.highlighter;

import org.myjaphoo.gui.movietree.DiffNode;
import org.myjaphoo.model.dbcompare.CompareResult;

/**
 * a predicate that decides by the diff status of a diffnode entry to highlight.
 */
public class DiffStatusPredicate extends ClassBasedPredicate<DiffNode> {
    private CompareResult diff;

    public DiffStatusPredicate(CompareResult diff) {
        super(DiffNode.class);
        this.diff = diff;
    }

    @Override
    public boolean isHighlighted(DiffNode node) {
        return node.getDbdiff().getDiffEntry() == diff;
    }
};