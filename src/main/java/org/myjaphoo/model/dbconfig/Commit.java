/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.dbconfig;

import javax.persistence.EntityManager;
import org.myjaphoo.model.logic.dbhandling.TransactionBoundaryDelegator;

/**
 * a transactional block, that changes data in the database.
 * @author lang
 */
abstract public class Commit {

    private DBConnection conn;

    public Commit(DBConnection conn) {
        this.conn = conn;
        execute();
    }

    private void execute() {

        conn.getTransactionHandler().doInNewTransaction(new TransactionBoundaryDelegator.CommitBlock() {

            @Override
            public void runCommitBlock(EntityManager em) throws Exception {
                Commit.this.run(em);
            }
        });
    }

    abstract protected void run(EntityManager em) throws Exception;
}