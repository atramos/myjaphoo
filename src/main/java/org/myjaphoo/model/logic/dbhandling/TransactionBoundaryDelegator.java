/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.dbhandling;

import javax.persistence.EntityManager;

/**
 *
 * @author mla
 */
public interface TransactionBoundaryDelegator {

    public void doInNewTransaction(CommitBlock runnable);

    public void clear();

    public interface CommitBlock {

        void runCommitBlock(EntityManager em) throws Exception;
    };

    public <T> T doLoading(LoaderBlock<T> loader);

    public interface LoaderBlock<T> {

        T runLoadBlock(EntityManager em);
    };

    public void close();
}
