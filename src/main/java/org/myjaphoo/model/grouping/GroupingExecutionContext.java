/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.FileSubstitution;
import org.myjaphoo.model.db.BookMark;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.Substitution;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Hält Kontext Informationen, die die einzelnen Gruppierer benötigen.
 * Notwendig, um den allgemeinen Grouping Code von Details, wie etwa
 * spezielle DAO Klassen abzukapseln.
 * @author mla
 */
public interface GroupingExecutionContext {

    Set<MovieEntry> getAllEntriesToGroup();

    List<JoinedDataRow> getAllCombinationsToGroup();

    List<Token> getTagList();

    FileSubstitution getFileSubstitution();

    List<BookMark> getBookMarks();

    Map<String, Substitution> createSubst(List<BookMark> bookmarks);

    ExecutionContext getFilterContext();
}
