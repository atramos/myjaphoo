/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.OrderType;
import org.myjaphoo.gui.editor.rsta.RSTAHelper;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.movietree.grouping.GroupAlgorithm;
import org.myjaphoo.gui.movietree.grouping.PartialGrouper;
import org.myjaphoo.gui.thumbtable.DistinctFilter;
import org.myjaphoo.gui.thumbtable.groupedthumbs.GroupedThumbView;
import org.myjaphoo.gui.thumbtable.groupedthumbs.ThumbStripe;
import org.myjaphoo.model.StructureType;
import org.myjaphoo.model.ThumbMode;
import org.myjaphoo.model.db.*;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.groupbyparser.GroupByParser;
import org.myjaphoo.model.groupbyparser.expr.GroupingExpression;
import org.myjaphoo.model.grouping.GroupingDim;
import org.myjaphoo.model.logic.MovieEntryJpaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Movie filter controller. Holds filter and filter methods
 *
 * @author mla
 */
public class MovieFilterController {

    public static final Logger LOGGER = LoggerFactory.getLogger(MovieFilterController.class.getName());
    private ChronicEntry currChronic = new ChronicEntry();
    /**
     * the currently displayed/filtered movie nodes.
     */
    private List<AbstractLeafNode> currentDisplayedMovieNodes;
    private LinkedList<ChronicEntry> chronicList = new LinkedList<>();

    private int chronicIndex=0;

    /** sollen duplikate zusammengefasst zu einem movieentry werden? */
    /**
     * duplicates should be combined into one movie entry?
     */
    private boolean condenseDuplicates = false;
    private MovieDataBaseFilter movieFilter = new MovieDataBaseFilter();
    private OrderType orderType = OrderType.BY_DIR_AND_NAME;
    MovieEntryJpaController movieEntryjpa = new MovieEntryJpaController();

    /**
     * the current displayed movie tree model.
     */
    private MovieStructureNode root;

    private List<String> usedLiterals = new ArrayList<>();

    public MovieFilterController() {
    }

    public ThumbDisplayFilterResult getThumbsModel(ThumbDisplayFilterResult.ThumbDisplayFilterResultMode mode, boolean preventGroupingDups) {
        if (mode == ThumbDisplayFilterResult.ThumbDisplayFilterResultMode.PlAINLIST) {
            return new ThumbDisplayFilterResult(getCurrentThumbs(preventGroupingDups));
        } else {
            return new ThumbDisplayFilterResult(getCurrentThumbsBreakDownByChildren(preventGroupingDups));
        }
    }

    private List<AbstractLeafNode> getCurrentThumbs(boolean preventGroupingDups) {
        if (movieFilter.getCurrentSelectedDir() == null) {
            return new ArrayList<AbstractLeafNode>();
        } else {
            this.currentDisplayedMovieNodes = createNodeList(movieFilter.getCurrentSelectedDir());
            if (preventGroupingDups) {
                DistinctFilter filter = new DistinctFilter();
                this.currentDisplayedMovieNodes = filter.filter(currentDisplayedMovieNodes);
            }
            return currentDisplayedMovieNodes;
        }
    }

    private List<AbstractLeafNode> createNodeList(AbstractMovieTreeNode fromNodeOn) {
        ArrayList<AbstractLeafNode> movieNodes = new ArrayList<AbstractLeafNode>(50000);
        addMovieNodes(movieNodes, fromNodeOn);
        Collections.sort(movieNodes, orderType.getComparator());
        return movieNodes;
    }

    private GroupedThumbView getCurrentThumbsBreakDownByChildren(boolean preventGroupingDups) {
        final AbstractMovieTreeNode currentDir = movieFilter.getCurrentSelectedDir();
        GroupedThumbView group = new GroupedThumbView(new ArrayList<ThumbStripe>());
        if (currentDir == null) {
            return group;
        } else {
            // hier werden alle leafs gesammelt, die direkt unter dem ausgewählten knoten liegen:
            // all leafs are collected here, which lie directly beneath the selected node:
            ArrayList<AbstractLeafNode> leafs = new ArrayList<AbstractLeafNode>(50000);

            for (AbstractTreeNode child : currentDir.getChildren()) {
                if (child instanceof MovieStructureNode) {
                    MovieStructureNode structureNode = (MovieStructureNode) child;
                    List<AbstractLeafNode> movieNodes = createNodeList(structureNode);

                    ThumbStripe stripe = new ThumbStripe(structureNode, movieNodes);
                    group.getStripes().add(stripe);
                } else if (child instanceof AbstractLeafNode) {
                    leafs.add((AbstractLeafNode) child);
                }
            }
            if (leafs.size() > 0) {
                // pseude struktur node für streifen erstellen, der keiner weiteren untergruppe angehört:
                // structure's pseudo node for creating stripes, which belongs to no other subgroup:
                MovieStructureNode directLeafs = new MovieStructureNode("Leaf"); // Blätter //NOI18N
                ThumbStripe stripe = new ThumbStripe(directLeafs, leafs);
                group.getStripes().add(stripe);
            }

            if (preventGroupingDups) {
                DistinctFilter filter = new DistinctFilter();
                filter.filter(group);
            }

            return group;
        }
    }

