/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.processing;

import org.myjaphoo.model.db.MovieEntry;

/**
 *
 * @author mla
 */
   public interface EntryListProcessor extends ListProcessor<MovieEntry> {

       public void startProcess();

        public void process(MovieEntry entry) throws Exception;


        public void stopProcess();
    }
