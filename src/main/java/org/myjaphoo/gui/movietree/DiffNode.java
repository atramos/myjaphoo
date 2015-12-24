/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree;

import org.myjaphoo.MovieNode;
import org.myjaphoo.model.FileType;
import org.myjaphoo.model.dbcompare.ComparisonSetGenerator;
import org.myjaphoo.model.dbcompare.DBDiffCombinationResult;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

/**
 *
 * A node in the movie tree, which displays a single database difference.
 * This is an extension of a MovieNode.
 * @author lang
 */
public class DiffNode extends MovieNode {

    private DBDiffCombinationResult dbdiff;

    public DiffNode(DBDiffCombinationResult dbdiff, boolean unique) {
        super(dbdiff.getEntry(), unique, dbdiff);
        this.dbdiff = dbdiff;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        return getDbdiff().getEntry().getName();

    }

    @Override
    public long getSizeOfContainingMovies() {
        return getDbdiff().getEntry().getFileLength();
    }

    @Override
    public String getCanonicalDir() {
        return getDbdiff().getEntry().getCanonicalDir();
    }

    @Override
    public long getFileLength() {
        return getDbdiff().getEntry().getFileLength();
    }

    @Override
    public String getCanonicalPath() {
        return getDbdiff().getEntry().getCanonicalPath();
    }


    @Override
    public void destruct() {
        super.destruct();
        //dbdiff = null;
    }

    @Override
    public String getTitle() {
        return getDbdiff().getEntry().getTitle();
    }

    @Override
    public String getComment() {
        return getDbdiff().getEntry().getComment();
    }

    public DBDiffCombinationResult getDbdiff() {
        return dbdiff;
    }

    public boolean is(FileType ft) {
        if (getMovieEntry() == ComparisonSetGenerator.NULL_ENTRY) {
            return  ft.is(getDbdiff().getCDBEntry().getName());
        } else {
            return ft.is(getName());
        }
    }
}
