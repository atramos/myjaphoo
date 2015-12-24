package org.myjaphoo.gui.movieprops

/**
 * InfoNode 
 * @author mla
 * @version $Id$
 *
 */
class InfoNode extends PropertyNode {

    InfoNode(PropertyNode parent, String name, String value) {
        super(parent)
        this.name = name;
        this.value = value;
        this.editable = false;
    }
}
