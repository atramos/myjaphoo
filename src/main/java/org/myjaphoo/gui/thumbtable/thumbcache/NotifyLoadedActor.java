/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable.thumbcache;

import groovyx.gpars.actor.DynamicDispatchActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Separate actor that notifies any call back listener that an thumb has been loaded and is now available in
 * the cache.
 * We use a separate actor to parallelize this event handling and get the main actor free of work
 *
 * @author mla
 */
public class NotifyLoadedActor extends DynamicDispatchActor {

    private static final Logger logger = LoggerFactory.getLogger(ThumbLoadActor.class);

    public NotifyLoadedActor() {

    }


    public void onMessage(ThumbNowLoadedMsg msg) {
        try {
            msg.getLoadCallBack().notifyIsLoaded(msg);
        } catch (Exception e) {
            logger.error("error in notify loaded actor thread!", e);
        }

    }
}
