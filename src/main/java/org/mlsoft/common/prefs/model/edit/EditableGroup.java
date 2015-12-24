package org.mlsoft.common.prefs.model.edit;

import java.util.Iterator;

/**
 * <p>�berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */

public interface EditableGroup extends EditableItem
{


  public Iterator childIterator();
}