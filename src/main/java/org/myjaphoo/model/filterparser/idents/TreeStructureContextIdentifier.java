package org.myjaphoo.model.filterparser.idents;

import org.mlsoft.structures.TreeStructure;
import org.myjaphoo.model.filterparser.expr.ExprType;

/**
 * Base class for identifiers that return values from movie entries.
 * All those identifiers act also as "qualifier" that deliver a movie entry as context.
 */
public abstract class TreeStructureContextIdentifier extends FixIdentifier<TreeStructure, TreeStructure> {

    public TreeStructureContextIdentifier(String name, String descr, String exampleUsage, ExprType exprType) {
        super(name, descr, exampleUsage, exprType, TreeStructure.class, TreeStructure.class, false, false);

    }

    @Override
    public TreeStructure extractQualifierContext(TreeStructure row) {
        return row;
    }
}