/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.cache.ChangeSet;
import org.myjaphoo.model.db.Token;

/**
 * base event class for changes on tags.
 * @author lang
 */
public class AbstractTagsChangedEvent extends ChangeSet {

    public AbstractTagsChangedEvent(Token... tokens) {
        add(tokens);
    }

    public AbstractTagsChangedEvent(Collection<Token> tokens) {
        addTags(tokens);
    }
}
