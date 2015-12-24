package org.myjaphoo.model.groovyparser

import org.myjaphoo.model.db.MovieEntry
import org.myjaphoo.model.filterparser.expr.JoinedDataRow

/**
 * metadata definitions as shortcuts to properties of the underlying datamodel.
 * @author lang
 * @version $Id$
 *
 */

class GroovyFilteringMetaClassDefs {

    private static initialized = false;

    public static void init() {
        if (!initialized) {
            initialized = true;

            MovieEntry.metaClass.getPath = {->
                return delegate.getCanonicalPath();
            }

            MovieEntry.metaClass.getDir = {->
                return delegate.getCanonicalDir();
            }

            JoinedDataRow.metaClass.getTag = {->
                return delegate.getToken();
            }

            JoinedDataRow.metaClass.getMetatag = {->
                return delegate.getMetaToken();
            }
        }
    }
}
