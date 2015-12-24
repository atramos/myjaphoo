/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.db;

import javax.persistence.*;

/**
 *
 * @author lang
 */
@Entity
@Table(name = "Preferences")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Prefs {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
