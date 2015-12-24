package org.mlsoft.common.prefs.model;

import org.mlsoft.common.prefs.model.editors.*;


/**
 * Backingdelegator f�r Preferences
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */

public class PrefsBackingDelegator extends BackingDelegator
{
  PrefsRoot root;
  AbstractPrefVal  abstractPrefVal;

  public PrefsBackingDelegator(PrefsRoot root,AbstractPrefVal
                                                 abstractPrefVal,Object defaultVal)
  {
    super(defaultVal);
    this.root=root;
    this.abstractPrefVal=abstractPrefVal;
  }

  public void setObjVal(Object newVal)
  {
    super.setObjVal(newVal);
    setPrefsObjVal(newVal);
  }

  public Object getObjVal()
  {
    if (abstractPrefVal instanceof BooleanVal)
      return new Boolean(root.getPrefs().getBoolean(abstractPrefVal.getName(),((Boolean)getDefaultVal()).booleanValue()));
    else if (abstractPrefVal instanceof StringVal)
      return root.getPrefs().get(abstractPrefVal.getName(),(String)getDefaultVal());
    else if (abstractPrefVal instanceof IntegerVal)
      return new Integer(root.getPrefs().getInt(abstractPrefVal.getName(),((Integer)getDefaultVal()).intValue()));
    else
      throw new RuntimeException("nicht unterst�tzter Typ bei PrefsBackingDelegator");
  }


  public void commit()
  {
    super.commit();
  }

  public void rollback()
  {
    Object rollbackVal=getRollbackVal();
    if (rollbackVal!=null)
    setPrefsObjVal(rollbackVal);
    super.rollback();

  }



  private void setPrefsObjVal(Object newVal)
  {
    if (newVal instanceof Boolean)
      root.getPrefs().putBoolean(abstractPrefVal.getName(),((Boolean)newVal).booleanValue());
    else if (newVal instanceof String)
      root.getPrefs().put(abstractPrefVal.getName(),(String)newVal);
    else if (newVal instanceof Integer)
      root.getPrefs().putInt(abstractPrefVal.getName(),((Integer)newVal).intValue());
    else
      throw new RuntimeException("nicht unterst�tzter Typ bei PrefsBackingDelegator");

  }

}