/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.processing;

import javax.persistence.EntityManager;
import org.myjaphoo.model.dbconfig.Commit;

import org.myjaphoo.model.logic.MyjaphooDB;


/**
 *
 * @author mla
 */
public class TransactionalProcessWrapper<T> implements ListProcessor<T> {

    private ListProcessor<T> delegator;

    public TransactionalProcessWrapper(ListProcessor<T> delegator) {
        this.delegator = delegator;
    }

    @Override
    public void startProcess() {

        new Commit(MyjaphooDB.singleInstance().getConnection()) {


            @Override
            protected void run(EntityManager em) {
                delegator.startProcess();
            }
        };
    }

    @Override
    public void process(final T entry) throws Exception {

        new Commit(MyjaphooDB.singleInstance().getConnection()) {

            @Override
            protected void run(EntityManager em) throws Exception {
                delegator.process(entry);
            }
        };
    }

    @Override
    public void stopProcess() {

        new Commit(MyjaphooDB.singleInstance().getConnection()) {

            @Override
            protected void run(EntityManager em) {
                delegator.stopProcess();
            }
        };
    }

    @Override
    public String shortName(T t) {
        return delegator.shortName(t);
    }

    @Override
    public String longName(T t) {
        return delegator.longName(t);
    }
};
