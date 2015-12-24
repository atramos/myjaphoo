/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.bookmark;

import org.mlsoft.common.acitivity.Channel;
import org.mlsoft.common.acitivity.ChannelManager;
import org.mlsoft.swing.jtable.ChangeAction;
import org.mlsoft.swing.jtable.ColDescr;
import org.mlsoft.swing.jtable.MappedTableModel;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.Commons;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.editor.rsta.EditorSyntaxCellEditor;
import org.myjaphoo.gui.editor.rsta.EditorSyntaxCellRenderer;
import org.myjaphoo.gui.editor.rsta.RSTAHelper;
import org.myjaphoo.gui.util.tables.BaseTable;
import org.myjaphoo.model.db.BookMark;
import org.myjaphoo.model.filterparser.FilterParser;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.groupbyparser.GroupByParser;
import org.myjaphoo.model.logic.MovieEntryJpaController;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ResourceBundle;

import static org.mlsoft.swing.jtable.ColDescr.col;
/**
 *
 * @author mla
 */
public class BookmarkTable extends BaseTable {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/bookmark/resources/BookmarkTableModel");

    private MovieEntryJpaController jpa = new MovieEntryJpaController();

    private MyjaphooController controller;

    private MappedTableModel<BookMark> model;

    public BookmarkTable(MyjaphooController controller) {
        this.controller = controller;
        setHorizontalScrollEnabled(true);
        addHighlighter(Commons.ROLLOVER_ROW_HIGHLIGHTER);
        setFilterHeaderEnabled(true);
        init();
    }

    private void init() {
        List<BookMark> entries = MainApplicationController.getInstance().getBookmarkList();
        final ColDescr filterCol = col("view.filterExpression", "Filter", true).setCellRenderer(new EditorSyntaxCellRenderer()).setCellEditor(new EditorSyntaxCellEditor());

        final ColDescr groupCol = col("view.userDefinedStruct", "Grouping structure", true).setCellRenderer(new EditorSyntaxCellRenderer()).setCellEditor(new EditorSyntaxCellEditor());

        model = MappedTableModel.configure(this, entries, BookMark.class, localeBundle,
                col("name", "Name", true),
                col("descr", "Descr", true),
                col("view.created","Created", false ),
                col("menuPath", "menuPath", true),
                filterCol,
                col("view.currentSelectedDir","Dir", false),
                groupCol,
                col("view.thumbmode", "Thumb Mode", false)
        );

        model.setChangeAction(new ChangeAction<BookMark>() {
            @Override

            public boolean onChangeNode(BookMark bm, Object oldVal, Object value, ColDescr<BookMark> col) {

                if (col == filterCol) {
                    // filter change:
                    String newFilterExpression = (String) value;
                    FilterParser parser = (FilterParser) RSTAHelper.filterParserCreator.createParser();
                    try {
                        // testen, ob der geänderte ausdruck auch geparst werden kann, und nur dann setzen:
                        parser.parse(newFilterExpression);

                    } catch (ParserException ex) {
                        LoggerFactory.getLogger(BookmarkTable.class.getName()).error("error", ex); //NOI18N
                        // per channel error rausschreiben (damits ins log-fenster ausgegeben wird):
                        Channel channel = ChannelManager.createChannel(BookmarkTable.class, "Bookmark Filter Parsing");
                        channel.errormessage(ex.getMessage());
                        return false;
                    }
                }
                if (col == groupCol) {
                    // grouping change:
                    String newFilterExpression = (String) value;
                    GroupByParser parser = (GroupByParser) RSTAHelper.groupByParserCreator.createParser();
                    try {
                        // testen, ob der geänderte ausdruck auch geparst werden kann, und nur dann setzen:
                        parser.parse(newFilterExpression);

                    } catch (ParserException ex) {
                        LoggerFactory.getLogger(BookmarkTable.class.getName()).error("error", ex); //NOI18N
                        // per channel error rausschreiben (damits ins log-fenster ausgegeben wird):
                        Channel channel = ChannelManager.createChannel(BookmarkTable.class, "Bookmark Group By Parsing");
                        channel.errormessage(ex.getMessage());
                        return false;
                    }
                }
                try {
                    jpa.edit(bm);
                    // fire change event on list:
                    MainApplicationController.getInstance().getBookmarkList().fireListElementChanged(bm);
                } catch (Exception ex) {
                    LoggerFactory.getLogger(BookmarkTable.class.getName()).error("error", ex); //NOI18N
                    throw new RuntimeException(ex);
                }
                return true;
            }
        });
    }

    public void refreshModel() {
        MainApplicationController.getInstance().reloadBookmarkList();
    }

    public MappedTableModel<BookMark> getBookMarkModel() {
        return model;
    }

}
