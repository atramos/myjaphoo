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

public class IntegerVal extends AbstractPrefVal implements EditableIntegerVal
{

  public IntegerVal(AbstractMetadata parent, String name, String guiName, String description, int defaultVal)
  {
    super(parent, name, guiName, description, new Integer(defaultVal));
  }

  public int getVal()
  {
    return ((Integer) getObjVal()).intValue();
  }


  public void setVal(int val)
  {
    setObjVal(new Integer(val));
  }
}