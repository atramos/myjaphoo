/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.groupbyparser.expr;

import org.myjaphoo.model.grouping.PartialPathBuilder;

/**
 *
 * @author mla
 */
public abstract class Grouping {

    abstract public PartialPathBuilder createPartialPathBuilder();

    abstract public String getDisplayExprTxt();
}
