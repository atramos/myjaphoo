/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.myjaphoo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.mlsoft.common.acitivity.Channel;
import org.mlsoft.common.acitivity.ChannelManager;
import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.movietree.DiffNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.movietree.grouping.GroupAlgorithm;
import org.myjaphoo.gui.movietree.grouping.GroupingExecutionContextImpl;
import org.myjaphoo.model.cache.EntityCacheActor;
import org.myjaphoo.model.db.ChronicEntry;
import org.myjaphoo.model.db.DataView;
import org.myjaphoo.model.dbcompare.DBDiffCombinationResult;
import org.myjaphoo.model.dbcompare.DatabaseComparison;
import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.*;


/**
 * @author mla TODO rename to something like treemodelgenerator
 */
public class MovieDataBaseFilter {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/resources/MovieDataBaseFilter");
    public static final Logger LOGGER = LoggerFactory.getLogger(MovieDataBaseFilter.class.getName());
    private long numOfMovies = 0;
    private AbstractMovieTreeNode currentSelectedDir = null;
    private MovieFilterInterface filter;

    /**
     * abhängig von den gewählten gruppierungen (und den daten) können movies
     * mehrfach unter verschiedenen gruppierungen erscheinen. Dann ist dieses
     * flag auf false gesetzt. (Wäre z.b. der Fall, wenn zwei verschiedene Tokens
     * einem Movie zugeordnet werden, und Gruppierung nach Token gewählt ist).
     */
    private boolean treeShowsMoviesUnique;

    private List<String> usedLiterals;


    /**
     * Default constructor.
     */
    public MovieDataBaseFilter() {
        filter = new MovieInMemoryFilter();
    }

    /**
     * Constructor mit movie filter. Nützlich für Testfälle.
     */
    public MovieDataBaseFilter(MovieFilterInterface filter) {
        this.filter = filter;
    }


    abstract class NodeCreator {

        List<MovieNode> createdNodes = new ArrayList<>(50000);

        abstract MovieNode createNode(JoinedDataRow cr, boolean unique);

        /**
         * Adds a new node to each parent to build up the tree structure.
         * @param parents all parents to add a node
         * @param cr      the combination result to build leaf nodes from.
         * @return
         */
        public MovieNode addNodes(List<MovieStructureNode> parents, JoinedDataRow cr) {
            MovieNode node = null;
            if (parents != null) {
                boolean unique = parents.size() <= 1;
                if (parents.size() > 1) {
                    // grouping is not unique
                    treeShowsMoviesUnique = false;
                }

                for (MovieStructureNode parent : parents) {
                    node = createNode(cr, unique);
                    parent.addChild(node);
                    createdNodes.add(node);
                }
            }
            return node;
        }

        public void resetJoinedRows() {
            for (MovieNode node : createdNodes) {
                node.setRow(null);
            }
        }
    }

    class MovieNodeCreator extends NodeCreator {
        @Override
        public MovieNode createNode(JoinedDataRow cr, boolean unique) {
            return new MovieNode(cr.getEntry(), unique, cr);
        }
    }

    ;

    class DiffNodeCreator extends NodeCreator {
        @Override
        public MovieNode createNode(JoinedDataRow cr, boolean unique) {
            DBDiffCombinationResult dbdiff = (DBDiffCombinationResult) cr;
            return new DiffNode(dbdiff, unique);
        }
    }

    ;

    public MovieStructureNode createStructuredTreeModel(List<? extends GroupAlgorithm> grouper, DataView dataView, boolean condenseDuplicates) {
        Channel channel = ChannelManager.createChannel(MovieDataBaseFilter.class, "Creating tree model");//NOI18N
        channel.startActivity();
        List<JoinedDataRow> allEntries;
        try {
            allEntries = filter.loadFilteredEntries(dataView, grouper);
            usedLiterals = filter.getUsedLiterals();

            StopWatch watch = new StopWatch();
            watch.start();
            // map, die wir für den condenseDuplicates modus brauchen
            Map<Long, MovieNode> mapChksumMovieNodes = new HashMap<Long, MovieNode>((int) numOfMovies * 2);
            treeShowsMoviesUnique = true;


            channel.setProgressSteps(allEntries.size() * grouper.size());
            GroupingExecutionContextImpl groupingExecutionContext = new GroupingExecutionContextImpl(allEntries, new ExecutionContext());
            numOfMovies = groupingExecutionContext.getAllEntriesToGroup().size();

            final EntityCacheActor comparisonDBCacheActor = DatabaseComparison.getInstance().getCacheActor();
            NodeCreator nodeCreator = comparisonDBCacheActor == null ? new MovieNodeCreator() : new DiffNodeCreator();

            List<MovieStructureNode> rootNodes = new ArrayList<>();
            for (GroupAlgorithm groupAlgorithm : grouper) {
                rootNodes.add(group(channel, nodeCreator, mapChksumMovieNodes, allEntries, groupAlgorithm, dataView, condenseDuplicates));
            }

            // reset all joined rows in the movie nodes to free their space:
            nodeCreator.resetJoinedRows();

            watch.stop();
            LOGGER.info("Grouping of movie tree finished, took " + watch.toString()); //NOI18N
            channel.stopActivity();
            mapChksumMovieNodes.clear();
            // build model:
            if (rootNodes.size() == 1) {
                return rootNodes.get(0);
            } else {
                MovieStructureNode root = new MovieStructureNode("root");
                setRootNodeName(dataView, root);
                for (int i = 0; i < rootNodes.size(); i++) {
                    MovieStructureNode node = rootNodes.get(i);
                    GroupAlgorithm groupAlg = grouper.get(i);
                    node.setName(groupAlg.getText());
                    root.addChild(node);
                }
                return root;
            }
        } catch (ParserException ex) {
            LOGGER.error("error", ex); //NOI18N
            channel.errormessage(ex.getMessage());
            channel.stopActivity();
            throw ex;
        }

    }

