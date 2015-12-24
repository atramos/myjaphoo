package org.mlsoft.eventbus;

import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Very simple event bus implementation. Based on ideas from google guava
 * event bus, but much simpler and without any dependencies to any libraries.
 * @author lang
 */
public final class BasicEventBus implements EventBus {

    org.slf4j.Logger logger = LoggerFactory.getLogger(BasicEventBus.class);

    private final List<Subscription> subscriptions = new CopyOnWriteArrayList<>();
    /**
     * the executer service used.
     */
    private final Executor executorService;

    /**
     * Executor service used for guaranteed sequential delivering of events.
     */
    private final Executor singleThreadedExecutorService;

    public BasicEventBus() {
        this(Executors.newCachedThreadPool(new ThreadFactory() {

            private final ThreadFactory delegate = Executors.defaultThreadFactory();

            @Override
            public Thread newThread(Runnable r) {
                Thread t = delegate.newThread(r);
                t.setDaemon(true);
                return t;
            }
        }));
    }

    public BasicEventBus(Executor executorService) {
        this.executorService = executorService;
        this.singleThreadedExecutorService = Executors.newSingleThreadExecutor();
    }

    private boolean alreadySubscribed(Object subscriber) {
        for (Subscription subscription : subscriptions) {
            Object otherSubscriber = subscription.getSubscriber();
            if (subscriber == otherSubscriber) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void register(Object subscriber) {
        if (alreadySubscribed(subscriber)) {
            return;
        }

        Method[] methods = subscriber.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Subscribe eh = method.getAnnotation(Subscribe.class);
            if (eh != null) {
                Class<?>[] parameters = method.getParameterTypes();
                if (parameters.length != 1) {
                    throw new IllegalArgumentException("Only one argument allowed!");
                }
                // add subscriber
                Subscription s = new Subscription(parameters[0], method, subscriber, eh.onETD(), eh.sequential());
                subscriptions.add(s);
            }
        }
    }

    @Override
    public void unregister(Object subscriber) {
        List<Subscription> killList = new ArrayList<>();
        for (Subscription info : subscriptions) {
            Object obj = info.getSubscriber();
            if (obj == null || obj == subscriber) {
                killList.add(info);
            }
        }
        for (Subscription kill : killList) {
            subscriptions.remove(kill);
        }
    }

    @Override
    public void post(Object event) {
        notifySubscribers(event);
    }


    private void notifySubscribers(final Object evt) {
        for (final Subscription info : subscriptions) {
            if (info.matchesEvent(evt)) {

                EventRunner hc = new EventRunner(info, evt);
                //logger.debug("preparing execution of event dispatch {}",  evt);

                if (info.isOnETD()) {
                    EventQueue.invokeLater(hc);
                } else {
                    if (info.isSequential()) {
                        singleThreadedExecutorService.execute(hc);
                    } else {
                        executorService.execute(hc);
                    }
                }
            }
        }
    }

    public static class Subscription {

        private final Class<?> eventClass;
        private final Method method;
        private final WeakReference<?> subscriber;
        private final boolean onETD;
        private final boolean sequential;

        public Subscription(Class<?> eventClass, Method method, Object subscriber, boolean onETD, boolean sequential) {
            this.eventClass = eventClass;
            this.method = method;
            this.subscriber = new WeakReference<Object>(subscriber);
            this.onETD = onETD;
            this.sequential = sequential;
        }

        public boolean matchesEvent(Object event) {
            return (eventClass.isAssignableFrom(event.getClass()));
        }

        public Method getMethod() {
            return method;
        }

        public Object getSubscriber() {
            return subscriber.get();
        }

        public boolean isOnETD() {
            return onETD;
        }

        public boolean isSequential() {
            return sequential;
        }

        @Override
        public String toString() {
            return method.getDeclaringClass().getSimpleName() + "." + method.getName() + " onEDT=" + onETD;
        }


    }

    private class EventRunner implements Runnable {

        private final Subscription subscription;
        private final Object event;

        public EventRunner(Subscription handlerInfo, Object event) {
            this.subscription = handlerInfo;
            this.event = event;
        }

        @Override
        public void run() {

            //logger.debug("event run {} for subscription {}", event, subscription);
            try {
                Object subscriber = subscription.getSubscriber();
                if (subscriber != null) {
                    subscription.getMethod().invoke(subscriber, event);
                }
            } catch (Exception e) {
                post(new BusExceptionEvent(subscription, e));
                logger.error("error dispatching bus event for " + subscription.toString() + "!", e);
            }
        }
    }
}
