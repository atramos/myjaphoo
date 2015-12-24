/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.dbhandling;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.mlsoft.common.acitivity.Channel;
import org.mlsoft.common.acitivity.ChannelManager;
import org.mlsoft.structures.Trees;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.*;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.dbconfig.DatabaseConfiguration;
import org.myjaphoo.model.logic.MovieEntryJpaController;
import org.myjaphoo.model.logic.MyjaphooDB;
import org.myjaphoo.model.logic.PathMappingJpaController;
import org.myjaphoo.model.logic.ScriptJpaController;
import org.myjaphoo.model.logic.dbhandling.TransactionBoundaryDelegator.CommitBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.*;

/**
 * Experimental class to export the database information to another database.
 * This code copies the complete information from one database  to another.
 * This could be used e.g. to migrate from one database ventor to another
 * or to create backup databases on another system.
 *
 * @author lang
 */
public class DatabaseExporter {

    private static Logger logger = LoggerFactory.getLogger(DatabaseExporter.class);
    private MovieEntryJpaController moviejpa = new MovieEntryJpaController();
    private PathMappingJpaController pmjpa = new PathMappingJpaController();

    public void exportToDatabase(DatabaseConfiguration targedDBConfig) {
        Channel channel = ChannelManager.createChannel(DatabaseExporter.class, "exporting to " + targedDBConfig.getName());
        channel.startActivity();
        channel.setProgressSteps(9);
        DBConnection targetConn = new DBConnection(targedDBConfig);
        targetConn.open();
        EntityManagerFactory targetEMF = targetConn.getEmf();
        //EntityManager em = targetEMF.createEntityManager();
        try {
            ThreadLocalTransactionBoundaryHandler transactionHandler = new ThreadLocalTransactionBoundaryHandler(targetConn);

            Map<Long, Long> sourceKeyToTargetKeyMapping = exportEntries(channel, transactionHandler);
            channel.nextProgress();

            exportPathMapping(channel, transactionHandler);
            channel.nextProgress();
            exportTags(channel, transactionHandler, sourceKeyToTargetKeyMapping);
            channel.nextProgress();
            exportMetaTags(channel, transactionHandler);
            channel.nextProgress();
            transactionHandler.clear();
            exportBookMarks(channel, transactionHandler);
            channel.nextProgress();
            transactionHandler.clear();
            exportChronics(channel, transactionHandler);
            channel.nextProgress();
            transactionHandler.clear();
            exportChangelogs(channel, transactionHandler);
            channel.nextProgress();
            exportScripts(channel, transactionHandler);

            transactionHandler.clear();

            exportThumbs(channel, transactionHandler, sourceKeyToTargetKeyMapping);
            channel.nextProgress();
        } finally {
            targetEMF.close();
            channel.emphasisedMessage("export finished!");
            channel.stopActivity();
        }
    }

    private void exportTags(final Channel channel, TransactionBoundaryDelegator transactionHandler, final Map<Long, Long> sourceKeyToTargetKeyMapping) {


        final List<Token> allTokens = CacheManager.getCacheActor().getImmutableModel().getTokenSet().asList();
        // order by tree structure, so that we can begin with the root tags
        // and get 
        Collections.sort(allTokens, Trees.TREELEVELORDER_COMPARATOR);
        transactionHandler.doInNewTransaction(new CommitBlock() {

            @Override
            public void runCommitBlock(EntityManager em) throws Exception {
                // export all except the internal root token which is already existing
                // in a initialized database:
                for (Token token : allTokens) {
                    if (token.getParent() != null) {
                        channel.message("export tag " + token.getName());
                        Token clone = (Token) token.partialClone();
                        clone.getChildren().clear();
                        clone.setId(null);
                        clone.getAssignedMetaTokens().clear();
                        clone.setMovieEntries(new HashSet<MovieEntry>(token.getMovieEntries().size()));

                        // set the parent one:
                        if (token.getParent() != null) {
                            Token parentToken = getTagByName(em, token.getParent().getName());
                            clone.setParent(parentToken);
                        }
                        // clone properties map:
                        clone.setAttributes(new HashMap<String, String>(clone.getAttributes()));

                        em.persist(clone);

                        // recreate the relation to movies:
                        for (MovieEntry originEntry : token.getMovieEntries()) {
                            Long idInTargetDB = sourceKeyToTargetKeyMapping.get(originEntry.getId());
                            MovieEntry targetEntry = em.find(MovieEntry.class, idInTargetDB);
                            clone.getMovieEntries().add(targetEntry);
                        }
                    }
                }
            }
        });
    }

