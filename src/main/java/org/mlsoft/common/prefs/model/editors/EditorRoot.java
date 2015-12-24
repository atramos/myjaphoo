package org.mlsoft.common.prefs.model.editors;


import org.mlsoft.common.prefs.model.AbstractMetadata;
import org.mlsoft.common.prefs.model.edit.EditableGroup;
import org.mlsoft.structures.AbstractTreeNode;

import java.util.Iterator;

/**
 * <p>�berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */
public abstract class EditorRoot extends AbstractMetadata implements EditableGroup {

    public EditorRoot() {
        super(null);
    }

    public String getDisplayClassName() {
        return "EditorRoot";
    }

    public String info() {
        return "";
    }

    /**
     * commited alle Editierungen
     */
    public void commit() {
        commit(this);
    }

    private void commit(AbstractTreeNode node) {
        Iterator iter = node.getChildren().iterator();
        while (iter.hasNext()) {
            AbstractTreeNode item = (AbstractTreeNode) iter.next();
            if (item instanceof AbstractPrefVal) {
                ((AbstractPrefVal) item).commit();
            }
            commit(item);
        }
    }

    private void rollback(AbstractTreeNode node) {
        Iterator iter = node.getChildren().iterator();
        while (iter.hasNext()) {
            AbstractTreeNode item = (AbstractTreeNode) iter.next();
            if (item instanceof AbstractPrefVal) {
                ((AbstractPrefVal) item).rollback();
            }
            rollback(item);
        }
    }

    /**
     * f�hrt einen rollback aus
     */
    public void rollback() {
        rollback(this);
    }

    /**
     * erzeuge einen BackingDelegator, der f�r alle Speichervorg�nge f�r die
     * Editoren dieser Wurzel verwendet werden sollen
     * @param abstractPrefVal der Editor, f�r den der Delegator erzeugt werden soll
     * @param defaultVal der defaultValue des Editors
     * @return
     */
    public abstract BackingDelegator createBackingDelegator(AbstractPrefVal abstractPrefVal,
            Object defaultVal);

    public Iterator childIterator() {
        return getChildren().iterator();
    }
}
