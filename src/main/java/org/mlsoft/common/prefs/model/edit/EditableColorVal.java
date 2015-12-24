/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.common.prefs.model.edit;

import java.awt.Color;

/**
 * Color value.
 * @author mla
 */
public interface EditableColorVal extends EditableVal {

    public Color getVal();

    public void setVal(Color val);
}