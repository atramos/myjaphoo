package org.mlsoft.common.prefs.model.edit;

/**
 * <p>�berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */

public interface EditableStringSelectionVal extends EditableStringVal
{
  public String[] getAuswahl();


  public int getIndex();

  public void setIndex(int index);


}