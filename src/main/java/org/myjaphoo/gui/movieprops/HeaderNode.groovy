package org.myjaphoo.gui.movieprops

import groovy.transform.TypeChecked

/**
 * HeaderNode 
 * @author mla
 * @version $Id$
 *
 */
@TypeChecked
class HeaderNode extends PropertyNode {

    public HeaderNode(PropertyNode parent, String name) {
        super(parent);
        this.name = name;
        this.editable = false;
        this.shouldBeExpanded = true;
    }
}
