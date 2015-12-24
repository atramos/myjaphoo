/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree.grouping;

import org.apache.commons.lang.StringUtils;
import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.movietree.StructureCharacteristicsFactory;
import org.myjaphoo.model.dbcompare.DBDiffCombinationResult;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.grouping.GroupingDim;
import org.myjaphoo.model.grouping.GroupingExecutionContext;
import org.myjaphoo.model.grouping.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Ein abstrakter grouper, der die Gruppierung durch angegebene Pfade durchfÃ¼hrt.
 * Der Pfad wird durch ein String-array angegeben. Jeder Pfad muss fÃ¼r sich
 * natÃ¼rlich eindeutig sein.
 * @author mla
 */
public abstract class AbstractByPathGrouper implements GroupAlgorithm {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractByPathGrouper.class.getName());
    private Map<Path, MovieStructureNode> dirMap = new HashMap<Path, MovieStructureNode>(50000);

    /**
     * used to condense path+entry combinations which where created by
     * the cartesion product of entry+tag+metatag, but which are not
     * identified by the grouping definition.
     */
    static class UniquePath {
        
        private Path path;
        private long movieId;
        
        public UniquePath(Path path, long movieId) {
            this.path = path;
            this.movieId = movieId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UniquePath that = (UniquePath) o;

            if (movieId != that.movieId) return false;
            if (!path.equals(that.path)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + (int) (movieId ^ (movieId >>> 32));
            return result;
        }
    }
    private Set<UniquePath> uniqueSet = new HashSet<UniquePath>(50000);
    
    @Override
    public List<MovieStructureNode> findParents(AbstractMovieTreeNode root, JoinedDataRow row) {
        Path paths[] = getPaths(row);
        return buildDirNodesFromPaths(root, paths, row);
    }
    
    private ArrayList<MovieStructureNode> buildDirNodesFromPaths(AbstractMovieTreeNode root, Path paths[], JoinedDataRow row) {
        if (paths != null) {
            ArrayList<MovieStructureNode> result = new ArrayList<MovieStructureNode>(paths.length);
            for (Path path : paths) {
                if (row instanceof DBDiffCombinationResult) {
                    // we add all diff nodes, since each node contains a diff information.
                    // we do not take care for unique paths, as this would be not so easy anyway:
                    // we would need to take both left and right part of the node into account.
                    MovieStructureNode foundDirNode = getOrCreateDirNode((MovieStructureNode) root, path);
                    result.add(foundDirNode);

                } else {
                    // take care that the nodes are unique, this could happen due to grouping/filter issues
                    // but normally we do not want to show identical nodes twice
                    long id = row.getEntry().getId();

                    UniquePath uniquePath = new UniquePath(path, id);
                    if (!uniqueSet.contains(uniquePath)) {
                        uniqueSet.add(uniquePath);
                        MovieStructureNode foundDirNode = getOrCreateDirNode((MovieStructureNode) root, path);
                        result.add(foundDirNode);
                    }
                }
            }
            return result;
        }
        return null;
    }
    
    private MovieStructureNode createDirNode(MovieStructureNode root, Path path) {
        if (path.isRoot()) {
            // we are recursively iterated to the root:
            return root;
        }
        MovieStructureNode node = createStructureNode(path);

        // find the parent:
        Path parentPath = path.getParentPath();
        MovieStructureNode parent = getOrCreateDirNode(root, parentPath);
        
        parent.addChild(node);
        dirMap.put(path, node);
        return node;
    }
    
    protected MovieStructureNode createStructureNode(Path path) {
        MovieStructureNode node = new MovieStructureNode(path.getLastPathName());
        node.setGroupingExpr(path.getLastPathBuilder());
        node.setCanonicalDir(path.getCanonicalDir());
        node.setStructureCharacteristics(StructureCharacteristicsFactory.getCharacteristics(path.getPathMarker()));
        return node;
    }
    
    private MovieStructureNode getOrCreateDirNode(MovieStructureNode root, Path path) {
        MovieStructureNode node = dirMap.get(path);
        if (node == null) {
            node = createDirNode(root, path);
        }
        return node;
    }
    
    @Override
    public AbstractMovieTreeNode findAccordingNode(AbstractMovieTreeNode root, String path) {
        // first check, if its a structure path expression:
        String[] parts = StringUtils.split(path, "/\\:");
        Path ppath = new Path(GroupingDim.Directory, parts);
        MovieStructureNode node = dirMap.get(ppath);
        if (node != null) {
            return node;
        }
        // or is it a directory path expression:
        // then do a stupid tree search by the path components:
        return findPath(root, parts, 0);
    }
    
    private AbstractMovieTreeNode findPath(AbstractMovieTreeNode root, String[] parts, int i) {
        if (i >= parts.length) {
            // path has full matched:
            return root;
        }
        String currSearchName = parts[i];
        
        for (AbstractTreeNode child : root.getChildren()) {
            AbstractMovieTreeNode movieChild = (AbstractMovieTreeNode) child;
            if (currSearchName.equals(movieChild.getName())) {
                // found name on this level: go deeper:
                return findPath(movieChild, parts, i + 1);
            }
        }
        // no match on this level;
        return null;
    }
    
    @Override
    public void pruneEmptyDirs(AbstractMovieTreeNode node) {
        // mittels tiefensuche traversieren:
        for (AbstractTreeNode child : node.getChildren()) {
            if (child instanceof MovieStructureNode) {
                pruneEmptyDirs((MovieStructureNode) child);
            }
        }
        
        if (node.getChildCount() == 1 && node.getChildAt(0) instanceof MovieStructureNode) {

            // nur ein kind, welches selbst ein dir ist; wir kÃ¶nnen die nodes also
            // zusammenfassen:
            MovieStructureNode child = (MovieStructureNode) node.getChildAt(0);
            String newname = ((MovieStructureNode) node).getName() + "/" + child.getName();
            
            ((MovieStructureNode) node).setName(newname);
            node.removeChild(child);
            child.setParent(null);
            for (AbstractMovieTreeNode childOfChild : child.getChildren()) {
                node.addChild(childOfChild);
                childOfChild.setParent(node);
            }
        }
    }


    @Override
    public AbstractMovieTreeNode getOrCreateRoot() {
        return new MovieStructureNode("Structure");
    }
    
    @Override
    public void postProcess(AbstractMovieTreeNode root) {
        root.sortNaturalByName();
        dirMap.clear();
    }
    
    @Override
    public void preProcess(GroupingExecutionContext context) {
    }
    
    public abstract Path[] getPaths(JoinedDataRow row);

}
