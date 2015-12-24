/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.processing;

import org.myjaphoo.model.logic.MyjaphooDB;

/**
 *
 * @author mla
 */
public class DelayedProcessWrapper<T> implements ListProcessor<T> {

    private ListProcessor<T> delegator;

    public DelayedProcessWrapper(ListProcessor<T> delegator) {
        this.delegator = delegator;
    }

    @Override
    public void startProcess() {
        delegator.startProcess();
    }

    @Override
    public synchronized void process(final T entry) throws Exception {
        MyjaphooDB.singleInstance().emClear();
        this.wait(150);
        delegator.process(entry);
    }

    @Override
    public void stopProcess() {
        delegator.stopProcess();
    }

    @Override
    public String shortName(T t) {
        return delegator.shortName(t);
    }

    @Override
    public String longName(T t) {
        return delegator.shortName(t);
    }
};
