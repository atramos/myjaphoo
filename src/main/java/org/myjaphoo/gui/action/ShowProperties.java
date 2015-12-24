/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;
import org.apache.commons.lang.time.StopWatch;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.mlsoft.common.acitivity.DefaultChannel;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.WmEntitySet;
import org.myjaphoo.model.logic.MyjaphooDB;

/**
 * Clears the entity manager cache.
 * @author lang
 */
public class ShowProperties extends AbstractWankmanAction {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/action/resources/ShowProperties");

    static ArrayList<WmEntitySet> sets = new ArrayList<WmEntitySet>();

    public ShowProperties(MyjaphooController controller) {
        super(controller, localeBundle.getString("SHOW PROPERTIES"), null);
    }

    @Override
    public void run(final MyjaphooController controller, ActionEvent e, ViewContext context)  {
        // for now, just log the infos to the log window:
        DefaultChannel channel = new DefaultChannel(ShowProperties.class, "Application Properties");
        Enumeration enumeration = System.getProperties().propertyNames();
        while(enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            String prop = System.getProperty(name);
            log(channel, name, prop);
        }
        // print hibernate statistics:
        Statistics stats = MyjaphooDB.singleInstance().getStatistics();
        stats.setStatisticsEnabled(true);
        log(channel, "close statement count", stats.getCloseStatementCount()); //NOI18N
        log(channel, "collection fetch count", stats.getCollectionFetchCount()); //NOI18N
        log(channel, "collection load count", stats.getCollectionLoadCount()); //NOI18N
        log(channel, "collection recreate count", stats.getCollectionRecreateCount()); //NOI18N
        log(channel, "collection remove count", stats.getCollectionRemoveCount()); //NOI18N
        log(channel, "collection update count", stats.getCollectionUpdateCount()); //NOI18N
        log(channel, "connect count", stats.getConnectCount()); //NOI18N
        log(channel, "entity delete count", stats.getEntityDeleteCount()); //NOI18N
        log(channel, "entity fetch count", stats.getEntityFetchCount()); //NOI18N
        log(channel, "entity insert count", stats.getEntityInsertCount()); //NOI18N
        log(channel, "entity load count", stats.getEntityLoadCount()); //NOI18N
        log(channel, "entity update count", stats.getEntityUpdateCount()); //NOI18N
        log(channel, "flush count", stats.getFlushCount()); //NOI18N
        log(channel, "optimistic failure count", stats.getOptimisticFailureCount()); //NOI18N
        log(channel, "prepare statement count", stats.getPrepareStatementCount()); //NOI18N
        log(channel, "query cache hit count", stats.getQueryCacheHitCount()); //NOI18N
        log(channel, "query cache miss count", stats.getQueryCacheMissCount()); //NOI18N
        log(channel, "query cache put count", stats.getQueryCachePutCount()); //NOI18N
        log(channel, "query execution count", stats.getQueryExecutionCount()); //NOI18N
        log(channel, "query execution max time", stats.getQueryExecutionMaxTime()); //NOI18N
        log(channel, "query execution max time query string", stats.getQueryExecutionMaxTimeQueryString()); //NOI18N
        for (String entityName : stats.getEntityNames()) {
            EntityStatistics entStats = stats.getEntityStatistics(entityName);
            logEntityStats(channel, entStats);
        }


        // test deep copy of cached model:
        WmEntitySet wmset = CacheManager.getCacheActor().getImmutableModel();
        // measure time to copy this:
        StopWatch w = new StopWatch();
        w.start();
        WmEntitySet clonedCopy = wmset.deepCopyClone();
        //sets.add(clonedCopy);
        w.stop();
        log(channel, "time to deep copy model: ", w.toString()); //NOI18N

    }

    private void log(DefaultChannel channel, String name, String arg) {
        channel.message(name + " = " + arg); //NOI18N
    }

    private void log(DefaultChannel channel, String name, long l) {
        channel.message(name + " = " + l); //NOI18N
    }

    private void logEntityStats(DefaultChannel channel, EntityStatistics entStats) {
        /*
        log(channel, entStats.getCategoryName() + " delete count", entStats.getDeleteCount()); //NOI18N
        log(channel, entStats.getCategoryName() + " fetch count", entStats.getFetchCount()); //NOI18N
        log(channel, entStats.getCategoryName() + " insert count", entStats.getInsertCount()); //NOI18N
        log(channel, entStats.getCategoryName() + " load count", entStats.getLoadCount()); //NOI18N
        log(channel, entStats.getCategoryName() + " optimistic failure count", entStats.getOptimisticFailureCount()); //NOI18N
        log(channel, entStats.getCategoryName() + " update count" , entStats.getUpdateCount()); //NOI18N
        */
    }
}
