/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.Token;

/**
 * Event when tags get assigned to movies.
 * @author lang
 */
public class TagsAssignedEvent extends AbstractTagsChangedEvent {

    public TagsAssignedEvent(Token... tokens) {
        super(tokens);
    }

    public TagsAssignedEvent(Collection<Token> tokens) {
        super(tokens);
    }
}
