/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.filterparser.processing;

import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.dbcompare.CombinationSet;

import java.util.Collection;

/**
 * Factory for creating combinations.
 * Hides the concrete implementation of combation creation algorithm.
 * @author lang
 */
class CombinationGeneratorFactory {
    
    public static CombinationSet createCombinations(final Collection<MovieEntry> allMovies, final ProcessingRequirementInformation pri1, final ProcessingRequirementInformation pri2) {
        // for now, use the silly method
        return new CombinationResultGenerator().getSillyIterator(allMovies, pri1, pri2);
    }
}
