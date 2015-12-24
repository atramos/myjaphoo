/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.MetaToken;

/**
 * Event when metatags are assigned to tags.
 * @author lang
 */
public class MetaTagsAssignedEvent extends AbstractMetaTagsChangedEvent {

    public MetaTagsAssignedEvent(MetaToken... tokens) {
        super(tokens);
    }

    public MetaTagsAssignedEvent(Collection<MetaToken> tokens) {
        super(tokens);
    }
}
