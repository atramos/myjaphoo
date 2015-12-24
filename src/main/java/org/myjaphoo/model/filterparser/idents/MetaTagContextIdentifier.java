package org.myjaphoo.model.filterparser.idents;

import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

/**
 * Base class for identifiers that return values from metatags.
 * All those identifiers act also as "qualifier" that deliver a metatag as context.
 */
public abstract class MetaTagContextIdentifier extends FixIdentifier<JoinedDataRow, MetaToken> {

    public MetaTagContextIdentifier(String name, String descr, String exampleUsage, ExprType exprType) {
        super(name, descr, exampleUsage, exprType, JoinedDataRow.class, MetaToken.class, false, true);

    }

    @Override
    public MetaToken extractQualifierContext(JoinedDataRow row) {
        return row.getMetaToken();
    }
}