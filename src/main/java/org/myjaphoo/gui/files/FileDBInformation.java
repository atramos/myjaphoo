package org.myjaphoo.gui.files;

import org.mlsoft.structures.Trees;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.model.FileSubstitution;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.PathMapping;
import org.myjaphoo.model.logic.FileSubstitutionImpl;
import org.myjaphoo.model.util.Helper;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * delivers information about db entries from th file view.
 */
public class FileDBInformation {

    List<PathMapping> allMappings = CacheManager.getPathMappingCache().getCachedPathMappings();

    FileSubstitution fileSubstitution = new FileSubstitutionImpl();

    private MyjaphooController controller;
    private MovieStructureNode dbFilesRoot;
    private static final Trees.EqualPathComponent<AbstractMovieTreeNode> EQUAL_FUNCTION = new Trees.EqualPathComponent<AbstractMovieTreeNode>() {
        @Override
        public boolean isEqual(AbstractMovieTreeNode node, Object pathComponent) {
            return node.getName().equals(pathComponent);
        }
    };


    public FileDBInformation(MyjaphooController controller) {
        this.controller = controller;
        dbFilesRoot = controller.getFilter().createDirectoryStructuredTreeModel();
    }

    public String checkMapping(Path path) {
        for (PathMapping pm : allMappings) {
            // todo rework using paths
            if (path == null) {
                return null;
            }
            if (new File(pm.getPathPrefix()).equals(path.toFile())) {
                return pm.getSubstitution();
            }
        }
        return null;
    }

    public AbstractMovieTreeNode findNodeOfDB(String absPath) {
        AbstractMovieTreeNode foundNode = findPathInDirectoryStructure(absPath);
        if (foundNode == null) {
            // last chance: try to find the node with a remapped path:
            String mappedBackFilePath = fileSubstitution.mapBack(absPath);
            if (mappedBackFilePath != null) {
                foundNode = findPathInDirectoryStructure(mappedBackFilePath);
            }
        }
        return foundNode;
    }

    private AbstractMovieTreeNode findPathInDirectoryStructure(String absPath) {
        String[] path = Helper.splitPathName(absPath);
        String[] pathWithRoot = new String[path.length + 1];
        pathWithRoot[0] = dbFilesRoot.getName();
        for (int i = 0; i < path.length; i++) {
            pathWithRoot[i + 1] = path[i];
        }
        AbstractMovieTreeNode foundNode = Trees.pathSearchByObjectPath((AbstractMovieTreeNode) dbFilesRoot, pathWithRoot, EQUAL_FUNCTION, 0);
        return foundNode;
    }

}
