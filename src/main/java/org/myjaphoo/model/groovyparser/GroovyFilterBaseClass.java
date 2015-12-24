package org.myjaphoo.model.groovyparser;

import groovy.lang.Closure;
import groovy.lang.Script;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;

/**
 * GroovyFilterBaseClass
 *
 * @author lang
 * @version $Id$
 */
public abstract class GroovyFilterBaseClass extends Script {

    public GroovyFilterBaseClass() {
        super();
        GroovyFilteringMetaClassDefs.init();
    }

    /**
     * marker property to "start" a groovy script in the default filter or grouping language.
     *
     * @return
     */
    public GroovyFilterBaseClass getGroovy() {
        return this;
    }

    /**
     * some shortcuts for the joined data row objects entry, tag and metatag:
     */
    public Object getEntry() {
        JoinedDataRow row = (JoinedDataRow) getProperty("row");
        return row.getEntry();
    }

    public Object getTag() {
        JoinedDataRow row = (JoinedDataRow) getProperty("row");
        return row.getToken();
    }

    public Object getMetatag() {
        JoinedDataRow row = (JoinedDataRow) getProperty("row");
        return row.getMetaToken();
    }

    /**
     * function to define the grouping via a list of closures used for grouping.
     * @param grouper
     */
    public void group(Closure ... grouper) {
        setProperty("grouper", grouper);
    }
}
