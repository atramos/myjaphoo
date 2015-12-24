/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.processing;

import org.myjaphoo.model.db.*;

/**
 * Result of filter processing. Contains the combination of
 * entry, token and metatoken for that a filter was successfull.
 * 
 * If the advanced database comparison feature is activated,
 * then this result contains a difference-combination between this and
 * the other database. In that case this class can be interpreted as
 * a single instruction to merge the difference.
 * 
 * database.
 * @author lang
 */
public interface CombinationResultInterface {

    /**
     * @return the entry
     */
    public MovieEntry getEntry();

    /**
     * @return the token
     */
    public Token getToken();

    /**
     * @return the metaToken
     */
    public MetaToken getMetaToken();

    /**
     * @return the entry
     */
    public MovieEntry getCDBEntry();

    /**
     * @return the token
     */
    public Token getCDBToken();

    /**
     * @return the metaToken
     */
    public MetaToken getCDBMetaToken();
}
