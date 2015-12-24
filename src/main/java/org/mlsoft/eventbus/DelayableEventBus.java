/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.eventbus;

import java.util.ArrayList;

/**
 * a event bus which could accumulate events, and then
 * post them later at once.
 *
 * It simply accumulates events and then posts to a delegator bus.
 *
 * @author lang
 */
public class DelayableEventBus implements EventBus {

    private EventBus delegatedBus;
    private boolean accumulate = false;
    private ArrayList<Object> accumulatedEvents = new ArrayList<Object>();

    public DelayableEventBus(EventBus delegatedBus) {
        this.delegatedBus = delegatedBus;
    }

    @Override
    public void register(Object subscriber) {
        delegatedBus.register(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        delegatedBus.unregister(subscriber);
    }

    @Override
    public void post(Object event) {
        if (accumulate) {
            accumulatedEvents.add(event);
        } else {
            delegatedBus.post(event);
        }
    }

    final public void accumulateEvents() {
        accumulate = true;

    }

    final public void fireAllAccumulatedEvents() {
        accumulate = false;
        for (Object event : accumulatedEvents) {
            delegatedBus.post(event);
        }
        accumulatedEvents.clear();
    }
}
