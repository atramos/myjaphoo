package org.mlsoft.common.acitivity.events

import groovy.transform.Immutable
import org.mlsoft.common.acitivity.Channel

/**
 * ActivityStartedEvent
 * @author mla
 * @version $Id$
 */
@Immutable(knownImmutables = ["channel"])
public class ActivityStartedEvent {
    def Channel channel;
    def String activityName;
    def boolean nestedActivity;
}
