/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.processing;


/**
 * Processor of lists of t;
 * @author mla
 */
   public interface ListProcessor<T> {

       public void startProcess();

        public void process(T t) throws Exception;


        public void stopProcess();


        public String shortName(T t);

        public String longName(T t);
    }
