/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.myjaphoo.model.grouping;

import org.myjaphoo.model.db.BookMark;
import org.myjaphoo.model.filterparser.FilterParser;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.expr.AbstractBoolExpression;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Gruppiert nach Bookmark Zugehörigkeit.
 *
 * @author mla
 */
public class BookmarkPartialGrouper extends AbstractPartialPathBuilder {

    private static final String NOASSGNMENT = "no bookmark";
    private static final Path[] NOASS = new Path[]{new Path(GroupingDim.Bookmark, NOASSGNMENT)};
    private List<BookMark> bookmarks = null;
    private AbstractBoolExpression[] bmExpr = null;

    @Override
    public void preProcess(GroupingExecutionContext context) {
	    super.preProcess(context);
        bookmarks = context.getBookMarks();
        bmExpr = new AbstractBoolExpression[bookmarks.size()];
        for (int i = 0; i < bookmarks.size(); i++) {
            BookMark bm = bookmarks.get(i);
            if (bm.getView().isFilter()) {
                FilterParser parser = new FilterParser(context.createSubst(bookmarks));
                try {
                    bmExpr[i] = parser.parse(bm.getView().getCombinedFilterExpression());
                } catch (ParserException ex) {
                    LoggerFactory.getLogger(BookmarkPartialGrouper.class.getName()).error("parser error:", ex);
                }
            }
        }

    }

    @Override
    public final Path[] getPaths(JoinedDataRow row) {
        Set<String> filteredbm = new HashSet<String>();
        for (int i = 0; i < bmExpr.length; i++) {
            if (bmExpr[i] == null) {
                filteredbm.add(NOASSGNMENT);
            } else if (bmExpr[i].evaluate(getContext().getFilterContext(), row).asBool()) {
                filteredbm.add(bookmarks.get(i).getName());
            }
        }
        Path[] result = new Path[filteredbm.size()];
        int i = 0;
        for (String name : filteredbm) {
            result[i] = new Path(GroupingDim.Bookmark, name);
            i++;
        }

        return result;
    }

    @Override
    public boolean needsTagRelation() {
        return true;
    }

    @Override
    public boolean needsMetaTagRelation() {
        return true;
    }
}
