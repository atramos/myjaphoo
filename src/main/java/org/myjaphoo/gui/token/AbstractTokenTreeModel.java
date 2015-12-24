/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.token;


import org.apache.commons.lang.StringUtils;
import org.myjaphoo.gui.editor.rsta.CachedHints;
import org.myjaphoo.gui.util.Utils;
import org.myjaphoo.gui.util.WrappedNode;
import org.myjaphoo.gui.util.WrappedNodeTreeModel;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.db.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Basisklasse für token tree modelle.
 * Bietet Funktionen, zum filtern, Aufbau des Trees u. Aufbau des Modells als flache liste.
 *
 * @author lang
 */
public class AbstractTokenTreeModel extends WrappedNodeTreeModel<Token> {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/token/resources/AbstractTokenTreeModel");

    public static final String[] COLUMNS = new String[]{
            localeBundle.getString("TAG"),
            localeBundle.getString("TYPE"),
            localeBundle.getString("NUM MEDIA"),
            localeBundle.getString("SIZE OF MEDIA DIRECT"),
            localeBundle.getString("NUM MEDIA INDIRECT"),
            localeBundle.getString("DESCRIPTION"),
            localeBundle.getString("ASSIGNED META TAGS")};
    private static Logger logger = LoggerFactory.getLogger(AbstractTokenTreeModel.class);

    public AbstractTokenTreeModel(Token newroot, boolean filter, String typedText, boolean flatList, boolean isEditable) {
        super(newroot, filter, typedText, flatList, prepareColumnList());
        this.isEditable = isEditable;
    }

    private static String[] prepareColumnList() {
        ArrayList<String> colList = new ArrayList<>();
        colList.addAll(Arrays.asList(COLUMNS));

        // add all the attributes:
        colList.addAll(CachedHints.getTagAttrKeys());
        return colList.toArray(new String[colList.size()]);
    }

    @Override
    protected boolean match(Token tok, String typedText) {
        if (typedText == null) {
            return true;
        }
        return StringUtils.containsIgnoreCase(tok.getName(), typedText);
    }

    @Override
    public Object getValueAt(Object token, int column) {
        switch (column) {
            case 0:
                return ((WrappedNode<Token>) token).getRef().getName();
            case 1:
                return ((WrappedNode<Token>) token).getRef().getTokentype();
            case 2:
                return ((WrappedNode<Token>) token).getRef().getMovieEntries().size();
            case 3:
                return Utils.humanReadableByteCount(((WrappedNode<Token>) token).getRef().sizeOfMovies());
            case 4:
                return countMoviesIndirect(((WrappedNode<Token>) token).getRef());
            case 5:
                return ((WrappedNode<Token>) token).getRef().getDescription();
            case 6:
                return createMetaTokenDescr(((WrappedNode<Token>) token).getRef());
            default:
                String attrName = getColumnName(column);
                return ((WrappedNode<Token>) token).getRef().getAttributes().get(attrName);
        }
    }

    private String createMetaTokenDescr(Token token) {
        StringBuilder b = new StringBuilder();
        for (MetaToken mt : token.getAssignedMetaTokens()) {
            if (b.length() > 0) {
                b.append(";"); //NOI18N
            }
            b.append(mt.getName());
        }
        return b.toString();
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        if (!isEditable) {
            return false;
        } else {
            return column == 0 || column == 1 || column == 5;
        }
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 0 || column == 5) {
            return String.class;
        }
        if (column == 1) {
            return TokenType.class;
        }
        return Object.class;
    }

    @Override
    public void setValueAt(Object value, Object node, int column) {
        if (!(column == 0 || column == 1 || column == 5)) {
            return;
        }
        WrappedNode<Token> token = (WrappedNode<Token>) node;
        try {
            if (column == 0) {
                token.getRef().setName((String) value);
            }
            if (column == 1) {
                token.getRef().setTokentype((TokenType) value);
            }
            if (column == 5) {
                token.getRef().setDescription((String) value);
            }
            CacheManager.getCacheActor().editToken(token.getRef());
        } catch (Exception ex) {
            logger.error("error", ex); //NOI18N
            throw new RuntimeException(ex);
        }
    }

    private int countMoviesIndirect(Token token) {
        int anz = 0;
        for (Token child : token.getChildren()) {
            anz += countMoviesIndirect(child);
            anz += child.getMovieEntries().size();
        }
        return anz;
    }
}
