package org.myjaphoo.model.logic;

import groovy.lang.Closure;
import org.myjaphoo.model.dbconfig.Commit;
import org.myjaphoo.model.dbconfig.DBConnection;

import javax.persistence.EntityManager;

/**
 * Base class for all dao classes.
 * Handles the database connection.
 * @author lang
 * @version $Id$
 */
public abstract class AbstractDao {
    /**
     * holds a special database connection. If null, then the default- db connection gets used.
     */
    private DBConnection dbConn;

    public AbstractDao(DBConnection dbConn) {
        super();
        this.dbConn = dbConn;
    }

    public AbstractDao() {

    }

    protected DBConnection getDbConn() {
        if (dbConn == null) {
            return MyjaphooDB.singleInstance().getConnection();
        } else {
            return dbConn;
        }
    }

    public void withTransaction(final Closure closure) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                closure.call(em);
            }
        };
    }
}
