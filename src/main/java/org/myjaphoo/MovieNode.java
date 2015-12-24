/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author lang
 */
public class MovieNode extends AbstractLeafNode {

    private MovieEntry movieEntry;
    /** duplicates, lazy initialized. */
    private ArrayList<MovieEntry> condensedDuplicates = null;

    /** true, wenn dieser movie unter der aktuellen gruppierung nur einmal im tree hängt. */
    private boolean unique;

    /** "empty" entry that we put in here, when the node is destructed to prevent NPEs. */
    private static final MovieEntry DESTRUCTED_ENTRY = new MovieEntry();
    static {
        DESTRUCTED_ENTRY.setId(-100L);
        DESTRUCTED_ENTRY.setName("");
        DESTRUCTED_ENTRY.setCanonicalDir("");
        DESTRUCTED_ENTRY.setChecksumCRC32(0L);
        DESTRUCTED_ENTRY.setComment("");
        DESTRUCTED_ENTRY.setTitle("");
    }

    private transient JoinedDataRow row;

    public MovieNode(MovieEntry movieEntry, boolean unique, JoinedDataRow row) {
        this.movieEntry = movieEntry;
        this.unique = unique;
        this.row = row;
    }

    public void addCondensedDuplicate(MovieEntry duplicate) {
        getCondensedDuplicates().add(duplicate);
    }

    public int getCondensedDuplicatesSize() {
        if (condensedDuplicates == null) {
            return 0;
        } else {
            return condensedDuplicates.size();
        }
    }

    /**
     * @return the movieEntry
     */
    public MovieEntry getMovieEntry() {
        return movieEntry;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * @return the duplicates
     */
    public ArrayList<MovieEntry> getCondensedDuplicates() {
        if (condensedDuplicates == null) {
            condensedDuplicates = new ArrayList<MovieEntry>(1);
        }
        return condensedDuplicates;
    }

    @Override
    public String getName() {
        if (movieEntry != null) {
            return movieEntry.getName();
        } else {
            return "entry is null"; //NOI18N
        }
    }

    @Override
    public long getSizeOfContainingMovies() {
        return movieEntry.getFileLength();
    }

    @Override
    public String getCanonicalDir() {
        return movieEntry.getCanonicalDir();
    }

    @Override
    public long getFileLength() {
        return movieEntry.getFileLength();
    }

    @Override
    public String getCanonicalPath() {
        return movieEntry.getCanonicalPath();
    }

    /**
     * @return the unique
     */
    public boolean isUnique() {
        return unique;
    }

    /**
     * @return the hasDups
     */
    public boolean isHasDups() {
        return CacheManager.getCacheActor().getImmutableModel().getDupHashMap().hasDuplicates(movieEntry.getChecksumCRC32());
    }

    /**
     * @return the dupsInDatabase
     */
    public Collection<MovieEntry> getDupsInDatabase() {
        return CacheManager.getCacheActor().getImmutableModel().getDupHashMap().getDuplicatesForMovie(movieEntry);
    }

    @Override
    public void destruct() {
        super.destruct();
        movieEntry = DESTRUCTED_ENTRY;
        if (condensedDuplicates != null) {
            condensedDuplicates.clear();
        }
    }

    @Override
    public String getTitle() {
        return movieEntry.getTitle();
    }

    @Override
    public String getComment() {
        return movieEntry.getComment();
    }

    /**
     * updated den entry für diese node. Wird nach Modell-Änderungen
     * gemacht, um nicht das ganze GUI modell auszutauschen.
     */
    public void updateNode(MovieEntry changedEntry) {
        this.movieEntry = changedEntry;
    }


    public JoinedDataRow getRow() {
        return row;
    }

    public void setRow(JoinedDataRow row) {
        this.row = row;
    }
}
