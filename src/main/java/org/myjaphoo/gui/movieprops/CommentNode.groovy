package org.myjaphoo.gui.movieprops

import org.myjaphoo.model.db.AttributedEntity

/**
 * CommentNode 
 * @author mla
 * @version $Id$
 *
 */
class CommentNode extends EditableNode {

    public CommentNode(PropertyNode parent, AttributedEntity attributedEntity) {
        super(parent, "Comment", attributedEntity.comment);
    }
}
