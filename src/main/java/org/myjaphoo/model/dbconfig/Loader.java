/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.dbconfig;

import javax.persistence.EntityManager;
import org.myjaphoo.model.logic.dbhandling.TransactionBoundaryDelegator;

/**
 * a transaction, which is only intendet for loading data.
 * @author lang
 */
abstract public class Loader<T> {

    private DBConnection conn;

    public Loader(DBConnection conn) {
        this.conn = conn;
    }

    public T execute() {
        return conn.getTransactionHandler().doLoading(new TransactionBoundaryDelegator.LoaderBlock<T>() {

            @Override
            public T runLoadBlock(EntityManager em) {
                return Loader.this.run(em);
            }
        });
    }

    abstract protected T run(EntityManager em);
}