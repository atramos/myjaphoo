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

public class DirectoryVal extends StringVal implements EditableDirectoryVal{

  public DirectoryVal(AbstractMetadata parent, String name, String guiName, String description, String defaultVal) {
    super(parent, name, guiName, description, defaultVal);
  }
}