/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.myjaphoo.FileCopying;
import org.myjaphoo.MyjaphooController;

/**
 * Copies one or more files to a destination dir.
 * @author lang
 */
public class CopyFilesAction extends AbstractCopyFilesAction {

    public CopyFilesAction(MyjaphooController controller, ViewContext context) {
        super(controller, context, true, FileCopying.PathOptionForCopying.NOADDITIONAL_PATH);
    }
}
