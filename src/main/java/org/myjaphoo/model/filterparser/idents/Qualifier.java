package org.myjaphoo.model.filterparser.idents;

import org.myjaphoo.model.filterparser.expr.ExprType;
import org.myjaphoo.model.filterparser.processing.ProcessingRequirementInformation;
import org.myjaphoo.model.filterparser.syntaxtree.AbstractParsedPiece;
import org.myjaphoo.model.filterparser.syntaxtree.DescriptionFormatting;
import org.myjaphoo.model.filterparser.syntaxtree.SelfDescriptingElement;

import java.util.Collection;
import java.util.HashMap;

/**
 * A qualifier definition that could be used in conjunction with an identifier.
 * Qualifiers are separated by ".".
 * A concrete statement could e.g. "left.tag.parents".
 * Internally all identifiers are also qualifiers whereas not all qualifiers are identifiers. This is represented in the type hierarchy,
 * that identifiers are a subclass of Qualifiers.
 * A qualifier has the ability to produce a "context"  which is the input object for the next qualifier (or identifier)
 * in the expression.
 * As all identifiers are themself qualifiers, the last qualifier (it must always be a real identifier) produces also
 * a context object which get used by itself to get the identifier value.
 * This design makes it possible to handle contextes managed and defined by qualifiers; makes it possible to
 * have qualifiers and identifiers havening the same name (e.g. tag), and therefore to be backward compatible with
 * statements like "tag.parents", which was in the past simply a identifier with the name "tag.parents", whereas it is
 * now a qualifier expression with "tag" as qualifier and "parents" as identifier which is defined for the context "tag".
 * "tag" itself is (as in previous versions) also a identifier which returns the name of a tag.
 *
 *
 *
 */
public abstract class Qualifier<InputContext, OutputContext>  implements AbstractParsedPiece, SelfDescriptingElement, ProcessingRequirementInformation {

    private static HashMap<String, Qualifier> map = new HashMap<String, Qualifier>();
    private String name;
    private ExprType type;
    private String descr;
    private String exampleUsage;
    private boolean needsTagRelation;
    private boolean needsMetaTagRelation;

    private Class<InputContext> inputContextClass;

    private Class<OutputContext> outputContextClass;

    protected Qualifier(String name, String descr, String exampleUsage, ExprType type, Class<InputContext> inputContextClass, Class<OutputContext> outputContextClass,
                        boolean needsTagRelation, boolean needsMetaTagRelation) {
        this.name = name;
        this.type = type;
        this.exampleUsage = exampleUsage;
        this.descr = descr;
        this.needsTagRelation = needsTagRelation;
        this.needsMetaTagRelation = needsMetaTagRelation;
        this.inputContextClass = inputContextClass;
        this.outputContextClass = outputContextClass;

        map.put(name, this);
    }

    public static Qualifier mapIdent(String name) {
        return map.get(name);
    }

    /**
     * extracts the context of this qualifier.
     * @param row the current row
     * @return the context object of this qualifier.
     */
    abstract public OutputContext extractQualifierContext(InputContext row);

    public Class<InputContext> getInputContextClass() {
        return inputContextClass;
    }

    public Class<OutputContext> getOutputContextClass() {
        return outputContextClass;
    }

    @Override
    public String toString() {
        return "id:" + name;
    }

    public String getName() {
        return name;
    }

    public ExprType getType() {
        return type;
    }

    public String getDisplayExprTxt() {
        return name;
    }

    public static Collection<Qualifier> getList() {
        return map.values();
    }

    @Override
    public String getSelfDescription() {
        return DescriptionFormatting.descFmt("Identifier", getName(), getType(), getSelfShortDescription(), getExampleUsage());
    }

    @Override
    public String getSelfShortDescription() {
        return descr;
    }

    @Override
    public final boolean needsTagRelation() {
        return needsTagRelation;
    }

    @Override
    public final boolean needsMetaTagRelation() {
        return needsMetaTagRelation;
    }

    /**
     * @return the exampleUsage
     */
    @Override
    public String getExampleUsage() {
        return exampleUsage;
    }
}