    private MovieStructureNode group(Channel channel, NodeCreator nodeCreator, Map<Long, MovieNode> mapChksumMovieNodes, List<JoinedDataRow> allEntries, GroupAlgorithm grouper, DataView dataView, boolean condenseDuplicates) {
        MovieStructureNode root = prepareRoot(grouper, dataView);
        GroupingExecutionContextImpl groupingExecutionContext = new GroupingExecutionContextImpl(allEntries, new ExecutionContext());
        grouper.preProcess(groupingExecutionContext);

        final EntityCacheActor comparisonDBCacheActor = DatabaseComparison.getInstance().getCacheActor();

        for (JoinedDataRow cr : allEntries) {
            channel.nextProgress();
            if (condenseDuplicates) {
                // suchen, obs schon einen identischen movie gibt:
                MovieNode alreadyExistingNode = mapChksumMovieNodes.get(cr.getEntry().getChecksumCRC32());
                if (alreadyExistingNode != null && !alreadyExistingNode.getMovieEntry().equals(cr.getEntry())) {
                    alreadyExistingNode.addCondensedDuplicate(cr.getEntry());
                } else {
                    MovieNode oneNode = nodeCreator.addNodes(grouper.findParents(root, cr), cr);
                    mapChksumMovieNodes.put(cr.getEntry().getChecksumCRC32(), oneNode);

                }
            } else {
                nodeCreator.addNodes(grouper.findParents(root, cr), cr);
            }
        }

        if (dataView.isPruneTree()) {
            grouper.pruneEmptyDirs(root);
        }
        grouper.postProcess(root);

        grouper.pruneByHavingClause(root);

        grouper.aggregate(root);

        retainSelectedDir(grouper, root, dataView);
        return root;
    }

    public String getFilterInfoText(ChronicEntry currChronic) {
        if (!currChronic.getView().isFilter()) {
            return MessageFormat.format(localeBundle.getString("UNFILTERED"), numOfMovies);
        } else {
            return MessageFormat.format(localeBundle.getString("FILTERED"), numOfMovies);
        }
    }

    /**
     * @return the currentSelectedDir
     */
    public AbstractMovieTreeNode getCurrentSelectedDir() {
        return currentSelectedDir;
    }

    public void setCurrentDir(AbstractMovieTreeNode movieDirNode) {
        currentSelectedDir = movieDirNode;
    }

    /**
     * @return the treeShowsMoviesUnique
     */
    public boolean isTreeShowsMoviesUnique() {
        return treeShowsMoviesUnique;
    }

    private MovieStructureNode prepareRoot(GroupAlgorithm grouper, DataView dataView) {
        MovieStructureNode root = (MovieStructureNode) grouper.getOrCreateRoot();
        setRootNodeName(dataView, root);
        return root;
    }

    private void setRootNodeName(DataView dataView, MovieStructureNode root) {
        if (dataView.isFilter()) {
            root.setName(MessageFormat.format(localeBundle.getString("FILTERED:"), numOfMovies));
        } else {
            root.setName(MessageFormat.format(localeBundle.getString("ALL"), numOfMovies));
        }
    }

    private void retainSelectedDir(GroupAlgorithm grouper, MovieStructureNode root, DataView dataView) {
        // reset the current dir, if possible:
        if (!StringUtils.isEmpty(dataView.getCurrentSelectedDir())) {
            LOGGER.debug("retain selected dir " + dataView.getCurrentSelectedDir()); //NOI18N
            currentSelectedDir = grouper.findAccordingNode(root, dataView.getCurrentSelectedDir());
        }
        if (currentSelectedDir == null) {
            // setze es einfach auf das erste kind der wurzel:
            if (root.getChildCount() > 0) {
                currentSelectedDir = (AbstractMovieTreeNode) root.getChildAt(0);
                LOGGER.debug("no better way: select first child " + currentSelectedDir.getPathName()); //NOI18N
            } else {
                currentSelectedDir = null;
            }
        }
    }

    public List<String> getUsedLiterals() {
        return usedLiterals;
    }
}
