package org.mlsoft.common.prefs.model.editors;

import org.mlsoft.common.prefs.model.*;
import org.mlsoft.common.prefs.model.edit.*;

/**
 * <p>�berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */

public class StringSelectionVal extends StringVal implements EditableStringSelectionVal{

  private String[] auswahl;

  public StringSelectionVal(AbstractMetadata parent, String name, String guiName, String description, String defaultVal,String[] auswahl) {
    super(parent, name, guiName, description, defaultVal);
    this.auswahl=auswahl;
  }

  public String[] getAuswahl()
  {
    return auswahl;
  }


  public int getIndex()
  {
    for (int i=0;i<auswahl.length; i++) {
      if (auswahl[i].equals(super.getVal()))
        return i;
    }
    return -1;
  }

  public void setIndex(int index)
  {
    super.setVal(auswahl[index]);
  }

}
