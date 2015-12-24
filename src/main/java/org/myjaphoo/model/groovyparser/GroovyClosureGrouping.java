package org.myjaphoo.model.groovyparser;

import groovy.lang.Closure;
import org.myjaphoo.model.groupbyparser.expr.Grouping;
import org.myjaphoo.model.grouping.PartialPathBuilder;

/**
 * GroovyClosureGrouping
 *
 * @author lang
 * @version $Id$
 */
public class GroovyClosureGrouping extends Grouping {

    private Closure closure;

    public GroovyClosureGrouping(Closure closure) {
        this.closure = closure;
    }

    @Override
    public PartialPathBuilder createPartialPathBuilder() {
        return new GroovyClosurePathGrouper(closure);
    }

    @Override
    public String getDisplayExprTxt() {
        return "groovy closure";
    }
}
