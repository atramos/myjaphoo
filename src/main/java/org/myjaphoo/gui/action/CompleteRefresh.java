/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.thumbtable.thumbcache.ThreadedThumbCache;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.logic.MyjaphooDB;

import java.awt.event.ActionEvent;

/**
 * Clears the entity manager cache, clears the caches and refreshes the view.
 * @author lang
 */
public class CompleteRefresh extends AbstractWankmanAction {

    public CompleteRefresh(MyjaphooController controller) {
        super(controller, "Clear all Caches & refresh View", null);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context) {
        invalidateAllCaches();
        controller.getView().updateAllViews();
    }

    public static void invalidateAllCaches() {
        ThreadedThumbCache.getInstance().clearCache();
        MyjaphooDB.singleInstance().emClear();
        CacheManager.getCacheActor().resetInternalCache();
        CacheManager.getCacheActor().resetImmutableCopy();
        CacheManager.getPathMappingCache().resetCache();
    }


}
