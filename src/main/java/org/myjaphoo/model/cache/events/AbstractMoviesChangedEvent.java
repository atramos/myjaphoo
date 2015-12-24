/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.cache.events;

import java.util.Collection;
import org.myjaphoo.model.cache.ChangeSet;
import org.myjaphoo.model.db.MovieEntry;

/**
 * base event class for changes on movies.
 * @author lang
 */
public class AbstractMoviesChangedEvent extends ChangeSet {

    public AbstractMoviesChangedEvent(MovieEntry... entries) {
        add(entries);
    }

    public AbstractMoviesChangedEvent(Collection<MovieEntry> entries) {
        addList(entries);
    }
}
