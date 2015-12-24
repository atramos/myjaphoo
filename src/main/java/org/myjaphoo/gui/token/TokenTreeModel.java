/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.token;

import java.util.List;
import javax.swing.tree.TreePath;


import org.mlsoft.eventbus.GlobalBus;
import org.mlsoft.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.myjaphoo.gui.util.WrappedNode;
import org.myjaphoo.model.EntitySet;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.cache.ChangeSet;
import org.myjaphoo.model.cache.events.MetaTagsAssignedEvent;
import org.myjaphoo.model.cache.events.TagsAddedEvent;
import org.myjaphoo.model.cache.events.TagsAssignedEvent;
import org.myjaphoo.model.cache.events.TagsDeletedEvent;
import org.myjaphoo.model.cache.events.TagsUnassigendEvent;
import org.myjaphoo.model.db.Token;

/**
 *
 * @author lang
 */
public class TokenTreeModel extends AbstractTokenTreeModel {

    public static final Logger logger = LoggerFactory.getLogger(TokenTreeModel.class);

    /**
     * Erzeugt eine Instanz von {@link MetadataTreeModel}.
     * @param  newroot Wurzel
     */
    public TokenTreeModel(Token newroot, boolean isFlat) {
        super(newroot, false, null, isFlat, true);
        GlobalBus.bus.register(this);
    }

    public void dispose() {
        GlobalBus.bus.unregister(this);
    }

    /**
     * {@inheritDoc}
     * editierung eines tokennames
     * @param  path {@inheritDoc}
     * @param  newValue {@inheritDoc}
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        try {
            WrappedNode<Token> tokennode = (WrappedNode<Token>) path.getLastPathComponent();
            Token token = tokennode.getRef();
            token.setName((String) newValue);
            CacheManager.getCacheActor().editToken(token);
        } catch (Exception ex) {
            logger.error("error", ex); //NOI18N
        }

    }

    @Subscribe(onETD = true)
    public void tagassigned(TagsAssignedEvent tae) {
        updateTagNodes(tae);
    }

    @Subscribe(onETD = true)
    public void metatagassigned(MetaTagsAssignedEvent tae) {
        updateTagNodes(tae);
    }

    @Subscribe(onETD = true)
    public void tagunAssigned(TagsUnassigendEvent tae) {
        updateTagNodes(tae);
    }

    @Subscribe(onETD = true)
    public void tagsAdded(TagsAddedEvent tae) {
        // tags where added:
        for (Token newTag : tae.getTokenSet().asList()) {
            addedNewNodeObject(newTag.getParent(), newTag);
        }
    }

    @Subscribe(onETD = true)
    public void tagsDeleted(TagsDeletedEvent tae) {
        // tags where deleted:
        for (Token newTag : tae.getTokenSet().asList()) {
            removeNodeObject(newTag.getParent(), newTag);
        }
    }

    private void updateTagNodes(ChangeSet tae) {
        final EntitySet<Token> tokenSet = CacheManager.getCacheActor().getImmutableModel().getTokenSet();
        final List<Token> changedTags = tae.getTokenSet().asList();
        List<Token> newVersionsOfChangedTags = tokenSet.getReferences(changedTags);
        updateNodes(newVersionsOfChangedTags);
    }
}
