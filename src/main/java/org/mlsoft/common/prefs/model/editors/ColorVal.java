package org.mlsoft.common.prefs.model.editors;

import java.awt.Color;
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
public class ColorVal extends AbstractPrefVal implements EditableColorVal {

    public ColorVal(AbstractMetadata parent, String name, String guiName, String description, Color defaultVal) {
        super(parent, name, guiName, description, defaultVal);
    }

    @Override
    public Color getVal() {
        return (Color) getObjVal();
    }

    @Override
    public void setVal(Color val) {
        setObjVal(val);
    }
}