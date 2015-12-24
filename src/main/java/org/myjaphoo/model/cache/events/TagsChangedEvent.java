/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.Token;

/**
 * Event when tags get changed.
 * @author lang
 */
public class TagsChangedEvent extends AbstractTagsChangedEvent {

    public TagsChangedEvent(Token... tokens) {
        super(tokens);
    }

    public TagsChangedEvent(Collection<Token> tokens) {
        super(tokens);
    }
}
