package org.myjaphoo.model.db.upgrades;

import org.myjaphoo.model.db.Token;

import javax.persistence.EntityManager;

/**
 * hotfix for 3.5: a root tag has never been created.
 */
public class V102Upgrader implements Upgrader {
    @Override
    public void upgrade(EntityManager em) {
        int count = ((Long) em.createQuery("select count(o) from Token as o").getSingleResult()).intValue();
        if (count == 0) {
            Token roottoken = new Token();
            roottoken.setName("InternRootToken");
            em.persist(roottoken);
        }
    }
}
