/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic.dbhandling;

import org.myjaphoo.model.dbconfig.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Transaktionshandler, der pro thread und datenbank verbindung einen entity manager verwaltet.
 * Dieser sollte für Hintergrundthreads verwendet werden, die parallel
 * neben andren Threads auf die DB zugreifen wollen. Dieser Handler
 * brauch deshalb keine Synchronisation.
 *
 * Dieser Handler sollte eigentlich den default hanlder nach und nach ersetzen.
 *
 * @author mla
 */
public class ThreadLocalTransactionBoundaryHandler implements TransactionBoundaryDelegator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadLocalTransactionBoundaryHandler.class.getName());

    private DBConnection connection;

    private ThreadLocal<Map<String, EntityManager>> threadLocalEntityManagerMap = new ThreadLocal<Map<String, EntityManager>>() {
        @Override
        protected Map<String, EntityManager> initialValue() {
            return new HashMap<>();
        }
    };

    public ThreadLocalTransactionBoundaryHandler(final DBConnection connection) {
        this.connection = connection;
    }

    private EntityManager getEntityManager() {
        Map<String, EntityManager> map = threadLocalEntityManagerMap.get();
        String key = Long.toString(connection.getConfiguration().getId());
        EntityManager em = map.get(key);
        if (em == null) {
            // create lazy:
            em = connection.getEmf().createEntityManager();
            map.put(key, em);
        }
        return em;
    }

    @Override
    public void doInNewTransaction(CommitBlock runnable) {
        EntityManager em = null;
        boolean hasStartedTransaction = false;
        try {
            em = getEntityManager();

            hasStartedTransaction = beginOrParticipateOnTransaction(em);
            runnable.runCommitBlock(em);
            LOGGER.trace("********* commit transaction");
            if (hasStartedTransaction) {
                em.getTransaction().commit();
                if (em.getTransaction().isActive()) {
                    throw new IllegalArgumentException("internal error! transaction still active!");
                }
            }

        } catch (Throwable t) {

            if (hasStartedTransaction) {
                LOGGER.error("exception in transaction; trying rollback!", t);
                em.getTransaction().rollback();
                LOGGER.trace("********* rollbacked transaction");
            } else {
                LOGGER.trace("********* nested participated transaction: let outer transaction boundary do a rollback");
            }

            throw new RuntimeException("Exception in transaction!", t);
        } finally {
        }
    }

    private boolean beginOrParticipateOnTransaction(EntityManager em) {
        if (!em.getTransaction().isActive()) {
            LOGGER.trace("********* begin new transaction");
            em.getTransaction().begin();
            return true;
        }
        // participate on an outer nested transaction:
        // do nothing return marker false:
        LOGGER.trace("********* participate on outer nested transaction");
        return false;
    }

    @Override
    public <T> T doLoading(LoaderBlock<T> loader) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            LOGGER.debug("------- start loading..");
            T returnVal = loader.runLoadBlock(em);
            LOGGER.debug("------- finished loading");
            return returnVal;
        } catch (Throwable t) {
            LOGGER.error("exception in load block!", t);
            throw new RuntimeException("Exception in transaction!", t);
        } finally {
        }
    }

    @Override
    public void clear() {
        LOGGER.info("********* clearing entity manager");
        getEntityManager().clear();
    }

    public void close() {
        Map<String, EntityManager> map = threadLocalEntityManagerMap.get();
        String key = Long.toString(connection.getConfiguration().getId());
        EntityManager em = map.get(key);
        if (em != null) {
            getEntityManager().close();
            map.put(key, null);
        }
    }
}
