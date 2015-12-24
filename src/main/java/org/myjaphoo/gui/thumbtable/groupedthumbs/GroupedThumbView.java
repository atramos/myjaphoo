/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable.groupedthumbs;

import java.util.List;

/**
 *
 * @author mla
 */
public class GroupedThumbView {

    private List<ThumbStripe> stripes;
    private int colmax = 10;

    public GroupedThumbView(List<ThumbStripe> stripes) {
        this.stripes = stripes;
    }

    /**
     * @return the stripes
     */
    public List<ThumbStripe> getStripes() {
        return stripes;
    }

    public void prepareBreakDown(int colmax) {
        this.colmax = colmax;
        // prepare breakdowns of the stripes
        for (ThumbStripe stripe : getStripes()) {
            stripe.buildBreakDownChildrenStructure(colmax);
        }
    }



    /**
     * @return the colmax
     */
    public int getColmax() {
        return colmax;
    }

    public int getNumOfNodes() {
        int num = 0;
        for (ThumbStripe stripe: stripes) {
            num+= stripe.getMovieNodes().size();
        }
        return num;
    }
}
