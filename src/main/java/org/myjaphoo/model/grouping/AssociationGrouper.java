/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.grouping;


import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

import java.util.HashSet;
import java.util.Set;


/**
 * Experimenteller Gruppierer, welcher versucht, assoziationen aufzuzeigen.
 * Im moment nur einstufig, d.h. direkte Zuordnungen werden aufgezeigt.
 * Fraglich, wie sinnvoll diese Funktion ist.
 *
 * -> alle direkt zugewiesenen Texte werden in keywords umgewandelt, und danach gruppiert.
 *
 * @author mla
 */
public class AssociationGrouper extends AbstractPartialPathBuilder {

    private int minLength = 3;
    private String separators = "//:\\.0123456789";

    @Override
    public void preProcess(GroupingExecutionContext context) {
    }

    @Override
    public Path[] getPaths(JoinedDataRow row) {
        // direkte assoziationen.
        return getAssPaths(row);

    }

    public final Path[] getAssPaths(JoinedDataRow row) {
        Set<String> parts = new HashSet<String>();

        split(parts, row.getEntry().getCanonicalPath());
        split(parts, row.getEntry().getComment());
        split(parts, row.getEntry().getTitle());

        split(parts, row.getToken().getName());
        split(parts, row.getToken().getDescription());

        split(parts, row.getMetaToken().getName());
        split(parts, row.getMetaToken().getDescription());

        Path[] result = new Path[parts.size()];
        String[] partsarr = parts.toArray(new String[parts.size()]);
        for (int i = 0; i < parts.size(); i++) {
            result[i] = createPath(partsarr[i]);
        }

        return result;
    }

    protected Path createPath(String word) {
        return new Path(GroupingDim.Assocation, word);
    }

    private void split(Set<String> parts, String str) {
        if (str == null) {
            return;
        }
        String[] hints = org.apache.commons.lang.StringUtils.split(str.toLowerCase(), separators);
        for (String hint : hints) {
            if (hint.length() > minLength) {
                parts.add(hint);
            }
        }
    }

    @Override
    public boolean needsTagRelation() {
        return true;
    }

    @Override
    public boolean needsMetaTagRelation() {
        return true;
    }
}
