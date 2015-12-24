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
class ProgressEvent {
    def Channel channel;
    def int percentage;
    def int overallPercentage;
}
