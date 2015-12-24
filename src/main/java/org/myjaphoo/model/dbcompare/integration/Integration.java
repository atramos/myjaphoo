package org.myjaphoo.model.dbcompare.integration;

import org.mlsoft.structures.Trees;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Thumbnail;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.dbcompare.AbstractEntryKey;
import org.myjaphoo.model.dbcompare.ComparisonContext;
import org.myjaphoo.model.dbcompare.DBDiffCombinationResult;
import org.myjaphoo.model.dbcompare.DatabaseComparison;
import org.myjaphoo.model.logic.ThumbnailJpaController;
import org.myjaphoo.model.logic.dbhandling.TransactionBoundaryDelegator;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Integrates differences from the compare db into this db based on DBDiffCombinationResults.
 * User: lang
 * Date: 28.11.12
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class Integration {

    ComparisonContext context = new ComparisonContext();

    TransactionBoundaryDelegator transaction;
    ThumbnailJpaController thumbnailJpaController = new ThumbnailJpaController();


    /** map with all created entries by this integration. We need to take care, that we do not create
     * entries multiple times, when mulitple diff nodes for e.g. different tags exists. */
    HashMap<AbstractEntryKey, MovieEntry> newlyCreatedEntries = new HashMap<AbstractEntryKey, MovieEntry>();
    /**
     * integreate all differences described in the db diff into the current database.
     *
     * @param dbdiff
     */
    public void integrate(DBDiffCombinationResult dbdiff) throws Exception {
        transaction = DatabaseComparison.getInstance().createTransaction();
        MovieEntry entry = integrateEntry(dbdiff);
        Token tag = integrateTag(dbdiff, entry);
        integrateMetaTag(dbdiff, tag);
    }

    private Token integrateTag(DBDiffCombinationResult dbdiff, MovieEntry entry) {
        switch (dbdiff.getDiffTag()) {
            case EQUAL: // nothing to do
                return dbdiff.getToken();
            case CHANGED:
                context.copyAttr(dbdiff.getCDBToken(), dbdiff.getToken());
                CacheManager.getCacheActor().editToken(dbdiff.getToken());
                return dbdiff.getToken();
            case NEW:
                Token tag = prepare(dbdiff.getCDBToken());
                // now assign the tag to the movie:
                CacheManager.getCacheActor().assignToken2MovieEntry(tag, Arrays.asList(entry));
                return tag;
            case REMOVED:
                // todo remove could probably mean two different things:
                // 1. remove the association between entry and tag
                // 2. the tag got completely removed (this also includes 1.)

                // 1. in any case remove the association (if the tag still exists in our db):
                if (CacheManager.getCacheActor().getImmutableModel().getTokenSet().find(dbdiff.getToken()) != null) {
                    CacheManager.getCacheActor().unassignTokenFromMovies(dbdiff.getToken(), Arrays.asList(entry));

                    // 2. if the comparison db doesnt have the tag at all, we delete it completely:
                    if (Trees.findByName(DatabaseComparison.getInstance().getCacheActor().getImmutableModel().getTokenSet().asList(), dbdiff.getToken().getName()) == null) {
                        CacheManager.getCacheActor().removeToken(dbdiff.getToken());
                    }
                }

                return null;
        }
        throw new RuntimeException("illegal state!");
    }


    private void integrateMetaTag(DBDiffCombinationResult dbdiff, Token tag) {
        switch (dbdiff.getDiffMetaTag()) {
            case EQUAL: // nohting to do
                break;
            case CHANGED:
                context.copyAttr(dbdiff.getCDBMetaToken(), dbdiff.getMetaToken());
                CacheManager.getCacheActor().editMetaToken(dbdiff.getMetaToken());
                break;
            case NEW:
                MetaToken mt = prepare(dbdiff.getCDBMetaToken());
                CacheManager.getCacheActor().assignMetaTokenToToken(mt, tag);
                break;
            case REMOVED:
                // 1. remove the association between entry and tag
                // 2. the tag got completely removed (this also includes 1.)

                // 1. in any case remove the association (if the tag still exists in our db):
                if (CacheManager.getCacheActor().getImmutableModel().getMetaTokenSet().find(dbdiff.getMetaToken()) != null) {
                    CacheManager.getCacheActor().unAssignMetaTokenFromToken(dbdiff.getMetaToken(), Arrays.asList(tag));

                    // 2. if the comparison db doesnt have the tag at all, we delete it completely:
                    if (Trees.findByName(DatabaseComparison.getInstance().getCacheActor().getImmutableModel().getMetaTokenSet().asList(), dbdiff.getMetaToken().getName()) == null) {
                        CacheManager.getCacheActor().removeMetaToken(dbdiff.getMetaToken());
                    }
                }
        }
    }

    private MovieEntry integrateEntry(DBDiffCombinationResult dbdiff) throws Exception {
        switch (dbdiff.getDiffEntry()) {
            case EQUAL: // nothing to do;
                return dbdiff.getEntry();
            case CHANGED:
                context.copyAttr(dbdiff.getCDBEntry(), dbdiff.getEntry());
                CacheManager.getCacheActor().editMovie(dbdiff.getEntry());
                return dbdiff.getEntry();
            case NEW:
                // the other db has the new entry:
                AbstractEntryKey key = dbdiff.getContext().createEntryKey(dbdiff.getCDBEntry());
                MovieEntry alreadyCreatedEntry = newlyCreatedEntries.get(key);
                if (alreadyCreatedEntry != null) {
                    // it has been created by another diff node before;
                    // which means, this is a diffnode that describes e.g. another tag difference for this entry;
                    // so we just return the previously created entry:
                    return alreadyCreatedEntry;
                }

                MovieEntry copy = new MovieEntry();
                context.copyAttr(dbdiff.getCDBEntry(), copy);
                CacheManager.getCacheActor().newMovie(copy);
                copyThumbnails(dbdiff.getCDBEntry(), copy);
                newlyCreatedEntries.put(key, copy);
                return copy;
            case REMOVED:
                // delete the movie in our database:
                CacheManager.getCacheActor().removeMovieEntry(dbdiff.getEntry());
                return null;
        }
        throw new RuntimeException("illegal state!");
    }

    private void copyThumbnails(final MovieEntry source, MovieEntry dest) {
        // load thumbs from the comparison db:
        final List<Thumbnail> sourceThumbs = transaction.doLoading(new TransactionBoundaryDelegator.LoaderBlock<List<Thumbnail>>() {
            @Override
            public List<Thumbnail> runLoadBlock(EntityManager em) {
                MovieEntry soureEntity = em.find(MovieEntry.class, source.getId());
                List<Thumbnail> result = soureEntity.getThumbnails();
                result.size();
                return result;
            }
        });
        // save the thumbs in our db:
        MovieEntry attachedDest = dest; //MyjaphooDB.singleInstance().ensureObjIsAttached(dest);
        for (Thumbnail sourceTn : sourceThumbs) {
            Thumbnail destTn = new Thumbnail();
            destTn.setH(sourceTn.getH());
            destTn.setW(sourceTn.getW());

            destTn.setMovieEntry(attachedDest);
            destTn.setType(sourceTn.getType());
            destTn.setThumbnail(sourceTn.getThumbnail());
            thumbnailJpaController.create(destTn);
            attachedDest.getThumbnails().add(destTn);
        }
    }


    private Token prepare(Token tagInOtherDb) {
        if (tagInOtherDb == null) {
            return null;
        }
        // could be either a fully new tag, or could also mean, that
        // the tag is existing, but the assignment to this movie is new:
        Token tag = Trees.findByName(CacheManager.getCacheActor().getImmutableModel().getTokenSet().asList(), tagInOtherDb.getName());
        if (tag != null) {
            context.copyAttr(tagInOtherDb, tag);
            CacheManager.getCacheActor().editToken(tag);
        } else {
            tag = new Token();
            context.copyAttr(tagInOtherDb, tag);
            Token parent = prepare(tagInOtherDb.getParent());
            CacheManager.getCacheActor().createToken(tag, parent);

        }
        return tag;
    }

    private MetaToken prepare(MetaToken tagInOtherDb) {
        if (tagInOtherDb == null) {
            return null;
        }
        // could be either a fully new tag, or could also mean, that
        // the tag is existing, but the assignment to this movie is new:
        MetaToken tag = Trees.findByName(CacheManager.getCacheActor().getImmutableModel().getMetaTokenSet().asList(), tagInOtherDb.getName());
        if (tag != null) {
            context.copyAttr(tagInOtherDb, tag);
            CacheManager.getCacheActor().editMetaToken(tag);
        } else {
            tag = new MetaToken();
            context.copyAttr(tagInOtherDb, tag);
            MetaToken parent = prepare(tagInOtherDb.getParent());
            CacheManager.getCacheActor().createMetaToken(tag, parent);
        }
        return tag;
    }

}
