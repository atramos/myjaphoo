/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree.grouping;

import org.myjaphoo.model.FileSubstitution;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.BookMark;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.Substitution;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.grouping.GroupingExecutionContext;
import org.myjaphoo.model.logic.FileSubstitutionImpl;
import org.myjaphoo.model.logic.MovieEntryJpaController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Hält Kontext Informationen, die die einzelnen Gruppierer benötigen.
 * Notwendig, um den allgemeinen Grouping Code von Details, wie etwa
 * spezielle DAO Klassen abzukapseln.
 * @author mla
 */
public class GroupingExecutionContextImpl implements GroupingExecutionContext {

    private List<JoinedDataRow> allEntries;
    private FileSubstitution fileSubstitution = new FileSubstitutionImpl();
    private MovieEntryJpaController jpa = new MovieEntryJpaController();
    private ExecutionContext context;

    public GroupingExecutionContextImpl(List<JoinedDataRow> allEntries, ExecutionContext context) {
        this.allEntries = allEntries;
        this.context = context;
    }

    @Override
    public Set<MovieEntry> getAllEntriesToGroup() {
        Set<MovieEntry> distinctEntries = new HashSet<MovieEntry>(allEntries.size());
        for (JoinedDataRow cr : allEntries) {
            distinctEntries.add(cr.getEntry());
        }
        return distinctEntries;
    }

    @Override
    public List<JoinedDataRow> getAllCombinationsToGroup() {
        return allEntries;
    }

    @Override
    public List<Token> getTagList() {
        return CacheManager.getCacheActor().getImmutableModel().getTokenSet().asList();
    }

    @Override
    public FileSubstitution getFileSubstitution() {
        return fileSubstitution;
    }

    @Override
    public List<BookMark> getBookMarks() {
        return jpa.findBookMarkEntities();
    }

    @Override
    public Map<String, Substitution> createSubst(List<BookMark> bookmarks) {
        return jpa.createSubst(bookmarks);
    }

    @Override
    public ExecutionContext getFilterContext() {
        return context;
    }
}
