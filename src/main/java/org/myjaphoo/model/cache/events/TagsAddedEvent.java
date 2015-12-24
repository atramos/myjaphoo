/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.Token;

/**
 * Event when tags get added.
 * @author lang
 */
public class TagsAddedEvent extends AbstractTagsChangedEvent {

    public TagsAddedEvent(Token... tokens) {
        super(tokens);
    }

    public TagsAddedEvent(Collection<Token> tokens) {
        super(tokens);
    }
}
