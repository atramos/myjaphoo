/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.logic.imp.ImportDelegator;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author lang
 */
public class MovieImport {


    public void flushDatabase() {
        MyjaphooDB.singleInstance().emClear();
    }

    private void scanFiles(File dir, FilenameFilter filefilter, ArrayList<File> movFiles) {
        File[] files = dir.listFiles( filefilter);
        for (File file : files) {
            if (file.isDirectory()) {
                scanFiles(file, filefilter, movFiles);
            } else {
                //logger.info("found " + file.getAbsolutePath());
                movFiles.add(file);
            }
        }
    }

    public List<File> scanFiles(String dir, ImportDelegator importDelegator) {
        return scanFiles(dir, importDelegator.getScanFilter());
    }

    public List<File> scanFiles(String dir, String scanFilter) {
        File file = new File(dir);

        OrFileFilter suffixFilter = null;

        if (!StringUtils.isEmpty(scanFilter)) {
            String[] filters = StringUtils.split(scanFilter.toLowerCase(), ";");
            suffixFilter = new OrFileFilter();
            suffixFilter.addFileFilter(new SuffixFileFilter(filters, IOCase.INSENSITIVE));
            suffixFilter.addFileFilter(DirectoryFileFilter.DIRECTORY);
        }
        ArrayList<File> movFiles = new ArrayList<File>();
        scanFiles(file, suffixFilter, movFiles);
        return movFiles;
    }


    public static Set<String> getCanonicalUnifiedPathsOfAllEntries() {
        List<MovieEntry> allMovies = CacheManager.getCacheActor().getImmutableModel().getMovieEntrySet().asList();
        Set<String> allCanonicalPaths = new HashSet<String>(allMovies.size() * 2);
        for (MovieEntry entry : allMovies) {
            allCanonicalPaths.add(unifyPath(entry.getCanonicalPath()));
        }
        return allCanonicalPaths;
    }

    /**
     * erzeugt einen "unifizierten" pfad: \ wird durch / ersetzt.
     *
     * @param path
     * @return
     */
    public static String unifyPath(String path) {
        return StringUtils.replaceChars(path, '\\', '/');
    }
}
