/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.eventbus;

import org.mlsoft.eventbus.BasicEventBus.Subscription;

/**
 *
 * @author lang
 */
class BusExceptionEvent {

    private Subscription subscriberInfo;
    private Throwable cause;

    public BusExceptionEvent(Subscription subscriberInfo, Throwable cause) {
        this.subscriberInfo = subscriberInfo;
        this.cause = cause;
    }

    /**
     * @return the cause
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * @return the subscriberInfo
     */
    public Subscription getSubscriberInfo() {
        return subscriberInfo;
    }
}
