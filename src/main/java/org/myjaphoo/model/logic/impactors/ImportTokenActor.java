/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.impactors;


import groovyx.gpars.actor.DynamicDispatchActor;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.logic.MyjaphooDB;
import org.myjaphoo.model.logic.TokenJpaController;
import org.myjaphoo.model.logic.dbhandling.ThreadLocalTransactionBoundaryHandler;
import org.myjaphoo.model.logic.dbhandling.TransactionBoundaryDelegator.CommitBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

/**
 * Dieser Actor übernimmt das anlegen u. verlinken von Tokens
 * für Entries innerhalb des Import-Prozesses.
 * Damit werden alle Tokenimporte sequenziell durchgeführt, anstatt
 * parallel, was ansonsten zusätzlichen Synchronisierungsaufwand bedeuted hätte.
 *
 * @author mla
 */

public class ImportTokenActor extends DynamicDispatchActor {

    private static Logger logger = LoggerFactory.getLogger(ImportTokenActor.class.getName());
    private static final int CLEARAFTERVAL = 50;
    private int counter = 0;
    /**
     * Eigenen default transaction handler einrichten, der einen eigenen
     * EntitManager hat.
     */
    private ThreadLocalTransactionBoundaryHandler tr =
            new ThreadLocalTransactionBoundaryHandler(MyjaphooDB.singleInstance().getConnection());

    public void onMessage(StopMessage msg) {
        stop();
    }

    public void onMessage(AssignTokenMsg msg) {
        try {
            createAndAssignTokens(msg);

        } catch (Exception e) {
            logger.error("error creating/assigning tokens!", e);
        }
    }

    private void createAndAssignTokens(final AssignTokenMsg msg) {
        tr.doInNewTransaction(new CommitBlock() {

            @Override
            public void runCommitBlock(EntityManager em) throws Exception {
                counter++;
                if (counter % CLEARAFTERVAL == 0) {
                    em.clear();
                }
                MovieEntry entry = em.find(MovieEntry.class, msg.getMovieId());
                for (String tokenname : msg.getTokenNames()) {
                    assignToken(em, entry, tokenname);
                }
            }
        });
    }

    private void assignToken(EntityManager em, MovieEntry entry, String tokenname) {

        Token token = TokenJpaController.findTokenByName(em, tokenname);
        if (token == null) {
            // need to create the token:
            Token newToken = new Token();
            newToken.setName(tokenname);
            Token rootToken = TokenJpaController.findRootToken(em);
            newToken.setParent(rootToken);
            rootToken.getChildren().add(newToken);
            em.persist(newToken);
            token = newToken;
        }
        if (!entry.getTokens().contains(token)) {
            entry.getTokens().add(token);
        }
        if (!token.getMovieEntries().contains(entry)) {
            token.getMovieEntries().add(entry);
        }
    }
}
