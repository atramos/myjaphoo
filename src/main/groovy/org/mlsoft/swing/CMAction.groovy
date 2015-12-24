package org.mlsoft.swing

import org.mlsoft.swing.annotation.ContextMenuAction

/**
 * CMAction 
 * @author mla
 * @version $Id$
 *
 */
class CMAction extends ManagedAction {
    def ContextMenuAction actionAnnotation;

    CMAction(ConfigHandler configHandler, ContextMenuAction actionAnnotation) {
        super(configHandler, actionAnnotation.name(), actionAnnotation.enableExpr());
        this.actionAnnotation = actionAnnotation
    }
}
