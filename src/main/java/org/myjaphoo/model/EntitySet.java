/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.myjaphoo.model.db.CacheableEntity;

/**
 * Nimmt eine Liste von Entities auf. Die Entities können
 * über ihre ID gefunden werden (die equals methode muss korrekt überladen sein).
 * @author mla
 */
public class EntitySet<E extends CacheableEntity> implements Cloneable {

    private Map<Long, E> set = new HashMap<Long, E>();

    public EntitySet() {
    }

    public EntitySet(Collection<E> list) {
        for (E e : list) {
            this.set.put(e.getId(), e);
        }
    }

    public void remove(E e) {
        this.set.remove(e.getId());
    }

    public E find(E e) {
        if (e == null) {
            return null;
        }
        return set.get(e.getId());
    }

    public List<E> asList() {
        return new ArrayList<E>(set.values());
    }

    /**
     * makes a shallow copy of this object.
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Erzeugt eine Kopie. Die entities selbst werden auch gecloned.
     * Die Referenzen der Entities werden aber nicht gekloned. (shallow copcy),
     * da diese sowiso anschliessend der gesamte Objekt-Graph auf die kopien
     * verlinkt werden muss.
     */
    public EntitySet partialCopyClone() throws CloneNotSupportedException {
        EntitySet<E> copy = (EntitySet<E>) clone();
        // clone all the entities:
        copy.set = new HashMap<Long, E>(set.size());
        for (E e : set.values()) {
            E eclone = (E) e.partialClone();
            copy.set.put(eclone.getId(), eclone);
        }
        return copy;
    }

    public void add(E e) {
        set.put(e.getId(), e);
    }

    public void addAll(E... es) {
        for (E e : es) {
            set.put(e.getId(), e);
        }
    }

    public int size() {
        return set.size();
    }

    /**
     * Returns a list with the references of this entity set, based on a list of given "old" references.
     * This is often needed, e.g. entity change events contain only the old
     * references (the state before the change), but not the new status after the 
     * change.
     * @param newOnes
     * @param oldOnes
     * @return 
     */
    public List<E> getReferences(List<E> oldOnes) {
        List<E> result = new ArrayList<E>();
        for (E t : oldOnes) {
            E newOne = find(t);
            if (newOne != null) {
                result.add(newOne);
            }
        }
        return result;
    }
}
