/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db.upgrades;

/**
 *
 * Lists all database upgrades.
 *
 * They are ordered from newest to the oldest, as they are self referencing
 * the next update. This way the upgrade algorithm can just
 * move alongside the upgrades to perform all upgrades to the latest one.
 *
 * @author lang
 */
public enum DBUpgrade {

    V_1_0_2(null, new V102Upgrader()),

    /** introduction of history entries with selected tab. */
    V_1_0_1(V_1_0_2, new V101Upgrader()),

    /** the first version where we introduced this db upgrade mechanism. i.e. the bootstrap marker.*/
    V_1_0_0(V_1_0_1, null);


    private DBUpgrade nextOne;
    private Upgrader upgrader;

    private DBUpgrade(DBUpgrade nextOne, Upgrader upgrader) {
        this.nextOne = nextOne;
        this.upgrader = upgrader;
    }

    /**
     * @return the nextOne
     */
    public DBUpgrade getNextOne() {
        return nextOne;
    }

    /**
     * @return the upgrader
     */
    public Upgrader getUpgrader() {
        return upgrader;
    }
}
