/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.Token;

/**
 * Event when tags get deleted.
 * @author lang
 */
public class TagsDeletedEvent extends AbstractTagsChangedEvent {

    public TagsDeletedEvent(Token... tokens) {
        super(tokens);
    }

    public TagsDeletedEvent(Collection<Token> tokens) {
        super(tokens);
    }
}
