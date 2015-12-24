/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.groupbyparser.expr;

import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.values.ObjectValue;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.filterparser.values.ValueSet;
import org.myjaphoo.model.grouping.CachingPartialPathBuilder;
import org.myjaphoo.model.grouping.Path;
import org.myjaphoo.model.grouping.PathAttributes;

import java.util.ArrayList;
import java.util.Collection;


/**
 *
 * @author mla
 */
class ByIdentifierPathBuilder extends CachingPartialPathBuilder<String> {

    private Expression expr;
    private String exprText;

    private final Path NOTHING_PATH = new Path(exprText, "-- nothing --");
    public final Path[] NOTHING = new Path[]{NOTHING_PATH};

    public ByIdentifierPathBuilder(Expression expr) {
        this.expr = expr;
        exprText = expr.getDisplayExprTxt();
    }

    @Override
    protected Path createPath(String string) {
        PathAttributes[] attributes = PathAttributes.createAttributes(exprText, string);
        return new Path(attributes);
    }

    @Override
    public Path[] getPaths(JoinedDataRow row) {
        Collection<String> values = getIdentValuesForGrouping(getContext().getFilterContext(), expr, row);
        if (values == null || values.isEmpty()) {
            return NOTHING;
        }
        Path[] result = new Path[values.size()];
        int i = 0;
        for (String val : values) {
            if (val == null) {
                result[i] = NOTHING_PATH;
            } else {
                result[i] = getPath(val);
            }
            i++;
        }

        return result;
    }

    /**
     * liefert die identifier als string werte für einen movie entry. I.d.r. ist das
     * genau ein wert, es können aber auch ggf. mehrere werte sein (z.b. bei tokens: mehrere
     * zugeordnete tokens, etc.).
     * Diese Funktion wird verwendet, um gruppierungen nach identifiern in der
     * gruppierungs-sprache zu ermöglichen.
     * @return 
     */
    public final Collection<String> getIdentValuesForGrouping(ExecutionContext context, Expression expr, JoinedDataRow row) {
        ArrayList<String> result = new ArrayList<String>();
        Value value = expr.evaluate(context, row);
        // special check, if a return type from groovy code contains a value itself, in that case unwrap those value
        // as the groovy code seems to have taken care about the value logic:
        if (value instanceof ObjectValue) {
            Object groovyReturnValue = ((ObjectValue)value).getValue();
            if (groovyReturnValue instanceof Value) {
                value = (Value) groovyReturnValue;
            }
        }
        if (value instanceof ValueSet) {
            ValueSet vset = (ValueSet) value;
            for (Value val : vset.getValues()) {
                result.add(val.convertToString());
            }
        } else {
            result.add(value.convertToString());
        }
        return result;
    }    


    @Override
    public boolean needsTagRelation() {
        return expr.needsTagRelation();
    }

    @Override
    public boolean needsMetaTagRelation() {
        return expr.needsMetaTagRelation();
    }
}
