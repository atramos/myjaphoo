/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.cache.ChangeSet;
import org.myjaphoo.model.db.MetaToken;

/**
 * base event class for changes on meta tags.
 * @author lang
 */
public class AbstractMetaTagsChangedEvent extends ChangeSet {

    public AbstractMetaTagsChangedEvent(MetaToken... tokens) {
        add(tokens);
    }

    public AbstractMetaTagsChangedEvent(Collection<MetaToken> tokens) {
        add(tokens.toArray(new MetaToken[tokens.size()]));
    }
}
