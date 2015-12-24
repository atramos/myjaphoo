/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import java.util.List;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.thumbtable.groupedthumbs.GroupedThumbView;

/**
 *
 * @author mla
 */
public class ThumbDisplayFilterResult {

    public enum ThumbDisplayFilterResultMode {

        PlAINLIST,
        SLIDESGROUPED
    }
    private ThumbDisplayFilterResultMode mode;
    private List<AbstractLeafNode> plainThumbList;
    private GroupedThumbView groupedThumbView;

    public ThumbDisplayFilterResult(List<AbstractLeafNode> plainThumbList) {
        this.plainThumbList = plainThumbList;
        this.mode = ThumbDisplayFilterResultMode.PlAINLIST;
    }

    public ThumbDisplayFilterResult(GroupedThumbView groupedThumbView) {
        this.groupedThumbView = groupedThumbView;
        this.mode = ThumbDisplayFilterResultMode.SLIDESGROUPED;
    }

    /**
     * @return the mode
     */
    public ThumbDisplayFilterResultMode getMode() {
        return mode;
    }

    /**
     * @return the plainThumbList
     */
    public List<AbstractLeafNode> getPlainThumbList() {
        if (mode != ThumbDisplayFilterResultMode.PlAINLIST) {
            throw new IllegalArgumentException();
        }
        return plainThumbList;
    }

    /**
     * @return the groupedThumbView
     */
    public GroupedThumbView getGroupedThumbView() {
        if (mode != ThumbDisplayFilterResultMode.SLIDESGROUPED) {
            throw new IllegalArgumentException();
        }
        return groupedThumbView;
    }

    public int getNumOfNodes() {
        switch (mode) {
            case PlAINLIST:
                return plainThumbList.size();
            case SLIDESGROUPED:
                return groupedThumbView.getNumOfNodes();
            default:
                throw new RuntimeException("internal error"); //NOI18N
        }
    }
}
