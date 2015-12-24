/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Chroniken eintrag.
 * @author mla
 */
@Entity
public class ChronicEntry implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="table-hilo-generator")
    private Long id;
    
    @Embedded
    private DataView view;

    public ChronicEntry() {
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        ChronicEntry cloned = (ChronicEntry) super.clone();
        if (getView() != null) {
            cloned.setView((DataView) getView().clone());
        }
        return cloned;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ChronicEntry)) {
            return false;
        }
        ChronicEntry other = (ChronicEntry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public boolean isContentequals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        ChronicEntry other = (ChronicEntry) obj;
        return getView().isContentequals(other.getView());
    }

  

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the view
     */
    public DataView getView() {
        if (view == null) {
            setView(new DataView());
        }
        return view;
    }

    /**
     * @param view the view to set
     */
    public void setView(DataView view) {
        this.view = view;
    }

}