    public Token getTagByName(EntityManager em, String name) {
        Query query = em.createQuery("from Token where name = :name");
        query.setParameter("name", name);
        Token tok = (Token) query.getSingleResult();
        return tok;
    }

    public MetaToken getMetaTagByName(EntityManager em, String name) {
        Query query = em.createQuery("from MetaToken where name = :name");
        query.setParameter("name", name);
        MetaToken metaTag = (MetaToken) query.getSingleResult();
        return metaTag;
    }

    private void exportMetaTags(final Channel channel, TransactionBoundaryDelegator transactionHandler) {
        final List<MetaToken> allTokens = CacheManager.getCacheActor().getImmutableModel().getMetaTokenSet().asList();
        // order by tree structure, so that we can begin with the root tags
        // and get 
        Collections.sort(allTokens, Trees.TREELEVELORDER_COMPARATOR);
        transactionHandler.doInNewTransaction(new CommitBlock() {

            @Override
            public void runCommitBlock(EntityManager em) throws Exception {
                for (MetaToken metatag : allTokens) {
                    channel.message("export metatag " + metatag.getName());
                    MetaToken clone = (MetaToken) metatag.partialClone();
                    clone.getChildren().clear();
                    clone.setId(null);
                    clone.setAssignedTokens(new HashSet<Token>(metatag.getAssignedTokens().size()));

                    // set the parent one:
                    if (metatag.getParent() != null) {

                        MetaToken parentMetaTag = getMetaTagByName(em, metatag.getParent().getName());
                        clone.setParent(parentMetaTag);
                    }
                    // clone properties map:
                    clone.setAttributes(new HashMap<String, String>(clone.getAttributes()));

                    // assign relations to tags:
                    for (Token token : metatag.getAssignedTokens()) {
                        Token tokenInDB = getTagByName(em, token.getName());
                        clone.getAssignedTokens().add(tokenInDB);
                    }

                    em.persist(clone);
                }
            }
        });
    }

    private void exportBookMarks(final Channel channel, TransactionBoundaryDelegator transactionHandler) {
        final List<BookMark> bms = moviejpa.findBookMarkEntities();
        transactionHandler.doInNewTransaction(new CommitBlock() {

            @Override
            public void runCommitBlock(EntityManager em) throws Exception {
                for (BookMark bm : bms) {
                    channel.message("export bookmark " + bm.getName());
                    BookMark copy = new BookMark();
                    copy.setMenuPath(bm.getMenuPath());
                    copy.setName(bm.getName());
                    copy.setDescr(bm.getDescr());
                    copy.setView(bm.getView());
                    em.persist(copy);
                }
            }
        });
    }

    private void exportChronics(Channel channel, TransactionBoundaryDelegator transactionHandler) {
        final List<ChronicEntry> chronics = moviejpa.findChronicEntryEntities();
        channel.message("export all chronics ");
        transactionHandler.doInNewTransaction(new CommitBlock() {

            @Override
            public void runCommitBlock(EntityManager em) throws Exception {
                for (ChronicEntry chronic : chronics) {
                    ChronicEntry copy = new ChronicEntry();
                    copy.setView(chronic.getView());
                    em.persist(copy);
                }
            }
        });
    }

    private void exportChangelogs(Channel channel, TransactionBoundaryDelegator transactionHandler) {
        final List<ChangeLog> chronics = moviejpa.findChangeLogEntities();
        channel.message("export all changelogs ");
        transactionHandler.doInNewTransaction(new CommitBlock() {

            @Override
            public void runCommitBlock(EntityManager em) throws Exception {
                for (ChangeLog changelog : chronics) {
                    ChangeLog copy = new ChangeLog();

                    copy.setCltype(changelog.getCltype());
                    copy.setCreated(changelog.getCreated());
                    copy.setMsg(changelog.getMsg());
                    copy.setObjDescription(changelog.getObjDescription());
                    em.persist(copy);
                }
            }
        });
    }

    private void exportScripts(Channel channel, TransactionBoundaryDelegator transactionHandler) {
        final List<SavedGroovyScript> scripts = new ScriptJpaController().findScriptEntities();
        channel.message("export all scripts ");
        transactionHandler.doInNewTransaction(new CommitBlock() {

            @Override
            public void runCommitBlock(EntityManager em) throws Exception {
                for (SavedGroovyScript script : scripts) {
                    SavedGroovyScript copy = new SavedGroovyScript();
                    copy.setDescr(script.getDescr());
                    copy.setMenuPath(script.getMenuPath());
                    copy.setName(script.getName());
                    copy.setScriptText(script.getScriptText());
                    copy.setScriptType(script.getScriptType());
                    em.persist(copy);
                }
            }
        });
    }


