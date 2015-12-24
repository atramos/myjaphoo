package org.mlsoft.common.prefs.model.editors;

import java.util.Iterator;

import org.mlsoft.common.prefs.model.*;
import org.mlsoft.common.prefs.model.edit.*;

/**
 * <p>PreferencesGroup </p>
 * <p>Gruppiert logisch zusammengeh�rige PreferencesValues, um diese in der Gui
 * auch zusammengeh�rend darzustellen
 * </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */

public class EditorGroup extends AbstractPrefItem implements EditableGroup{

  public EditorGroup(AbstractMetadata parent,String name, String guiName, String description) {
    super(parent,name,guiName,description);
  }

  public Iterator childIterator()
  {
    return super.getChildren().iterator();

  }



}