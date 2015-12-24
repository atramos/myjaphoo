/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db;

import org.myjaphoo.model.db.upgrades.DBUpgrade;

import javax.persistence.*;
import java.util.Date;

/**
 * Saves the database upgrade history in the database.
 * The upgrade algorithm determines with this information if and what
 * upgrades are necessary.
 * @author lang
 */
@Entity
public class DatabaseVersion {
    
        private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="table-hilo-generator")
    private Long id;
    
    @Column(unique=true, nullable=false) 
    @Enumerated(EnumType.STRING)
    private DBUpgrade dbversion;
    
    /** the date when the upgrade was executed. */
    @Temporal(TemporalType.TIMESTAMP)
    private Date upgradedAt;

    /**
     * @return the dbversion
     */
    public DBUpgrade getDbversion() {
        return dbversion;
    }

    /**
     * @param dbversion the dbversion to set
     */
    public void setDbversion(DBUpgrade dbversion) {
        this.dbversion = dbversion;
    }

    /**
     * @return the upgradedAt
     */
    public Date getUpgradedAt() {
        return upgradedAt;
    }

    /**
     * @param upgradedAt the upgradedAt to set
     */
    public void setUpgradedAt(Date upgradedAt) {
        this.upgradedAt = upgradedAt;
    }
    
    
}