    private Map<Long, Long> exportEntries(Channel channel, TransactionBoundaryDelegator transactionHandler) {

        channel.message("export all media entries ");
        final Collection<MovieEntry> entries = CacheManager.getCacheActor().getImmutableModel().getMovies();
        final Map<Long, Long> sourceKeyToTargetKeyMapping = new HashMap<Long, Long>(entries.size() * 2);
        transactionHandler.doInNewTransaction(new BatchCommitBlock() {

            @Override
            public void runCommitBlock(EntityManager em) throws Exception {
                for (MovieEntry entry : entries) {
                    MovieEntry clone = (MovieEntry) entry.partialClone();
                    clone.setId(null);
                    clone.setThumbnails(new ArrayList<Thumbnail>());

                    // clone properties map:
                    clone.setAttributes(new HashMap<String, String>(clone.getAttributes()));

                    persist(em, clone);
                    sourceKeyToTargetKeyMapping.put(entry.getId(), clone.getId());
                }
            }
        });
        return sourceKeyToTargetKeyMapping;
    }

    /**
     * exports the thumbnails. this is the most critical one.
     * The thumbnail table contains the most of the data of a database.
     * Therefore for very big databases, the attemt is to
     * - use scrollable results from hibernate to read the thumbnail data
     * - not to map the read result in Thumbnail-Objects (which would lead to
     * load the relation to movie entry by hinbernate), but instead to use
     * a jpa query to select all relevant columns directly.
     * - commit often on the target db during insert, to minimize temporary storage for
     * rollback
     *
     * @param channel
     * @param transactionHandler
     * @param sourceKeyToTargetKeyMapping
     */
    private void exportThumbs(Channel channel, TransactionBoundaryDelegator transactionHandler, final Map<Long, Long> sourceKeyToTargetKeyMapping) {
        EntityManager sourceEm = MyjaphooDB.singleInstance().createEntityManager();
        channel.message("export all thumb nails ");
        final Session SourceSession = (Session) sourceEm.getDelegate();
        try {
            // load thumbnails as scrollable result and NOT as entity object 
            // to minimize the dramatic overhead when accessing the movie entry relation

            final ScrollableResults cursor = SourceSession.createQuery("select t.movieEntry.id, t.thumbnail from Thumbnail t order by t.movieEntry.id").scroll();


            transactionHandler.doInNewTransaction(new BatchCommitBlock() {

                int count = 0;

                @Override
                public void runCommitBlock(EntityManager em) throws Exception {
                    while (cursor.next()) {
                        Long movie_id = cursor.getLong(0);
                        byte[] byteData = cursor.getBinary(1);

                        Long targetId = sourceKeyToTargetKeyMapping.get(movie_id);
                        MovieEntry entryInTargetDB = em.find(MovieEntry.class, targetId);
                        Thumbnail tn = new Thumbnail();
                        tn.setMovieEntry(entryInTargetDB);
                        tn.setThumbnail(byteData);
                        persist(em, tn);
                        count++;

                        if (count % 100 == 0) {
                            SourceSession.flush();
                            SourceSession.clear();
                        }

                    }
                }
            });
        } finally {
            sourceEm.close();
        }

    }


    abstract static class BatchCommitBlock implements CommitBlock {

        private int batchCounter = 0;
        private static int batchMax = 100;

        public void persist(EntityManager em, Object o) {
            batchCounter++;
            em.persist(o);
            if (batchCounter % batchMax == 0) {
                em.flush();
                em.clear();
                em.getTransaction().commit();
                em.getTransaction().begin();
            }
        }
    }

    private void exportPathMapping(Channel channel, TransactionBoundaryDelegator transactionHandler) {
        final List<PathMapping> pathmappings = pmjpa.findPathMappingEntities();
        channel.message("export all path mappings ");
        transactionHandler.doInNewTransaction(new CommitBlock() {

            @Override
            public void runCommitBlock(EntityManager em) throws Exception {
                for (PathMapping pm : pathmappings) {
                    PathMapping copy = new PathMapping();
                    copy.setPathPrefix(pm.getPathPrefix());
                    copy.setSubstitution(pm.getSubstitution());
                    em.persist(copy);
                }
            }
        });
    }
}
