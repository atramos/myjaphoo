/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db.upgrades;

import javax.persistence.EntityManager;

/**
 * Job that does upgrading. 
 * It exissts one implementation for each upgrade.
 * @author lang
 */
public interface Upgrader {

    public void upgrade(EntityManager em);
    
}
