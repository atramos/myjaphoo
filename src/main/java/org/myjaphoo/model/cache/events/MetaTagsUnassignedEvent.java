/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.MetaToken;

/**
 * Event when meta tags get unassigned from tags.
 * @author lang
 */
public class MetaTagsUnassignedEvent extends AbstractMetaTagsChangedEvent {

    public MetaTagsUnassignedEvent(MetaToken... tokens) {
        super(tokens);
    }

    public MetaTagsUnassignedEvent(Collection<MetaToken> tokens) {
        super(tokens);
    }
}
