/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.processing;

import org.mlsoft.common.acitivity.Channel;
import org.mlsoft.common.acitivity.ChannelManager;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.dbconfig.Commit;
import org.myjaphoo.model.logic.MyjaphooDB;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;


/**
 *
 * @author mla
 */
public class Processing {

    public static void processMovieList(List<MovieEntry> list, EntryListProcessor processor, String activity) {
        process(list, processor, activity);
    }

    public static <T> void processBigList(List<T> list, ListProcessor<T> processor, String activity) {
        DelayedProcessWrapper wrapper = new DelayedProcessWrapper(processor);
        process(list, wrapper, activity);
    }

    /**
     * Experimentell: jeder movie wird in einer separaten transaktion ausgeführt.
     */
    public static <T> void processEachInATransaction(List<T> list, ListProcessor<T> processor, String activity) {
        TransactionalProcessWrapper<T> tpw = new TransactionalProcessWrapper(processor);
        process(list, tpw, activity);
    }

    public static <T> void processtInOneTransaction(final List<T> list, final ListProcessor<T> processor, final String activity) {

        new Commit(MyjaphooDB.singleInstance().getConnection()) {

            @Override
            protected void run(EntityManager em) {
                process(list, processor, activity);
            }
        };
    }

    private static <T> void process(List<T> list, ListProcessor<T> processor, String activity) {

        Channel channel = ChannelManager.createChannel(Processing.class, activity);
        channel.startActivity();
        processor.startProcess();
        channel.setProgressSteps(list.size());
        try {
            for (T t : list) {
                channel.nextProgress();
                channel.message(activity + ": " + processor.longName(t)); //NOI18N
                try {
                    processor.process(t);
                } catch (Exception ex) {
                    LoggerFactory.getLogger(Processing.class.getName()).error("exception processing list", ex); //NOI18N
                    channel.errormessage("error during execution!", ex); //NOI18N
                    throw new RuntimeException("exception processing list", ex); //NOI18N
                }
            }
        } finally {
            channel.stopActivity();
            processor.stopProcess();
        }
    }

    public static <T> ListProcessor<T> withDelay(ListProcessor<T> processor) {
        return new DelayedProcessWrapper<T>(processor);
    }
}
