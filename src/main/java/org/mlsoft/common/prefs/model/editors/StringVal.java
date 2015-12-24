package org.mlsoft.common.prefs.model.editors;

import org.mlsoft.common.prefs.model.*;
import org.mlsoft.common.prefs.model.edit.*;

/**
 * <p>StringVal </p>
 * <p>Simpler String Preferences Parameter </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */

public class StringVal extends AbstractPrefVal implements EditableStringVal{

  public StringVal(AbstractMetadata parent,String name, String guiName, String description, String defaultVal) {
    super(parent,name, guiName, description,defaultVal);
  }


  public String getVal()
  {
    return (String) getObjVal();
  }


  public void setVal(String val)
  {
    setObjVal(val);
  }
}