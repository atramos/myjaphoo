/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.MetaToken;

/**
 * Events when metatags get changed.
 * @author lang
 */
public class MetaTagsChangedEvent extends AbstractMetaTagsChangedEvent {

    public MetaTagsChangedEvent(MetaToken... tokens) {
        super(tokens);
    }

    public MetaTagsChangedEvent(Collection<MetaToken> tokens) {
        super(tokens);
    }
}
