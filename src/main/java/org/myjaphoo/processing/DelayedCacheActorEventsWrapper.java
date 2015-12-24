/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.processing;

import org.myjaphoo.model.cache.CacheManager;

/**
 *
 * @author mla
 */
public class DelayedCacheActorEventsWrapper<T> implements ListProcessor<T> {

    private ListProcessor<T> delegator;

    public DelayedCacheActorEventsWrapper(ListProcessor<T> delegator) {
        this.delegator = delegator;
    }

    @Override
    public void startProcess() {
        delegator.startProcess();
        CacheManager.getCacheActor().accumulateEvents();
    }

    @Override
    public synchronized void process(final T entry) throws Exception {
        delegator.process(entry);
    }

    @Override
    public void stopProcess() {
        CacheManager.getCacheActor().fireAllAccumulatedEvents();
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
