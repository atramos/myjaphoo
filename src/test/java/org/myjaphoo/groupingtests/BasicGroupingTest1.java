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
import org.myjaphoo.model.grouping.GroupingDim;
import org.myjaphoo.model.logic.MyjaphooDB;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author mla
 */
public class BasicGroupingTest1 extends TestCase {

    private static TestSet set = DataTestSets.createTestSet1();

    static {
                // init log4j:
        LoggingConfiguration.configurate();
        CacheManager.setCacheActor(new MockCacheActor(set.entries, set.tokens, set.metaTokens));
        CacheManager.setPathMappingCache(new PathMappingCacheMock());
    }

    public void testNoGroupingFiltersMovies() {
        MovieDataBaseFilter mdbf = new MovieDataBaseFilter();

        ChronicEntry chronic = new ChronicEntry();
        for (StructureType type : StructureType.values()) {
            if (type != StructureType.DUPLETTES && type != StructureType.DUPLETTES_IN_DIRS) {

                chronic.getView().setUserDefinedStruct(type.buildUserDefinedEquivalentExpr());
                checkGroupingVallidity(chronic, mdbf, type.toString());
            }
        }
    }

    public void testNoGroupingFiltersMoviesForUserDefinedStruct() {
        MyjaphooDB.singleInstance();
        MovieDataBaseFilter mdbf = new MovieDataBaseFilter();

        

        for (GroupingDim dim : GroupingDim.values()) {
            for (GroupingDim dim2 : GroupingDim.values()) {
                if (dim != GroupingDim.Duplicates && dim != GroupingDim.DuplicatesWithDirs && dim != GroupingDim.Bookmark
                        && dim2 != GroupingDim.Duplicates && dim2 != GroupingDim.DuplicatesWithDirs && dim2 != GroupingDim.Bookmark) {
                    //chronic.setUserDefinedStructureActivated(true);
                    ChronicEntry chronic = new ChronicEntry();
                    chronic.getView().setUserDefinedStructure(Arrays.asList(dim, dim2));
                    String descr = dim.toString() + "," + dim2.toString();
                    checkGroupingVallidity(chronic, mdbf, descr);
                }
            }
        }
    }

    public void testTokenMetaTokenGrouping() {
        MovieDataBaseFilter mdbf = new MovieDataBaseFilter();

        ChronicEntry chronic = new ChronicEntry();

        GroupingDim dim = GroupingDim.Token;
        GroupingDim dim2 = GroupingDim.Metatoken;
        chronic.getView().setUserDefinedStructureActivated(true);
        chronic.getView().setUserDefinedStructure(Arrays.asList(dim, dim2));
        String descr = dim.toString() + "," + dim2.toString();
        checkGroupingVallidity(chronic, mdbf, descr);


    }

    private void checkGroupingVallidity(ChronicEntry chronic, MovieDataBaseFilter mdbf, String descr) {
        List<? extends GroupAlgorithm> grouper = MovieFilterController.createGroupingAlgorithm(chronic);
        MovieStructureNode root = (MovieStructureNode)  mdbf.createStructuredTreeModel(grouper, chronic.getView(), false);
        StructureChecker ch = new StructureChecker(root);
        // bei jeder gruppierung müssen mind. die anzahl der movies rauskommen
        // == es darf kein movie allein durch die gruppierung rausgefiltert werden!
        // der minitest hat 5 movies:
        int leafs = ch.getNumOfLeafs();
        assertTrue("Struktur " + descr + " filtert Movies raus!!, anzahl leafs=" + leafs, 5 <= leafs);
    }
}
