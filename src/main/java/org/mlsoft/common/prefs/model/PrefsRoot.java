package org.mlsoft.common.prefs.model;

import java.util.prefs.Preferences;

import org.mlsoft.common.prefs.model.editors.*;

/**
 * <p>�berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */

public class PrefsRoot extends EditorRoot
{
  Preferences prefs;

  public PrefsRoot(Preferences prefs)
  {
    this.prefs=prefs;
  }

  public Preferences getPrefs()
  {
    return prefs;
  }

  public String getDisplayClassName()
  {
    return "PrefsRoot";
  }

  public String info()
  {
    return "";
  }

  public BackingDelegator createBackingDelegator(AbstractPrefVal
                                                 abstractPrefVal,
                                                 Object defaultVal)
  {
    return new PrefsBackingDelegator(this,abstractPrefVal,defaultVal);
  }



}