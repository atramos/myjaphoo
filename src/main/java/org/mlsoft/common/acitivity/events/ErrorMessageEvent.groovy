package org.mlsoft.common.acitivity.events

import groovy.transform.Immutable
import org.mlsoft.common.acitivity.Channel

/**
 * MessageEvent 
 * @author mla
 * @version $Id$
 *
 */
@Immutable(knownImmutables = ["channel", "t"])
class ErrorMessageEvent {
    def Channel channel;
    def String errorMessage;
    def Throwable t;
}
