/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Helper Methods for dispatching code within the AWT Event Dispatch Thread.
 * 
 * 
 * 
 * @author mla
 */
public class EventDispatchTools {

    private static final Logger logger = LoggerFactory.getLogger(EventDispatchTools.class);

    static class OnEDTDispatcher implements InvocationHandler {

        private Object implClass;

        public OnEDTDispatcher(final Object implClass) {
            this.implClass = implClass;
        }

        @Override
        public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
            if (method.getReturnType() == Void.TYPE) {
                // methods without return value are executed as "invokeLater":
                onEDT(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            method.invoke(implClass, args);
                        } catch (Exception ex) {
                            throw new RuntimeException("error during dispatching method in awt edt thread!", ex); //NOI18N
                        }
                    }
                });

                return null;

            } else {

                throw new RuntimeException("can not dispatch methods with return value via the AWT event dispatch thread!"); //NOI18N

            }
        }
    };

    /**
     * Ensures that the given runnable is executed within the EDT Thread.
     * @param runnable 
     */
    public static void onEDT(Runnable runnable) {
        if (EventQueue.isDispatchThread()) {
            // its already the ETD thread, just execute here:
            runnable.run();
        } else {
            // put into the event queue:
            EventQueue.invokeLater(runnable);
        }
    }

    public static void onEDTWait(Runnable runnable) {
        if (EventQueue.isDispatchThread()) {
            // its already the ETD thread, just execute here:
            runnable.run();
        } else {
            // put into the event queue:
            try {
                EventQueue.invokeAndWait(runnable);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Creates a proxy object for the interface and class implementation which dispatches
     * all method calls within the AWT Event Dispatch Thread.
     * 
     * @param <T>
     * @param theInterface
     * @param theImpl
     * @return 
     */
    public static <T> T createEDTDispatchingProxy(Class<T> theInterface, final T theImpl) {
        return (T) Proxy.newProxyInstance(theImpl.getClass().getClassLoader(),
                new Class[]{theInterface},
                new OnEDTDispatcher(theImpl));
    }
}
