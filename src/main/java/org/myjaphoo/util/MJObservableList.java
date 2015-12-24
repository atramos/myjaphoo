package org.myjaphoo.util;

import groovy.util.ObservableList;

/**
 * MJObservableList
 *
 * @author lang
 * @version $Id$
 */
public class MJObservableList extends ObservableList {

    /**
     * fires a update change event for a element of the list
     * @param objectElementInList
     */
    public void fireListElementChanged(Object objectElementInList) {
        fireElementUpdatedEvent(indexOf(objectElementInList), null, "object change" );
    }
}
