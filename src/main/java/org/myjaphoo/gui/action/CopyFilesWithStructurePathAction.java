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
public class CopyFilesWithStructurePathAction extends AbstractCopyFilesAction {

    public CopyFilesWithStructurePathAction(MyjaphooController controller, ViewContext context) {
        super(controller, context, true, FileCopying.PathOptionForCopying.USE_STRUCTURE_PATH);
    }
}
