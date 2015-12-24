/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.dbcompare;

import org.mlsoft.common.acitivity.Channel;
import org.mlsoft.common.acitivity.ChannelManager;
import org.mlsoft.eventbus.GlobalBus;
import org.myjaphoo.gui.thumbtable.thumbcache.ThreadedThumbCache;
import org.myjaphoo.model.cache.EntityCacheActor;
import org.myjaphoo.model.cache.EntityCacheActorImpl;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.logic.dbhandling.ThreadLocalTransactionBoundaryHandler;
import org.myjaphoo.model.logic.dbhandling.TransactionBoundaryDelegator;

import java.util.ArrayList;
import java.util.ResourceBundle;


/**
 * Alle Vergleichsfunktionen mit einer weiteren Datenbank werden über diese Klasse geregelt.
 * Diese Klasse hält alle notwendigen Informationen, um vergleiche anzustellen, bzw. vergleichsinfos zu liefern.
 *
 * @author
 */
public class DatabaseComparison {
    public static final ResourceBundle BUNDLE = java.util.ResourceBundle.getBundle("org/myjaphoo/model/dbcompare/resources/DatabaseComparison");

    private ComparisonMethod comparisonMethod = ComparisonMethod.COMPARE_CHECKSUM;

    private static DatabaseComparison instance = null;

    private DatabaseConfiguration config;
    private EntityCacheActor cacheActor = null;
    private DBConnection dBConnection = null;
    private ThreadedThumbCache thumbCache;


    private DatabaseComparison() {
    }

    public static DatabaseComparison getInstance() {
        if (instance == null) {
            instance = new DatabaseComparison();
        }
        return instance;
    }


    public EntityCacheActor getCacheActor() {
        return cacheActor;
    }

    private EntityCacheActor initCacheActor() {
        if (cacheActor == null) {
            dBConnection = new DBConnection(config);
            dBConnection.open();
            cacheActor = new EntityCacheActorImpl(dBConnection, GlobalBus.bus);
        }
        return cacheActor;
    }


    public void openComparisonDatabase(DatabaseConfiguration config) {
        this.config = (DatabaseConfiguration) config.clone();

        Channel channel = ChannelManager.createChannel(this.getClass(), "open and read comparison database " + config.getFilledConnectionUrl()); //NOI18N
        channel.startActivity();

        try {
            initCacheActor();
        } finally {
            channel.stopActivity();
        }

    }

    public void closeComparisonDatabase() {
        // clear the cached data:
        if (thumbCache != null) {
            thumbCache.clearCache();
        }
        if (dBConnection != null) {
            dBConnection.close();
        }
        cacheActor = null;
        Channel channel = ChannelManager.createChannel(this.getClass(), "closing comparison database " + config.getFilledConnectionUrl()); //NOI18N
        channel.startActivity();
        channel.stopActivity();
    }

    public boolean isActive() {
        return cacheActor != null;
    }

    public boolean hasSameEntry(MovieEntry entry) {
        if (cacheActor == null) {
            return false;
        }
        return cacheActor.getImmutableModel().getDupHashMap().containsEntry(entry.getChecksumCRC32());
    }

    public ArrayList<MovieEntry> getDups(MovieEntry entry) {
        if (cacheActor == null) {
            return null;
        }
        return cacheActor.getImmutableModel().getDupHashMap().getDuplicatesForCheckSum(entry.getChecksumCRC32());
    }

    public int getComparisonColor(MovieEntry entry) {
        if (cacheActor == null) {
            return 0;
        }
        if (cacheActor.getImmutableModel().getDupHashMap().containsEntry(entry.getChecksumCRC32())) {
            return 1;
        } else {
            return 2;
        }
    }

    public String getCategoryName(MovieEntry entry) {
        if (cacheActor == null) {
            return BUNDLE.getString("NO COMPARISON AVAILABLE");
        }
        if (cacheActor.getImmutableModel().getDupHashMap().containsEntry(entry.getChecksumCRC32())) {
            return BUNDLE.getString("EXISTS_IN_COMPARISON_DB");
        } else {
            return BUNDLE.getString("EXISTS_NOT_IN_COMPARISON_DB");
        }
    }

    public ThreadedThumbCache getThumbCache() {
        if (cacheActor == null) {
            return null;
        }
        if (thumbCache == null) {
            thumbCache = new ThreadedThumbCache(dBConnection);
        }
        return thumbCache;
    }

    public TransactionBoundaryDelegator createTransaction() {
        return new ThreadLocalTransactionBoundaryHandler(dBConnection);
    }

    public ComparisonMethod getComparisonMethod() {
        return comparisonMethod;
    }

    public void setComparisonMethod(ComparisonMethod comparisonMethod) {
        this.comparisonMethod = comparisonMethod;
    }

    public static class Info {

        public boolean isComparisonDBOpened;
        public String comparisonDBName = ""; //NOI18N
        public int numOfMovies;
        public long sizeOfMovies;
        public int numOfDuplicates;
        public long wastedMem;
    }

    public Info getInfo() {
        Info info = new Info();
        info.isComparisonDBOpened = cacheActor != null;
        if (info.isComparisonDBOpened) {
            info.comparisonDBName = config.getFilledConnectionUrl();
            info.numOfMovies = cacheActor.getImmutableModel().getMovies().size();
            info.numOfDuplicates = cacheActor.getImmutableModel().getDupHashMap().calcDuplicationCount();
            info.wastedMem = cacheActor.getImmutableModel().getDupHashMap().calcWastedMem();
        }
        return info;
    }
}
