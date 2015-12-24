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

public class BooleanVal extends AbstractPrefVal implements EditableBooleanVal{

  public BooleanVal(AbstractMetadata parent,String name, String guiName, String description, Boolean defaultVal) {
    super(parent,name, guiName, description,defaultVal);
  }


  public void setVal(boolean val)
  {
    setObjVal(new Boolean(val));
  }

  public boolean getVal()
  {
    return ((Boolean)getObjVal()).booleanValue();
  }


}