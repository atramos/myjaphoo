package org.myjaphoo.model.groovyparser;

import groovy.lang.Closure;
import org.apache.commons.lang.ObjectUtils;
import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.grouping.CachingPartialPathBuilder;
import org.myjaphoo.model.grouping.Path;
import org.myjaphoo.model.grouping.PathAttributes;

import java.util.ArrayList;
import java.util.Collection;

/**
 * GroovyClosurePathGrouper
 *
 * @author lang
 * @version $Id$
 */
public class GroovyClosurePathGrouper extends CachingPartialPathBuilder<String> {

    private Closure closure;

    private final Path NOTHING_PATH = new Path("groovy closure", "-- nothing --");
    public final Path[] NOTHING = new Path[]{NOTHING_PATH};

    public GroovyClosurePathGrouper(Closure closure) {
        this.closure = closure;
    }

    @Override
    protected Path createPath(String string) {
        PathAttributes[] attributes = PathAttributes.createAttributes("groovy closure", string);
        return new Path(attributes);
    }

    @Override
    public Path[] getPaths(JoinedDataRow row) {
        Collection<String> values = getIdentValuesForGrouping(getContext().getFilterContext(), row);
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
     *
     * @return
     */
    public final Collection<String> getIdentValuesForGrouping(ExecutionContext context, JoinedDataRow row) {
        try {
            Object value = closure.call(row);
            ArrayList<String> result = new ArrayList<>();
            if (value instanceof Collection) {
                for (Object o : ((Collection) value)) {
                    result.add(ObjectUtils.toString(o));
                }
            } else {
                result.add(ObjectUtils.toString(value));
            }
            return result;
        } catch (Exception e) {
            throw new ParserException(null, e.getLocalizedMessage(), 0, 0);
        }
    }

    @Override
    public boolean needsTagRelation() {
        return true; // can we dedice this better?
    }

    @Override
    public boolean needsMetaTagRelation() {
        return true; // can we dedice this better?
    }
}
