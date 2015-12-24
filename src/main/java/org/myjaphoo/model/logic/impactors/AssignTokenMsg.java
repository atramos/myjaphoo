/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.logic.impactors;

import java.util.List;

/**
 *
 * @author mla
 */
class AssignTokenMsg {

    private long movieId;

    private List<String> tokenNames;

    public AssignTokenMsg(long movieId, List<String> tokenNames) {
        this.movieId = movieId;
        this.tokenNames = tokenNames;
    }

    /**
     * @return the movieId
     */
    public long getMovieId() {
        return movieId;
    }

    /**
     * @return the tokenNames
     */
    public List<String> getTokenNames() {
        return tokenNames;
    }


}
