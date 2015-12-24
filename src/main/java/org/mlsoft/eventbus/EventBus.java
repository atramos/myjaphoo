package org.mlsoft.eventbus;

/**

 * @author lang
 */
public interface EventBus {

    void register(Object subscriber);

    void unregister(Object subscriber);

    void post(Object event);
}
