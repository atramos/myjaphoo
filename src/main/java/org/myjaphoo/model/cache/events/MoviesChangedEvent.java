/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.MovieEntry;

/**
 * Event when movies get changed.
 * @author lang
 */
public class MoviesChangedEvent extends AbstractMoviesChangedEvent {

    public MoviesChangedEvent(MovieEntry... entries) {
        super(entries);
    }

    public MoviesChangedEvent(Collection<MovieEntry> entries) {
        super(entries);
    }
}
