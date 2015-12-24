/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.MovieEntry;

/**
 * Event when movies get added.
 * @author lang
 */
public class MoviesAddedEvent extends AbstractMoviesChangedEvent {

    public MoviesAddedEvent(MovieEntry... entries) {
        super(entries);
    }

    public MoviesAddedEvent(Collection<MovieEntry> entries) {
        super(entries);
    }
}
