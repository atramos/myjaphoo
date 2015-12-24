/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.eventbus;

import java.awt.EventQueue;
import java.util.concurrent.Executor;

/**
 *
 * @author lang
 */
public class EventBusses {

    public static EventBus createForwardedEventBus(EventBus forwardToNewOne) {
        EventBus bus = new BasicEventBus();
        wireForwarding(forwardToNewOne, bus);
        return bus;
    }

    private static class Forwarding {
        private EventBus forwardThisOne;
        private EventBus toThatOne;

        public Forwarding(EventBus forwardThisOne, EventBus toThatOne) {
            this.forwardThisOne = forwardThisOne;
            this.toThatOne = toThatOne;
            forwardThisOne.register(this);
        }
        
        @Subscribe
        public void forwardEvent(Object o) {
            toThatOne.post(o);
        }
    }
    
    public static void wireForwarding(EventBus forwardThisOne, EventBus toThatOne) {
        new Forwarding(forwardThisOne, toThatOne);
    }
}
