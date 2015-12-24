package org.myjaphoo.model.dbcompare;

import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A combination set, which contains all combination results and additional information.
 */
public class CombinationSet {

    private ArrayList<JoinedDataRow> result;
    private Collection<MovieEntry> movies;

    private boolean needsTagRelation ;
    private boolean needsMetaTagRelation;

    public CombinationSet(ArrayList<JoinedDataRow> result, Collection<MovieEntry> movies, boolean needsTagRelation,
                          boolean needsMetaTagRelation )
    {
        this.result = result;
        this.movies = movies;
        this.needsTagRelation = needsTagRelation;
        this.needsMetaTagRelation = needsMetaTagRelation;
    }

    public ArrayList<JoinedDataRow> getResult() {
        return result;
    }

    public Collection<MovieEntry> getMovies() {
        return movies;
    }

    public boolean isNeedsTagRelation() {
        return needsTagRelation;
    }

    public boolean isNeedsMetaTagRelation() {
        return needsMetaTagRelation;
    }
}