    public Token getCurrentToken() {
        return currChronic.getView().getCurrentToken();
    }

    private void addMovieNodes(ArrayList<AbstractLeafNode> movieNodes, AbstractTreeNode movieDirNode) {
        // falls ein movie selektiert ist (u. kein dir) dann nur diesen movie returnieren:
        // if a movie is selected (and not you) then just returnieren this movie:
        if (movieDirNode instanceof AbstractLeafNode) {
            movieNodes.add((AbstractLeafNode) movieDirNode);
            return;
        }
        final List<AbstractTreeNode> children = movieDirNode.getChildren();
        // children could be null due to desctruction of nodes:
        if (children != null) {
            for (AbstractTreeNode node : children) {
                if (node instanceof AbstractLeafNode) {
                    movieNodes.add((AbstractLeafNode) node);
                } else {
                    // its a directory:
                    if (isListChildMovies()) {
                        addMovieNodes(movieNodes, (AbstractTreeNode) node);
                    }
                }
            }
        }
    }

    public MovieStructureNode createMovieTreeModel() {
        pushChronik();
        List<? extends GroupAlgorithm> algorithms = createGroupingAlgorithm(currChronic);
        boolean condensateDups = isCondenseDuplicates();

        root = movieFilter.createStructuredTreeModel(algorithms, currChronic.getView(),
                condensateDups);
        usedLiterals = movieFilter.getUsedLiterals();
        return root;
    }

    public MovieStructureNode getLastCreatedMovieTreeModel() {
        return root;
    }

    public MovieStructureNode createDirectoryStructuredTreeModel() {
        GroupAlgorithm algorithm = new PartialGrouper(null, null, StructureType.DIRECTORY.createPartialPathBuilder());
        boolean condensateDups = isCondenseDuplicates();

        return movieFilter.createStructuredTreeModel(Arrays.asList(algorithm), new DataView(),
                condensateDups);
    }

    public static List<? extends GroupAlgorithm> createGroupingAlgorithm(ChronicEntry entry) {
        String userDefinedStructure = entry.getView().getUserDefinedStruct();
        try {
            GroupByParser parser = (GroupByParser) RSTAHelper.groupByParserCreator.createParser();
            List<GroupingExpression> expressions = parser.parseGroupByExpression(userDefinedStructure);
            ArrayList<GroupAlgorithm> result = new ArrayList<>();
            for (GroupingExpression expression: expressions) {
                PartialGrouper algorithm = new PartialGrouper(expression.createGrouper(), expression.getHavingClause(), expression.getAggregations());
                algorithm.setText(expression.getText());
                result.add(algorithm);
            }
            return result;
        } catch (ParserException ex) {
            LOGGER.error("unable to parse and load user defined structure  " + userDefinedStructure, ex); //NOI18N
            return Arrays.asList(new PartialGrouper(null, null, StructureType.DIRECTORY.createPartialPathBuilder()));
        }
    }

    public void setCurrentToken(Token tokenNode) {
        currChronic.getView().setCurrentToken(tokenNode);
    }

    /**
     * @return the filterPattern
     */
    public String getFilterPattern() {
        return currChronic.getView().getFilterExpression();
    }

    /**
     * @param filterPattern the filterPattern to set
     */
    public void setFilterPattern(String filterPattern) {
        currChronic.getView().setFilterExpression(filterPattern);
    }

    public String getPreFilterPattern() {
        return currChronic.getView().getPreFilterExpression();
    }

    public void setPreFilterPattern(String preFilterPattern) {
        currChronic.getView().setPreFilterExpression(preFilterPattern);
    }


    public void setCurrentDir(AbstractMovieTreeNode movieDirNode) {
        LOGGER.debug("set current dir " + movieDirNode.toString()); //NOI18N
        movieFilter.setCurrentDir(movieDirNode);
        currChronic.getView().setCurrentSelectedDir(movieDirNode.getPathName());
        pushChronik();
    }

    public String getFilterInfoText() {
        return movieFilter.getFilterInfoText(currChronic);
    }

    /**
     * @return the currentSelectedDir
     */
    public AbstractTreeNode getCurrentSelectedDir() {
        return movieFilter.getCurrentSelectedDir();
    }

    /**
     * @return the listChildMovies
     */
    public boolean isListChildMovies() {
        return currChronic.getView().isListChildMovies();
    }

    public void setListChildMovies(boolean enabled) {
        currChronic.getView().setListChildMovies(enabled);
    }

    public void setPruneTree(boolean selected) {
        currChronic.getView().setPruneTree(selected);
    }

    /**
     * @return the pruneTree
     */
    public boolean isPruneTree() {
        return currChronic.getView().isPruneTree();
    }

