/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.MetaToken;

/**
 * Event, when meta tags get deleted.
 * @author lang
 */
public class MetaTagsDeletedEvent extends AbstractMetaTagsChangedEvent {

    public MetaTagsDeletedEvent(MetaToken... tokens) {
        super(tokens);
    }

    public MetaTagsDeletedEvent(Collection<MetaToken> tokens) {
        super(tokens);
    }
}
