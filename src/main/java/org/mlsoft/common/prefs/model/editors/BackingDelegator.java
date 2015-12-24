package org.mlsoft.common.prefs.model.editors;

/**
 * �bernimmt das eigentliche Speichern der Editorwerte
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */

public abstract class BackingDelegator
{
  private Object defaultVal;
  private Object val;
  private Object rollbackVal;

  public BackingDelegator(Object defaultVal)
  {
    this.defaultVal=defaultVal;
    this.rollbackVal=null;
  }

  public Object getDefaultVal()
  {
    return defaultVal;
  }

  public void setObjVal(Object newVal)
  {
    if (rollbackVal==null)
      rollbackVal=newVal;
    if (rollbackVal==null)
      rollbackVal=defaultVal;
    val=newVal;
  }

  public abstract Object getObjVal();


  public void commit()
  {
    rollbackVal=null;
  }

  public void rollback()
  {
    if (rollbackVal!=null)
    {
      val=rollbackVal;
      rollbackVal=null;
    }
  }

  protected Object getRollbackVal()
  {
    return rollbackVal;
  }



}