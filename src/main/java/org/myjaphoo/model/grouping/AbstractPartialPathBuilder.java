/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.myjaphoo.model.grouping;

/**
 *
 * @author mla
 */
public abstract class AbstractPartialPathBuilder implements PartialPathBuilder {

    private GroupingExecutionContext context;

    @Override
    public void preProcess(GroupingExecutionContext context) {
        this.context = context;
    }

    /**
     * @return the context
     */
    public GroupingExecutionContext getContext() {
        return context;
    }
}
