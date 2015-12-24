/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.groupbyparser;

import org.myjaphoo.model.filterparser.syntaxtree.SpecialCharSymbol;

/**
 *
 * @author mla
 */
public class GroupingSymbols extends SpecialCharSymbol {

    public static final GroupingSymbols GROUPBY = new GroupingSymbols("groupby");
    public static final GroupingSymbols GROUP = new GroupingSymbols("group");
    public static final GroupingSymbols BY = new GroupingSymbols("by");
    public static final GroupingSymbols IF = new GroupingSymbols("if");
    public static final GroupingSymbols ELSE = new GroupingSymbols("else");
    public static final GroupingSymbols ELSEIF = new GroupingSymbols("elseif");
    public static final GroupingSymbols CBRACEOPEN = new GroupingSymbols("{");
    public static final GroupingSymbols CBRACECLOSE = new GroupingSymbols("}");

    private GroupingSymbols(String sym) {
        super(sym);
    }
}