    /**
     * @return the currentDisplayedMovieNodes
     */
    public List<AbstractLeafNode> getCurrentDisplayedMovieNodes() {
        return currentDisplayedMovieNodes;
    }

    private void pushChronik() {
        try {
            final ChronicEntry copy = (ChronicEntry) currChronic.clone();
            if (!containsSameContents(chronicList, copy)) {
                copy.setId(null);
                copy.getView().setCreated(new Date());
                movieEntryjpa.create(copy);

                getChronicList().addFirst(copy);
                if (getChronicList().size() > MyjaphooAppPrefs.PRF_MAXCHRONIC.getVal()) {
                    getChronicList().removeLast();
                }
                // add it to the cached chronic list: since its a observable list, it will update the ui:
                MainApplicationController.getInstance().getChronicList().add(copy);
                chronicIndex = 0;
            }
        } catch (Exception ex) {
            LOGGER.error("error creating new chronic entry", ex); //NOI18N
        }
    }

    /**
     * @return the condenseDuplicates
     */
    public boolean isCondenseDuplicates() {
        return condenseDuplicates;
    }

    /**
     * @param condenseDuplicates the condenseDuplicates to set
     */
    public void setCondenseDuplicates(boolean condenseDuplicates) {
        this.condenseDuplicates = condenseDuplicates;
    }

    /**
     * @param structType the structType to set
     */
    public void setStructType(StructureType structType) {
        setUserDefinedStructure(Arrays.asList(structType.getDims()));
    }

    public void setOrder(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderType getOrder() {
        return orderType;
    }

    public void setUserDefinedStructure(List<GroupingDim> hierarchy) {
        currChronic.getView().setUserDefinedStructure(hierarchy);
    }

    public void setUserDefinedStructureActivated(boolean selected) {
        currChronic.getView().setUserDefinedStructureActivated(selected);
    }

    /**
     * @return the userDefinedStructure
     */
    public String getUserDefinedStruct() {
        return currChronic.getView().getUserDefinedStruct();
    }

    /**
     * @return the treeShowsMoviesUnique
     */
    public boolean isTreeShowsMoviesUnique() {
        return movieFilter.isTreeShowsMoviesUnique();
    }

    /**
     * @return the chronicList
     */
    public LinkedList<ChronicEntry> getChronicList() {
        if (chronicList.size() == 0) {
            loadChronicList();
        }
        return chronicList;
    }


    public void popChronic(ChronicEntry c) {
        try {
            currChronic = (ChronicEntry) c.clone();
        } catch (CloneNotSupportedException ex) {
            LOGGER.error("error cloning", ex); //NOI18N
        }
    }

    private void loadChronicList() {
        final List<ChronicEntry> loaded = movieEntryjpa.findChronicEntryEntities(MyjaphooAppPrefs.PRF_MAXCHRONIC.getVal(), 0);
        for (ChronicEntry loadedEntry : loaded) {
            if (!containsSameContents(chronicList, loadedEntry)) {
                chronicList.add(loadedEntry);
            }
        }
        chronicIndex = 0;
    }

    private boolean containsSameContents(LinkedList<ChronicEntry> chronicList, ChronicEntry copy) {
        for (ChronicEntry entry : chronicList) {
            if (entry.isContentequals(copy)) {
                return true;
            }
        }
        return false;
    }

    public BookMark createBookMarkFromCurrentChronic() {
        BookMark bm = new BookMark();
        bm.setView((DataView) currChronic.getView().clone());
        return bm;
    }

    void setCurrentMetaToken(MetaToken metaToken) {
        currChronic.getView().setCurrentMetaToken(metaToken);
    }

    public MetaToken getCurrentMetaToken() {
        return currChronic.getView().getCurrentMetaToken();
    }

    public void setGroupByPattern(String text) {
        currChronic.getView().setUserDefinedStruct(text);
        currChronic.getView().setUserDefinedStructureActivated(true);
    }

    public ThumbMode getThumbMode() {
        return currChronic.getView().getThumbmode();
    }

    void setThumbMode(ThumbMode mode) {
        currChronic.getView().setThumbmode(mode);
    }

    public List<String> getUsedLiterals() {
        return usedLiterals;
    }

    private boolean updateChronicIndex(int newVal) {
        boolean updateSuccessfull = true;
        chronicIndex = newVal;
        if (chronicIndex < 0) {
            chronicIndex = 0;
            updateSuccessfull = false;
        }
        if (chronicIndex >= getChronicList().size()) {
            chronicIndex = getChronicList().size()-1;
            updateSuccessfull = false;
        }
        return updateSuccessfull;
    }

    public ChronicEntry getBackHistory() {
        if (!updateChronicIndex(chronicIndex+1)) {
            return null;
        }
        ChronicEntry chronicEntry = getChronicList().get(chronicIndex);
        return chronicEntry;
    }

    public ChronicEntry getForwardHistory() {
        if (!updateChronicIndex(chronicIndex-1)) {
            return null;
        }
        ChronicEntry chronicEntry = getChronicList().get(chronicIndex);
        return chronicEntry;
    }
}
