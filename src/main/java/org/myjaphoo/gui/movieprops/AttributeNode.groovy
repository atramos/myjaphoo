package org.myjaphoo.gui.movieprops

import groovy.transform.TypeChecked
import org.myjaphoo.model.db.AttributedEntity

/**
 * AttributeNode 
 * @author mla
 * @version $Id$
 *
 */
@TypeChecked
public class AttributeNode extends EditableNode {

    public AttributeNode(AttributeHeaderNode parent, String name, String value) {
        super(parent, name, value);
    }

    public AttributedEntity getEntity() {
        ((AttributeHeaderNode)getParent()).attributedEntity;
    }
}
