/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.expr;

import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;


/**
 * This represents a "row" during filtering and grouping with all relevant data.
 * This contains "joined" data from other entities if necessary.
 *
 * @author lang
 */
public class JoinedDataRow {

    /**
     * the movie entry for filtering.
     */
    private MovieEntry entry;
    /**
     * a joined tag, if tags get joined for this row.
     */
    private Token token;
    /**
     * a joined meta-tag, if tags get joined for this row.
     */
    private MetaToken metaToken;

    public JoinedDataRow() {
    }

    public JoinedDataRow(MovieEntry entry, Token token, MetaToken metaToken) {
        setEntry(entry);
        setToken(token);
        setMetaToken(metaToken);
    }

    /**
     * @return the entry
     */
    public MovieEntry getEntry() {
        return entry;
    }

    /**
     * @return the token
     */
    public Token getToken() {
        return token;
    }

    /**
     * @return the metaToken
     */
    public MetaToken getMetaToken() {
        return metaToken;
    }

    public void setEntry(MovieEntry entry) {
        this.entry = entry;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setMetaToken(MetaToken metaToken) {
        this.metaToken = metaToken;
    }
}
