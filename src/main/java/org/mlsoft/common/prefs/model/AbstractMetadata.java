package org.mlsoft.common.prefs.model;


import org.mlsoft.structures.AbstractTreeNode;

/**
 *
 * Basisklasse zur Beschreibung von Metadaten.
 * Jedes Metadatum hat einen Namen und eine Beschreibung sowie einen Vater.
 * Metadaten sind hierarchisch und liegen deshalb in einer Baumstruktur vor.
 *
 * Metadaten sollten die Methoden create und scopePut unterst�tzen, um
 * das Metadatenformat zu unterst�tzen. Wenn dies der Fall ist, so
 * kann ein entsprechendes Metadatum auch �ber clone kopiert werden.
 *
 * @author $Author: kylie $
 * @version $Revision: 124 $ ($Date: 2005-03-19 17:31:15 +0100 (Sa, 19 Mrz 2005) $)
 */
public abstract class AbstractMetadata extends AbstractTreeNode implements Comparable
{
  /** Name des Metadatums */
  private String name = null;
  /**
   * der Anzeigename f�r dieses Objekt (f�r den Benutzer)
   */
  private String displayname = null;
  /** kurze Beschreibung des Metadatums */
  private String descrshort = null;
  /** lange Beschreibung des Metadatums */
  private String descrlong = null;


  /**
   * Erzeugt eine Instanz von <code>AbstractMetadata</code>
   *
   * @param parent der Vater dieses Metadatums
   */
  public AbstractMetadata(AbstractMetadata parent)
  {
    super(parent);
  }


  /**
   * setzt den Namen dieses Metadatums
   *
   * @param n der Name
   */
  public void setName(String n)
  {
    name = n;
  }

  /**
   * liefert den Namen des Metadatums.
   *
   * @return der Name des Metadatums
   */
  public String getName()
  {
    return ((name == null)
            ? ""
            : name);
  }
  /**
   * liefert den Anzeigenamen (ohne Trennhinweise)
   *
   * @return der Anzeigename
   */
  final public String getDisplayname()
  {
    if(displayname == null)
      return "";
    else
      return displayname;
  }

  /**
   * setzt den Anzeigenamen. Dieser darf auch Trennhinweise beinhalten.
   * @param dispname der Anzeigename
   */
  final public void setDisplayname(String dispname)
  {
    this.displayname=dispname;
  }


  /**
   * setzt eine kurze Beschreibung des Metadatums
   *
   * @param n die Beschreibung
   */
  final public void setDescrshort(String n)
  {
    descrshort = n;
  }

  /**
   * liefert die kurze Beschreibung
   *
   * @return die Beschreibung
   */
  final public String getDescrshort()
  {
    return ((descrshort == null)
            ? ""
            : descrshort);
  }

  /**
   * setzt die lange Beschreibung des Metadatums
   *
   * @param n lange Beschreibung
   */
  public void setDescrlong(String n)
  {
    descrlong = n;
  }

  /**
   * liefert die lange Beschreibung
   *
   * @return die Beschreibung
   */
  public String getDescrlong()
  {
    return ((descrlong == null)
            ? ""
            : descrlong);
  }

  /**
   * liefert einen benutzerverst�ndlichen Namen der Klasse. Dieser wird z.B. beim Parsen verwendet
   * und auch in der Metadatenrepository-ansicht.
   * @return Klassenname f�r den Anwender
   */
  abstract public String getDisplayClassName();


  /**
   * hier wird der Name des Metadatums geliefert.
   *
   * @return der Name des Metadatums
   */
  public String toString()
  {
    return getDisplayname();
  }



  /**
   * Methode von Interface Comparable. Ben�tigen wir, um Metadaten in Sets zu verwenden.
   * @param o objekt zum vergleichen
   * @return {@inheritDoc}
   */
  public int compareTo(Object o)
  {
    return toString().compareTo(o.toString());
  }
}

