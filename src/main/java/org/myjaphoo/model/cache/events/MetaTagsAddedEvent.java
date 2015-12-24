/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.MetaToken;

/**
 * Event fired when adding meta tags
 * @author lang
 */
public class MetaTagsAddedEvent extends AbstractMetaTagsChangedEvent {

    public MetaTagsAddedEvent(MetaToken... tokens) {
        super(tokens);
    }

    public MetaTagsAddedEvent(Collection<MetaToken> tokens) {
        super(tokens);
    }
}
