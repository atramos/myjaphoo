/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 * saved script
 *
 * @author lang
 */
@Entity
public class SavedGroovyScript implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "table-hilo-generator")
    private Long id;

    @Column(unique = true)
    private String name;

    private String descr;

    @Lob
    @Column(length=1000000000)
    private String scriptText;

    private String menuPath;

    @Enumerated(EnumType.STRING)
    private ScriptType scriptType = ScriptType.SCRIPT;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScriptText() {
        return scriptText;
    }

    public void setScriptText(String scriptText) {
        this.scriptText = scriptText;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SavedGroovyScript script = (SavedGroovyScript) o;

        if (id != null ? !id.equals(script.id) : script.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public ScriptType getScriptType() {
        return scriptType;
    }

    public void setScriptType(ScriptType scriptType) {
        this.scriptType = scriptType;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }
}
