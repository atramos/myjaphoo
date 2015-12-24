/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.groupbyparser.expr;

import org.myjaphoo.model.groupbyparser.StandardGroupingSymbols;
import org.myjaphoo.model.grouping.PartialPathBuilder;

/**
 *
 * @author mla
 */
public class StandardGrouping extends Grouping {

    private StandardGroupingSymbols standardGrouping;

    public StandardGrouping(StandardGroupingSymbols standardGrouping) {
        this.standardGrouping = standardGrouping;
    }

    @Override
    public PartialPathBuilder createPartialPathBuilder() {
        return standardGrouping.getDim().createPartialPathBuilder();
    }

    @Override
    public String getDisplayExprTxt() {
        return standardGrouping.getDim().name();
    }
}
