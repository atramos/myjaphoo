package org.mlsoft.swing

import org.mlsoft.swing.annotation.ToolbarAction

/**
 * TBAction 
 * @author mla
 * @version $Id$
 *
 */
class TBAction extends ManagedAction {
    def ToolbarAction actionAnnotation;

    TBAction(ConfigHandler configHandler, ToolbarAction actionAnnotation) {
        super(configHandler, actionAnnotation.name(), actionAnnotation.enableExpr());
        this.actionAnnotation = actionAnnotation;
    }

}
