package org.myjaphoo.model.filterparser.idents;

import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

/**
 * Base class for identifiers that return values from movie entries.
 * All those identifiers act also as "qualifier" that deliver a movie entry as context.
 */
public abstract class EntryContextIdentifier extends FixIdentifier<JoinedDataRow, MovieEntry> {

    public EntryContextIdentifier(String name, String descr, String exampleUsage, ExprType exprType) {
        super(name, descr, exampleUsage, exprType, JoinedDataRow.class, MovieEntry.class, false, false);

    }

    @Override
    public MovieEntry extractQualifierContext(JoinedDataRow row) {
        return row.getEntry();
    }
}