package org.mlsoft.common.prefs.model.edit;



/**
 * Vom Benutzer in irgendeiner Form �nderbares objekt (k�nnen irgendwelche
 * Einstellungen, Preferences Parameter, Dialogeingaben, etc sein)
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version 1.0
 */

public interface EditableItem
{

    /**
     * liefert den Namen des Metadatums.
     *
     * @return der Name des Metadatums
     */
    public String getName();

    /**
     * liefert den Anzeigenamen
     *
     * @return der Anzeigename
     */
    public String getDisplayname();


    /**
     * liefert die kurze Beschreibung
     *
     * @return die Beschreibung
     */
    public String getDescrshort();

    /**
     * liefert die lange Beschreibung
     *
     * @return die Beschreibung
     */
    public String getDescrlong();


    /**
     * liefert einen benutzerverst�ndlichen Namen der Klasse. Dieser wird z.B. beim Parsen verwendet
     * und auch in der Metadatenrepository-ansicht.
     * @return benutzerverst�ndlicher Klassenname
     */
    public String getDisplayClassName();


    /**
     * hier wird der Name des Metadatums geliefert.
     *
     * @return der Name des Metadatums
     */
    public String toString();


    /**
     * Methode von Interface Comparable. Ben�tigen wir, um Metadaten in Sets zu verwenden. Ein Metadatum
     * wird i.d.R. anhand der Namensbezeichnung verglichen.
     * @param o objekt zum vergleichen
     * @return {@inheritDoc}
     */
    public int compareTo(Object o);
}