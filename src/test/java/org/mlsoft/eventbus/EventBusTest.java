/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.eventbus;

import java.awt.EventQueue;
import junit.framework.TestCase;

/**
 *
 * @author lang
 */
public class EventBusTest extends TestCase {

    public void testEventBusMechanism() throws InterruptedException {
        BasicEventBus bus = new BasicEventBus();
        EventBusChangeRecorder myRecorder = new EventBusChangeRecorder();

        bus.register(myRecorder);

        // ....
        bus.post(new ChangeEvent());

        Thread.sleep(1000);

        assertTrue(myRecorder.changeReceived);
    }

    public void testUIEventBusMechanism() throws InterruptedException {
        BasicEventBus bus = new BasicEventBus();
        EventBusChangeRecorderAWT myRecorder = new EventBusChangeRecorderAWT();

        bus.register(myRecorder);

        // ....
        bus.post(new ChangeEvent());

        Thread.sleep(1000);

        assertTrue(myRecorder.changeReceived);
        assertTrue(myRecorder.executedOnAWTEventThread);
    }

    public void testDerivedEventBusMechanism() throws InterruptedException {
        BasicEventBus bus = new BasicEventBus();
        EventBusChangeRecorderAWT myRecorder = new EventBusChangeRecorderAWT();

        bus.register(myRecorder);

        // ....
        bus.post(new DerivedChangeEvent());

        Thread.sleep(1000);

        assertTrue(myRecorder.changeReceived);
        myRecorder.changeReceived = false;

        bus.post(new IndependandEvent());
        assertFalse(myRecorder.changeReceived);
    }

    public void testForwardingMechanism() throws InterruptedException {
        BasicEventBus bus = new BasicEventBus();
        BasicEventBus forwardedToThis = new BasicEventBus();
        EventBusses.wireForwarding(bus, forwardedToThis);

        EventBusChangeRecorderAWT myRecorder = new EventBusChangeRecorderAWT();

        forwardedToThis.register(myRecorder);

        // ....
        bus.post(new DerivedChangeEvent());

        Thread.sleep(1000);

        assertTrue(myRecorder.changeReceived);

    }

    private static class IndependandEvent {
    }

    static class ChangeEvent {
    }

    static class DerivedChangeEvent extends ChangeEvent {
    }

    static class EventBusChangeRecorder {

        public boolean changeReceived = false;
        public boolean executedOnAWTEventThread = false;

        @Subscribe
        public void recordCustomerChange(ChangeEvent e) {
            System.out.println("change received");
            changeReceived = true;
            executedOnAWTEventThread = EventQueue.isDispatchThread();
        }
    }

    static class EventBusChangeRecorderAWT {

        public boolean changeReceived = false;
        public boolean executedOnAWTEventThread = false;

        @Subscribe(onETD = true)
        public void recordCustomerChange(ChangeEvent e) {
            System.out.println("change received");
            changeReceived = true;
            executedOnAWTEventThread = EventQueue.isDispatchThread();
        }
    }
}
