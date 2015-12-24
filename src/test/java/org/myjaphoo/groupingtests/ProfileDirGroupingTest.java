/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.groupingtests;

import junit.framework.TestCase;
import org.myjaphoo.LoggingConfiguration;
import org.myjaphoo.MovieDataBaseFilter;
import org.myjaphoo.MovieFilterController;
import org.myjaphoo.datasets.TestSet;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.movietree.grouping.GroupAlgorithm;
import org.myjaphoo.mocks.MockCacheActor;
import org.myjaphoo.mocks.PathMappingCacheMock;
import org.myjaphoo.model.StructureType;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.ChronicEntry;

import java.util.List;

/**
 * Testfall fürs Profilen (Zeitmessen) von Gruppierungen.
 * @author mla
 */
public class ProfileDirGroupingTest extends TestCase {

    private static TestSet set = DataTestSets.createTestSet1(10000, 100, 200);

    static {
        // init log4j:
        LoggingConfiguration.configurate();
        CacheManager.setCacheActor(new MockCacheActor(set.entries, set.tokens, set.metaTokens));
        CacheManager.setPathMappingCache(new PathMappingCacheMock());
    }

    public void testNoGroupingFiltersMovies() {
        MovieDataBaseFilter mdbf = new MovieDataBaseFilter();

        ChronicEntry chronic = new ChronicEntry();
        chronic.getView().setUserDefinedStruct(StructureType.DIRECTORY.buildUserDefinedEquivalentExpr());
        startGrouping(chronic, mdbf, StructureType.DIRECTORY.toString());
    }

    private void startGrouping(ChronicEntry chronic, MovieDataBaseFilter mdbf, String descr) {
        List<? extends GroupAlgorithm> grouper = MovieFilterController.createGroupingAlgorithm(chronic);
        MovieStructureNode root = (MovieStructureNode) mdbf.createStructuredTreeModel(grouper, chronic.getView(), false);
    }
}
