package org.myjaphoo.model.filterparser.idents;

import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

/**
 * Base class for identifiers that return values from tags.
 * All those identifiers act also as "qualifier" that deliver a tag as context.
 */
public abstract class TagContextIdentifier extends FixIdentifier<JoinedDataRow, Token> {

    public TagContextIdentifier(String name, String descr, String exampleUsage, ExprType exprType) {
        super(name, descr, exampleUsage, exprType, JoinedDataRow.class, Token.class, true, false);

    }

    @Override
    public Token extractQualifierContext(JoinedDataRow row) {
        return row.getToken();
    }
}