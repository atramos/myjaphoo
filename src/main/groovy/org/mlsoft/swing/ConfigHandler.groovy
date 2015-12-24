package org.mlsoft.swing

import org.mlsoft.swing.annotation.ContextMenuAction
import org.mlsoft.swing.annotation.ToolbarAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.swing.*
import java.awt.event.ActionEvent
import java.lang.reflect.Method

/**
 * Contains methods to read and interpret annotations for jxtree and jxtable configuration.
 */
public class ConfigHandler {
    private static final Logger logger = LoggerFactory.getLogger(ConfigHandler.class);

    def ResourceBundle localeBundle;

    def Object configByConventionHandler;

    ArrayList<TBAction> toolbarActions = new ArrayList<>();

    ArrayList<CMAction> popupActions = new ArrayList<>();

    private ComponentSupporter componentSupporter;

    private static final Comparator ACTIONS_COMP = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            return o1.actionAnnotation.order() - o2.actionAnnotation.order();
        }
    };

    public ConfigHandler(ComponentSupporter componentSupporter) {
       this.componentSupporter = componentSupporter;
    }

    /**
     * sets the configuration object. Methods defined on that object with special reserved names and signature get
     * used for callback handlers for this tree. This is the "configuration by convention" part of this implementation.
     * A implementor needs not to define all of them if he does not
     * want to react on all events. The following methods get used:
     * <p/>
     * - JPopupMenu getPopupFor(T selectedElement) : creates a popup if a popup trigger has been recognized on a given item.
     * - void onDoubleClickAction(T selElement): action handler when a double click happens on a given item.
     * - void onElementSelected(T selElement): action handler when a element got selected in the tree.
     *
     * @param configByConventionHandler
     * @param localeBundle
     */
    public void setConfiguration(Object configByConventionHandler, ResourceBundle localeBundle) {
        this.configByConventionHandler = configByConventionHandler;
        this.localeBundle = localeBundle;
        scanAnnotations(configByConventionHandler);
    }

    boolean hasPopupAnnotationsDefined() {
        return popupActions.size() > 0;
    }

    void updateActionsEnabledState(Object t) {
        toolbarActions.each {
            TBAction action ->
                action.abstractAction.setEnabled(action.isEnabled(t));
        }
    }

    private void scanAnnotations(Object configByConventionHandler) {
        if (configByConventionHandler == null) {
            return;
        }
        ArrayList<TBAction> actions = new ArrayList<TBAction>();

        ArrayList<TBAction> popupActions = new ArrayList<TBAction>();
        for (final Method method : configByConventionHandler.getClass().getMethods()) {
            final ToolbarAction actionAnnotation = method.getAnnotation(ToolbarAction.class);
            int len = method.getParameterTypes().length;

            if (actionAnnotation != null) {
                TBAction action = new TBAction(this, actionAnnotation);
                action.method = method;
                if (len ==1) {
                    def Class firstParam =  method.getParameterTypes()[0];
                    action.hasListArgument = (firstParam.name == "java.util.List");
                }
                actions.add(action);
            }

            final ContextMenuAction contextMenuActionAnnotation = method.getAnnotation(ContextMenuAction.class);
            if (contextMenuActionAnnotation != null) {
                CMAction action = new CMAction(this, contextMenuActionAnnotation);
                action.method = method;
                if (len ==1) {
                    def Class firstParam =  method.getParameterTypes()[0];
                    action.hasListArgument = (firstParam.name == "java.util.List");
                }
                popupActions.add(action);
            }
        }
        // order the list:
        Collections.sort(actions, ACTIONS_COMP);

        Collections.sort(popupActions, ACTIONS_COMP);
        toolbarActions = actions;
        this.popupActions = popupActions;
    }


    public void updateToolbarActions(JToolBar toolBar) {
        toolBar.removeAll();
        for (final TBAction action : toolbarActions) {
            final TBAction theAction = action;
            theAction.abstractAction = new AbstractAction(theAction.getActionName()) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // get the selected object:
                    if (theAction.actionAnnotation.contextRelevant()) {
                        if (theAction.hasListArgument) {
                            List t = componentSupporter.getSelectedElements();
                            if (t != null) {
                                configMethodCall(theAction.method.getName(), t);
                            }
                        } else {
                            Object t = componentSupporter.getFirstSelectedElement();
                            if (t != null) {
                                configMethodCall(theAction.method.getName(), t);
                            }
                        }
                    } else {
                        configMethodCall(theAction.method.getName());
                    }
                }
            };
            toolBar.add(theAction.abstractAction);
        }
    }

    private JPopupMenu createPopupFromAnnotations() {
        JPopupMenu m = new JPopupMenu("PopupMenu"); //NOI18N
        for (final CMAction cmAction : popupActions) {
            final CMAction theAction = cmAction; // workaround for groovy final problems

            if (theAction.isEnabled(theAction.hasListArgument? componentSupporter.selectedElements : componentSupporter.getFirstSelectedElement())) {
                m.add(new AbstractAction(theAction.getActionName()) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // get the selected object:
                        if (theAction.actionAnnotation.contextRelevant()) {
                            if (theAction.hasListArgument) {
                                List t = componentSupporter.getSelectedElements();
                                if (t != null) {
                                    configMethodCall(theAction.method.getName(), t);
                                }
                            } else {
                                Object t = componentSupporter.getFirstSelectedElement();
                                if (t != null) {
                                    configMethodCall(theAction.method.getName(), t);
                                }
                            }
                        } else {
                            configMethodCall(theAction.method.getName());
                        }
                    }
                });
            }
        }

        return m;
    }

    public JPopupMenu createPopup() {
        JPopupMenu popup = null;
        if (hasPopupAnnotationsDefined()) {
            popup = createPopupFromAnnotations();
        } else {
            popup = (JPopupMenu) configMethodCall("getPopupFor", componentSupporter.getFirstSelectedElement());
        }
        return popup;
    }

    public Object configMethodCall(String methName, Object arg) {
        try {
            return configByConventionHandler."$methName"(arg);
        } catch (groovy.lang.MissingMethodException glm) {
            logger.warn("config by convention: " + methName + " not existing or failed: " + glm.getMessage());
            return null;
        } catch (Exception ex) {
            logger.warn("config by convention: " + methName + " executing failed!", ex);
            return null;
        }
    }

    public Object configMethodCall(String methName) {
        try {
            return configByConventionHandler."$methName"();
        } catch (groovy.lang.MissingMethodException glm) {
            logger.warn("config by convention: " + methName + " not existing or failed", glm);
            return null;
        } catch (Exception ex) {
            logger.warn("config by convention: " + methName + " executing failed!", ex);
            return null;
        }
    }
}
