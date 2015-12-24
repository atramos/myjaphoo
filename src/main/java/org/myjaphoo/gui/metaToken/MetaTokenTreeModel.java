/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.metaToken;

import org.apache.commons.lang.StringUtils;
import org.mlsoft.eventbus.GlobalBus;
import org.mlsoft.eventbus.Subscribe;
import org.myjaphoo.gui.editor.rsta.CachedHints;
import org.myjaphoo.gui.util.WrappedNode;
import org.myjaphoo.gui.util.WrappedNodeTreeModel;
import org.myjaphoo.model.EntitySet;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.cache.events.*;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author lang
 */
public class MetaTokenTreeModel extends WrappedNodeTreeModel<MetaToken> {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/metaToken/resources/MetaTokenTreeModel");
    public static final String[] COLUMNS = new String[]{
        localeBundle.getString("META TAG"),
        localeBundle.getString("DESCRIPTION"),
        localeBundle.getString("ASSIGNED TAGS")};
    public static final Logger LOGGER = LoggerFactory.getLogger(MetaTokenTreeModel.class.getName());

    public MetaTokenTreeModel(MetaToken newroot, boolean isFlat) {
        super(newroot, false, null, isFlat, prepareColumnList());
        isEditable = true;
        GlobalBus.bus.register(this);
    }

    private static String[] prepareColumnList() {
        ArrayList<String> colList = new ArrayList<>();
        colList.addAll(Arrays.asList(COLUMNS));

        // add all the attributes:
        colList.addAll(CachedHints.getMetaTagAttrKeys());
        return colList.toArray(new String[colList.size()]);
    }

    public void dispose() {
        GlobalBus.bus.unregister(this);
    }

    @Override
    protected boolean match(MetaToken tok, String typedText) {
        if (typedText == null) {
            return true;
        }
        return StringUtils.containsIgnoreCase(tok.getName(), typedText);
    }

    @Override
    public Object getValueAt(Object token, int column) {
        switch (column) {
            case 0:
                return ((WrappedNode<MetaToken>) token).getRef().getName();
            case 1:
                return ((WrappedNode<MetaToken>) token).getRef().getDescription();
            case 2:
                return createTokenDescr(((WrappedNode<MetaToken>) token).getRef());
            default:
                String attrName = getColumnName(column);
                return ((WrappedNode<MetaToken>) token).getRef().getAttributes().get(attrName);
        }
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        if (!isEditable) {
            return false;
        } else {
            return column == 0 || column == 1;
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 0 || column == 1) {
            return String.class;
        }
        return Object.class;
    }

    @Override
    public void setValueAt(Object value, Object node, int column) {
        if (!(column == 0 || column == 1 || column == 5)) {
            return;
        }
        WrappedNode<MetaToken> mnode = (WrappedNode<MetaToken>) node;
        MetaToken mt = mnode.getRef();
        try {
            if (column == 0) {
                mt.setName((String) value);
            }
            if (column == 1) {
                mt.setDescription((String) value);
            }
            CacheManager.getCacheActor().editMetaToken(mt);
        } catch (Exception ex) {
            LOGGER.error("error", ex); //NOI18N
            throw new RuntimeException(ex);
        }
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
            WrappedNode<MetaToken> token = (WrappedNode<MetaToken>) path.getLastPathComponent();

            token.getRef().setName((String) newValue);
            CacheManager.getCacheActor().editMetaToken(token.getRef());
        } catch (Exception ex) {
            LOGGER.error("error", ex); //NOI18N
        }

    }

    private Object createTokenDescr(MetaToken metaToken) {
        StringBuilder b = new StringBuilder();
        for (Token t : metaToken.getAssignedTokens()) {
            if (b.length() > 0) {
                b.append(";"); //NOI18N
            }
            b.append(t.getName());
        }
        return b.toString();
    }

    @Subscribe(onETD = true)
    public void tagassigned(MetaTagsAssignedEvent tae) {
        updateTagNodes(tae);
    }

    @Subscribe(onETD = true)
    public void tagunAssigned(MetaTagsUnassignedEvent tae) {
        updateTagNodes(tae);
    }

    @Subscribe(onETD = true)
    public void tagsAdded(MetaTagsAddedEvent tae) {
        // tags where added:
        for (MetaToken newTag : tae.getMetaTokenSet().asList()) {
            addedNewNodeObject(newTag.getParent(), newTag);
        }
    }
    
    @Subscribe(onETD = true)
    public void tagsDeleted(MetaTagsDeletedEvent tae) {
        // tags where deleted:
        for (MetaToken newTag : tae.getMetaTokenSet().asList()) {
            removeNodeObject(newTag.getParent(), newTag);
        }
    }      

    private void updateTagNodes(AbstractMetaTagsChangedEvent tae) {
        final EntitySet<MetaToken> metatagSet = CacheManager.getCacheActor().getImmutableModel().getMetaTokenSet();
        final List<MetaToken> changedMetaTags = tae.getMetaTokenSet().asList();
        List<MetaToken> newVersionsOfChangedMetaTags = metatagSet.getReferences(changedMetaTags);
        updateNodes(newVersionsOfChangedMetaTags);
    }
}
