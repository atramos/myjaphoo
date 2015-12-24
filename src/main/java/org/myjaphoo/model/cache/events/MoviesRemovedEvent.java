/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.MovieEntry;

/**
 * Event when movies get removed
 * @author lang
 */
public class MoviesRemovedEvent extends AbstractMoviesChangedEvent {

    public MoviesRemovedEvent(MovieEntry... entries) {
        super(entries);
    }

    public MoviesRemovedEvent(Collection<MovieEntry> entries) {
        super(entries);
    }
}
