/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db.upgrades;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Upgrader from 1.0.0 to 1.0.1.
 * Fills new fields thumbmode with default values.
 * @author lang
 */
class V101Upgrader implements Upgrader {

    public V101Upgrader() {
    }

    @Override
    public void upgrade(EntityManager em) {
        Query q = em.createQuery("update BookMark b set b.view.thumbmode = org.myjaphoo.model.ThumbMode.ALTTHUMB where b.view.thumbmode is null");
        q.executeUpdate();
        q = em.createQuery("update ChronicEntry b set b.view.thumbmode = org.myjaphoo.model.ThumbMode.ALTTHUMB where b.view.thumbmode is null");
        q.executeUpdate();
    }
}
