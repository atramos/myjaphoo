package org.myjaphoo.gui.filtereditor;

import org.jdesktop.swingx.HorizontalLayout;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.editor.rsta.CachedHints;
import org.myjaphoo.model.db.BookMark;
import org.myjaphoo.model.filterparser.FilterBrickParser;
import org.myjaphoo.model.filterparser.functions.Function;
import org.myjaphoo.model.filterparser.idents.FixIdentifier;
import org.myjaphoo.model.filterparser.idents.Identifiers;
import org.myjaphoo.model.filterparser.operator.AbstractOperator;
import org.myjaphoo.model.filterparser.operator.Operators;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * PreFilterToolbar
 *
 * @author mla
 * @version $Id$
 */
public class PreFilterToolbar extends JToolBar {

    private MyjaphooController controller;
    private FilterEditorPanel filterEditorPanel;
    private FilterBrickParser filterBrickParser = new FilterBrickParser();

    public PreFilterToolbar(MyjaphooController controller, FilterEditorPanel filterEditorPanel) {
        this.controller = controller;
        this.filterEditorPanel = filterEditorPanel;
        setLayout(new HorizontalLayout());
    }

    public FilterEditorPanel getFilterEditorPanel() {
        return filterEditorPanel;
    }

    public void addBrick(FilterChooser filterChooser) {
        int index = findNextIndex();
        add((Component) filterChooser, index);
    }

    private int findNextIndex() {
        for (int i = 0; i < getComponentCount(); i++) {
            if (!(getComponent(i) instanceof FilterChooser)) {
                return i;
            }
        }
        return getComponentCount();
    }

    public void updateFilterBricks(String preFilterExpr) {
        removeAllFilter();

        FilterBrickParser.ParsedFilterBricks filterBricks = filterBrickParser.parseFilterBrickExpression(preFilterExpr);

        for (FilterBrickParser.IdentFilterBrick identBrick : filterBricks.bricks) {
            createBrick(identBrick);
        }
        for (String bookmarkName : filterBricks.bookmarkBricks) {
            createBookmarkBrick(bookmarkName);
        }
        for (FilterBrickParser.AttrFilterBrick identBrick : filterBricks.attrbricks) {
            addEntryAttrFilter(identBrick.attrFunction, identBrick.attribute, identBrick.literalStr, identBrick.operator);
        }

        // if there are no filter bricks at all, then add the default ones:
        if (findNextIndex() == 0) {
            // add the standard filter bricks:
            addTextFilter();
            addTagFilter();
            addBookmarkFilter(null);
        }
    }

    private void createBookmarkBrick(String name) {
        addBookmarkFilter(name);
    }

    private void createBrick(FilterBrickParser.IdentFilterBrick identBrick) {
        addIdentifierFilter(identBrick.ident.getName(), identBrick.ident, identBrick.literalStr, identBrick.operator);
    }

    public void removeAllFilter() {
        Component[] comps = getComponents().clone();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof FilterChooser) {
                FilterChooser cbfc = (FilterChooser) comps[i];
                remove((Component) cbfc);
            }
        }
        repaint();
    }

    public void addBookmarkFilter(String value) {
        addBrick(new ComboBoxFilterChooser(this, "Bookmark", MainApplicationController.getInstance().getBookmarkList(), value) {

            @Override
            public String createFilterExpression(Object selectedItem) {
                if (selectedItem instanceof BookMark) {
                    BookMark bm = (BookMark) selectedItem;
                    return " $'" + bm.getName() + "' ";
                } else {
                    return null;
                }
            }
        });
    }

    public void addIdentifierFilter(String title, final FixIdentifier ident, Object value, AbstractOperator op) {
        addIdentifierFilter(title, ident, CachedHints.getHintsForIdentifier(ident), value, op);
    }

    public void addIdentifierFilter(String title, final FixIdentifier ident, List list, Object value, AbstractOperator op) {
        addBrick(new IdentifierComboboxFilterChooser(this, ident, title, list, value, op));
    }

    public void addIdentifierFilter(final FixIdentifier ident) {
        addIdentifierFilter(ident.getName(), ident, null, Operators.LIKE);
    }

    public void addTextFilter() {
        addIdentifierFilter(Identifiers.TEXT);
    }

    public void addTagFilter() {
        addIdentifierFilter(Identifiers.TAG);
    }


    /**
     * builds the filter expression of all filter chooser of this tool bar.
     *
     * @return filter expression
     */
    public String createFilterExpression() {
        StringBuilder filter = new StringBuilder();
        for (Component comp : getComponents()) {
            if (comp instanceof FilterChooser) {
                FilterChooser chooser = (FilterChooser) comp;
                String filterExpression = chooser.getFilterExpression();
                if (filterExpression != null) {
                    if (filter.length() > 0) {
                        filter.append(" and ");
                    }
                    filter.append(filterExpression);
                }
            }
        }
        return filter.toString();
    }

    public void clearAllFilter() {
        Component[] comps = getComponents().clone();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof ComboBoxFilterChooser) {
                ComboBoxFilterChooser cbfc = (ComboBoxFilterChooser) comps[i];
                cbfc.clearFilter();
            }
        }
        repaint();
    }

    public void addEntryAttrFilter(Function func, String key) {
        addBrick(new AttributeComboboxFilterChooser(this, func.getName(), key, CachedHints.getHintsForAttributes(func, key), null));
    }

    public void addEntryAttrFilter(Function func, String key, String value, AbstractOperator op) {
        addBrick(new AttributeComboboxFilterChooser(this, func.getName(), key, CachedHints.getHintsForAttributes(func, key), value, op));
    }
}
