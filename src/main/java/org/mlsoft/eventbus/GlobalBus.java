/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.eventbus;

/**
 * the global application wide event bus.
 * @author lang
 */
public class GlobalBus {
    
    public static final EventBus bus = new BasicEventBus();
}
