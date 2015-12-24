package org.mlsoft.common.prefs.model.editors;

import org.mlsoft.common.prefs.model.*;
import org.mlsoft.common.prefs.model.edit.EditableItem;


/**
 * <p>�berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */

public class AbstractPrefItem extends AbstractMetadata implements EditableItem{

  private EditorRoot root;

  public AbstractPrefItem(AbstractMetadata parent,String name, String guiName, String description) {
    super(parent);
    parent.addChild(this);
    this.setName(name);
    this.setDisplayname(guiName);
    this.setDescrshort(description);
    root=(EditorRoot)findRoot();
  }

  public String getDisplayClassName()
  {
    return "AbstractPrefItem";
  }
  public String info()
  {
    return "";
  }



  protected EditorRoot getRoot()
  {
    return root;
  }

}