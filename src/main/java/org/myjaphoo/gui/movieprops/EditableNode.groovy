package org.myjaphoo.gui.movieprops

import groovy.transform.TypeChecked

/**
 * EditableNode 
 * @author mla
 * @version $Id$
 *
 */
@TypeChecked
class EditableNode extends PropertyNode {

    def boolean edited;

    public EditableNode(PropertyNode parent, String name, String value) {
        super(parent);
        this.name = name;
        this.value = value;
        this.editable = true;
        edited = false;
    }

    public void setValue(String val) {
        if (val != getValue()) {
            super.setValue(val);
            edited = true;
        }
    }

    public boolean hasBeenChanged() { return edited; }
}