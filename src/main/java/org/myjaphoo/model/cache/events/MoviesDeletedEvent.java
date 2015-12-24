/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.db.MovieEntry;

/**
 * Event when movies get deleted.
 * @author lang
 */
public class MoviesDeletedEvent extends AbstractMoviesChangedEvent {

    public MoviesDeletedEvent(MovieEntry... entries) {
        super(entries);
    }

    public MoviesDeletedEvent(Collection<MovieEntry> entries) {
        super(entries);
    }
}
