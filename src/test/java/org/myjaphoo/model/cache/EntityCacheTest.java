/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache;

import org.myjaphoo.model.WmEntitySet;
import java.util.Arrays;
import junit.framework.TestCase;
import org.myjaphoo.datasets.TestSet;
import org.myjaphoo.model.cache.impl.EntityCache;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Rating;
import org.myjaphoo.model.db.Token;

/**
 * Simuliert aufrufe gegen den cache-system, wie es von der gui aus
 * stattfinden würde.
 * @author mla
 */
public class EntityCacheTest extends TestCase {

    public void testAssigningTokens() {
        TestSet set = new TestSet();
        MovieEntry e1 = set.createEntry("movie1", "aaa/bbb/ccc", 100, 100, Rating.BAD);
        MovieEntry e2 = set.createEntry("movie2", "ddd/eee/fff", 101, 101, Rating.MIDDLE);
        MovieEntry e3 = set.createEntry("movie3", "gggg/hhhh", 102, 103, Rating.MIDDLE);
        MovieEntry e4 = set.createEntry("movie4", "jjjj", 104, 104, Rating.VERY_BAD);
        MovieEntry e5 = set.createEntry("movie5", "ddd/eee/fff", 105, 105, Rating.VERY_GOOD);

        Token t1 = set.createToken("Token1");
        Token t2 = set.createToken("Token2");
        Token t3 = set.createToken("Token3");

        set.ass(e1, t1);
        set.ass(e2, t1);
        set.ass(e3, t2);
        set.ass(e3, t1);
        set.ass(e4, t2);

        MetaToken mt1 = set.createMetaToken("meta1");
        MetaToken mt2 = set.createMetaToken("meta2");
        MetaToken mt3 = set.createMetaToken("meta3");
        set.ass(mt1, t1);
        set.ass(mt2, t1);
        set.ass(mt3, t2);

        WmEntitySet wmset = new WmEntitySet(set.entries, set.tokens, set.metaTokens);
        EntityCache cache = new EntityCache(new MockCache(wmset));


        // gui workflow testen:

        // immutable modell holen (ist eine kopie des internen modells):
        WmEntitySet clone = cache.getImmutableModel();
        // check identiy (except of referencial identity) of the copy:
        MovieEntry clonedE5 = clone.getMovieEntrySet().find(e5);

        assertEquals(clonedE5.getId(), e5.getId());
        assertTrue(clonedE5 != e5);

        Token clonedT1 = clone.getTokenSet().find(t1);
        assertEquals(clonedT1.getId(), t1.getId());
        assertTrue(clonedT1 != t1);

        // änderung durchführen: t1 zu e5 zuordnen:
        cache.assignToken2MovieEntry(clonedT1, Arrays.asList(clonedE5));

        // neues immutable modell muss nun diese änderung beinhalten:
        MovieEntry changedE5 = cache.getImmutableModel().getMovieEntrySet().find(clonedE5);
        Token expectToBeT1 = changedE5.getTokens().iterator().next();

        assertEquals(expectToBeT1.getId(), clonedT1.getId());

    }
}
