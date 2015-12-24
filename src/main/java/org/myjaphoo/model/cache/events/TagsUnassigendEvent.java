/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.Token;

/**
 * Event when tags get unassigned.
 * @author lang
 */
public class TagsUnassigendEvent extends AbstractTagsChangedEvent {

    public TagsUnassigendEvent(Token... tokens) {
        super(tokens);
    }

    public TagsUnassigendEvent(Collection<Token> tokens) {
        super(tokens);
    }
}
