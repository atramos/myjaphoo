/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.db;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author mla
 */
@Entity
public class ChangeLog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="table-hilo-generator")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Enumerated(EnumType.STRING)
    private ChangeLogType cltype;

    private String msg;

    @Column(length=4048)
    private String objDescription;

    /**
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return the cltype
     */
    public ChangeLogType getCltype() {
        return cltype;
    }

    /**
     * @param cltype the cltype to set
     */
    public void setCltype(ChangeLogType cltype) {
        this.cltype = cltype;
    }

    /**
     * @return the objDescription
     */
    public String getObjDescription() {
        return objDescription;
    }

    /**
     * @param objDescription the objDescription to set
     */
    public void setObjDescription(String objDescription) {
        this.objDescription = objDescription;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
