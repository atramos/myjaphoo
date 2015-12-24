package org.myjaphoo.gui.files;


import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File node, which gets shown in the file view.
 */
public class FileNode {

    private static Logger logger = LoggerFactory.getLogger(FileNode.class);

    private Path path;

    private FileNode parent;

    private String name;

    private ArrayList<FileNode> children = null;

    private FileDBInformation info;

    /**
     * if this file represents an entry in the current database, this node contains the representing node.
     * this could be either a STructure node repesenting a directory or a entry node representing the file.
     */
    private transient AbstractMovieTreeNode matchingNodeInDb = null;
    private transient boolean initialized = false;

    private String mappedPath;

    private boolean showInMovieTree = false;

    public FileNode(Path p, FileDBInformation info) {
        this.path = p;
        this.name = p != null ? getFileName(p) : "";
        this.info = info;
        this.mappedPath = info.checkMapping(p);
    }

    public String getName() {
        return name;
    }

    private String getFileName(Path path) {
        Path fn = path.getFileName();
        return fn != null ? fn.toString() : path.toString();
    }


    public FileNode getParent() {
        return parent;
    }

    public List<FileNode> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
            if (path == null) {
                addFileStoreChildren();

            } else {
                createChildren(path);
            }
        }
        return children;
    }

    private void addFileStoreChildren() {
        for (Path p : FileSystems.getDefault().getRootDirectories()) {
            FileNode child = new FileNode(p, info);
            child.parent = this;
            children.add(child);
        }
    }

    public void createChildren(Path parentPath) {
        if (Files.isDirectory(parentPath)) {
            try (DirectoryStream<Path> ds =
                         Files.newDirectoryStream(parentPath)) {
                for (Path p : ds) {
                    FileNode child = new FileNode(p, info);
                    child.parent = this;
                    children.add(child);
                }

            } catch (AccessDeniedException ade) {
                logger.warn("access denied for " + ade.getLocalizedMessage());
            } catch (IOException e) {
                logger.warn("error scanning file tree ", e);
            }
        }
    }


    public static FileNode createRoot(MyjaphooController controller) {
        return new FileNode((Path) null, new FileDBInformation(controller));
    }


    public File getFile() {
        // todo rework using paths
        return path != null ? path.toFile() : null;
    }

    public boolean isShowInMovieTree() {
        return showInMovieTree;
    }

    public void setShowInMovieTree(boolean showInMovieTree) {
        this.showInMovieTree = showInMovieTree;
    }

    public String getMappedPath() {
        return mappedPath;
    }

    public void setMappedPath(String mappedPath) {
        this.mappedPath = mappedPath;
    }

    public List<FileNode> collectSelectedNodes() {
        List<FileNode> result = new ArrayList<FileNode>();
        collectSelectedNodes(result, this);
        return result;
    }

    private void collectSelectedNodes(List<FileNode> result, FileNode fileNode) {
        // iterate nodes via properties, not methods; we will only
        // traverse already existing ones, not create nodes:
        if (fileNode.isShowInMovieTree()) {
            result.add(fileNode);
        }
        if (fileNode.children != null) {
            for (FileNode child : fileNode.children) {
                collectSelectedNodes(result, child);
            }
        }

    }

    public boolean isInDb() {
        return getMatchingNode() != null;
    }

    public String getFoundPathInDb() {
        if (getMatchingNode() != null) {
            return matchingNodeInDb.getPathName();
        } else {
            return null;
        }
    }

    private AbstractMovieTreeNode getMatchingNode() {
        if (!initialized) {
            if (path != null) {
                // todo rework using paths
                matchingNodeInDb = info.findNodeOfDB(path.toFile().getAbsolutePath());
            }
            initialized = true;
        }
        return matchingNodeInDb;
    }

    public int getNumOfContainingMovies() {
        if (getMatchingNode() != null) {
            return getMatchingNode().getNumOfContainingMovies();
        } else {
            return 0;
        }
    }

    public String getFileSizeInDB() {
        if (getMatchingNode() != null) {
            return Utils.humanReadableByteCount(getMatchingNode().getSizeOfContainingMovies());
        } else {
            return "0";
        }
    }
}
