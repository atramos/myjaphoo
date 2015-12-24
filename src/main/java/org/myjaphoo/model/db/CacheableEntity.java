/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db;

/**
 * Interface, das alle entites erfüllen, die im CacheManager gecached werden.
 * @author mla
 */
public interface CacheableEntity extends Cloneable {

    public Long getId();

    /**
     * Erzeugt eine geclonde Kopie, bei der allerdings
     * sämtliche Referenzen auf andere Entities nur shallow kopiert werden.
     * Transiente Attribute (transiente Referenzen) werden NICHT kopiert.
     * Sie werden einfach auf leere Mengen gesetzt.
     * Dies ist also keine Vollständige Kopie. Die Referenzen müssen ggf.
     * anschliessend korrigiert/nachgezogen werden.
     * @return
     */
    Object partialClone();
}
