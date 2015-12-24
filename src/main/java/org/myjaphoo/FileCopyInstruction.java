/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import java.util.ArrayList;
import java.util.List;
import org.myjaphoo.FileCopying.PathOptionForCopying;
import org.myjaphoo.model.db.MovieEntry;

/**
 * Bundles all information needed for a file copy action.
 * This is encapsulated into a immutable object to make it threadsave
 * for cases, where a long running copy action takes place in the background
 * and some other actions (gui refresh, db changes, etc) run meanwhile in the
 * foreground.
 * 
 * @author lang
 */
public class FileCopyInstruction {

    /** entry to copy (not perfect to handle this as "immutable" object, as the entry could in theory change.
    however this should be not relevant for most changes as long as the entry gets not deleted from the db at all meanwhile... 
     */
    private MovieEntry movieEntry;
    /** target dir. */
    private String toDir;
    /** flag indication if wm info file should be written. */
    private boolean createWmInfoFile;
    /** option for generation the output path. */
    private FileCopying.PathOptionForCopying pathOption;
    /** structure path from the movie tree in the ui of that entry. */
    private String pathStructureName;

    public FileCopyInstruction(MovieEntry movieEntry, String toDir, boolean createWmInfoFile, PathOptionForCopying pathOption, String pathStructureName) {
        this.movieEntry = movieEntry;
        this.toDir = toDir;
        this.createWmInfoFile = createWmInfoFile;
        this.pathOption = pathOption;
        this.pathStructureName = pathStructureName;
    }

    /**
     * @return the movieEntry
     */
    public MovieEntry getMovieEntry() {
        return movieEntry;
    }

    /**
     * @return the toDir
     */
    public String getToDir() {
        return toDir;
    }

    /**
     * @return the createWmInfoFile
     */
    public boolean isCreateWmInfoFile() {
        return createWmInfoFile;
    }

    /**
     * @return the pathOption
     */
    public FileCopying.PathOptionForCopying getPathOption() {
        return pathOption;
    }

    /**
     * @return the pathStructureName
     */
    public String getPathStructureName() {
        return pathStructureName;
    }

    public static List<FileCopyInstruction> createInstructions(List<MovieNode> nodes, String toDir, boolean createWmInfoFile, PathOptionForCopying pathOptionForCopying) {
        List<FileCopyInstruction> fcis = new ArrayList<FileCopyInstruction>(nodes.size());
        for (MovieNode node : nodes) {
            fcis.add(new FileCopyInstruction(node.getMovieEntry(), toDir, createWmInfoFile, pathOptionForCopying, node.getPathName()));
        }
        return fcis;
    }
}
