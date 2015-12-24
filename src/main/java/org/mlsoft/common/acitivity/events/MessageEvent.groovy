package org.mlsoft.common.acitivity.events

import groovy.transform.Immutable
import org.mlsoft.common.acitivity.Channel

/**
 * MessageEvent 
 * @author mla
 * @version $Id$
 *
 */
@Immutable(knownImmutables = ["channel"])
class MessageEvent {
    def Channel channel;
    def String message;
    def boolean emphasised;
}
