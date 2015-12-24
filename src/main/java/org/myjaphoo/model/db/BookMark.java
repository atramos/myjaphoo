/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author lang
 */
@Entity
public class BookMark implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "table-hilo-generator")
    private Long id;

    @Column(unique = true)
    private String name;
    @Column(length = 2000)
    private String descr;

    /**
     * path to show up in the menu in the ui (if set).
     */
    private String menuPath;

    @Embedded
    private DataView view;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the descr
     */
    public String getDescr() {
        return descr;
    }

    /**
     * @param descr the descr to set
     */
    public void setDescr(String descr) {
        this.descr = descr;
    }

    public ChronicEntry toChronic() {
        ChronicEntry c = new ChronicEntry();
        c.setView((DataView) getView().clone());
        //copyAttrs(c);
        return c;
    }

    /**
     * @return the view
     */
    public DataView getView() {
        if (view == null) {
            view = new DataView();
        }
        return view;
    }

    /**
     * @param view the view to set
     */
    public void setView(DataView view) {
        this.view = view;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }
}
