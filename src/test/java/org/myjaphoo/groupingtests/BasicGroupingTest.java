/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.groupingtests;

import junit.framework.TestCase;
import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.LoggingConfiguration;
import org.myjaphoo.MovieDataBaseFilter;
import org.myjaphoo.MovieFilterController;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.movietree.grouping.GroupAlgorithm;
import org.myjaphoo.mocks.MockCacheActor;
import org.myjaphoo.model.StructureType;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.ChronicEntry;

import java.util.List;

/**
 *
 * @author mla
 */
public class BasicGroupingTest extends TestCase {

    private static org.myjaphoo.datasets.TestSet set = DataTestSets.createTestSet1();

    public void testBasicGroupingByDirectory() {
        // init log4j:
        LoggingConfiguration.configurate();
        CacheManager.setCacheActor(new MockCacheActor(set.entries, set.tokens, set.metaTokens));

        //MovieFilterInterface staticFilter = new StaticTestFilter(DataTestSets.createTestSet1());
        MovieDataBaseFilter mdbf = new MovieDataBaseFilter();

        ChronicEntry chronic = new ChronicEntry();
        chronic.getView().setUserDefinedStruct(StructureType.DIRECTORY.buildUserDefinedEquivalentExpr());

        List<? extends GroupAlgorithm> grouper = MovieFilterController.createGroupingAlgorithm(chronic);
        MovieStructureNode root = (MovieStructureNode) mdbf.createStructuredTreeModel(grouper, chronic.getView(), false);

        // es gibt nur ein dir in den testdaten:
        assertEquals("muss ein dir sein!", 1, root.getChildCount());

        StructureChecker ch = new StructureChecker(root);
        ch.assertPathExists(root, "aaa", "bbb", "ccc");
        ch.assertPathExists(root, "aaa", "bbb", "ccc", "name");

        AbstractTreeNode child = ch.getNode(root, "aaa", "bbb", "ccc");
        assertEquals("im dir müssen 1000 hängen!", 1000, child.getChildCount());

        // unterhalb dieses dirs müssen dann alle 1000 movies hängen:
    }

    public void testBasicGroupingByKeyword() {
        CacheManager.setCacheActor(new MockCacheActor(set.entries, set.tokens, set.metaTokens));
        // init log4j:
        LoggingConfiguration.configurate();

        //MovieFilterInterface staticFilter = new StaticTestFilter(DataTestSets.createTestSet1());
        MovieDataBaseFilter mdbf = new MovieDataBaseFilter();

        ChronicEntry chronic = new ChronicEntry();
        chronic.getView().setUserDefinedStruct(StructureType.AUTO_KEYWORD_VERY_STRONG_GROUPER.buildUserDefinedEquivalentExpr());

        List<? extends GroupAlgorithm> grouper = MovieFilterController.createGroupingAlgorithm(chronic);
        MovieStructureNode root = (MovieStructureNode) mdbf.createStructuredTreeModel(grouper, chronic.getView(), false);


        StructureChecker ch = new StructureChecker(root);
        ch.assertPathExists(root, "aaa");
        ch.assertPathExists(root, "bbb");
        ch.assertPathExists(root, "ccc");
        ch.assertPathExists(root, "aaa", "name");

    }
}
