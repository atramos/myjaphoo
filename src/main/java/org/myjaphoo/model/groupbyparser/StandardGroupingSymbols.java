/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.groupbyparser;

import org.myjaphoo.model.grouping.GroupingDim;
import org.myjaphoo.model.filterparser.syntaxtree.SpecialCharSymbol;

/**
 *
 * @author mla
 */
public class StandardGroupingSymbols extends SpecialCharSymbol {

    private GroupingDim dim;

    // init the grouping symbols based on the grouping dims:
    static {
        for (GroupingDim dim: GroupingDim.values()) {
            // mit dem namen lowercase initialisieren:
            new StandardGroupingSymbols(dim, dim.name().toLowerCase());
            // und für kompatiblität mit bisherigen user definierten gruppierungen
            // auch noch mal mit dem eigentlichen namen (camelcase oder so ähnlich)
            new StandardGroupingSymbols(dim, dim.name());
        }
    }

    public static void initMe() {
        // to initialize the static instances....
    }

    private StandardGroupingSymbols(GroupingDim dim, String sym) {
        super(sym);
        this.dim = dim;
    }

    /**
     * @return the dim
     */
    public GroupingDim getDim() {
        return dim;
    }
    
}
