/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.action;

import org.jdesktop.application.Task;
import org.mlsoft.common.acitivity.Channel;
import org.mlsoft.common.acitivity.ChannelManager;
import org.myjaphoo.MyjaphooApp;
import org.myjaphoo.MyjaphooBackgroundJob;
import org.myjaphoo.MyjaphooController;
import org.myjaphoo.gui.util.FileChoosing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author lang
 */
public abstract class AbstractWankmanAction extends AbstractAction {

    private static final Logger logger = LoggerFactory.getLogger(AbstractWankmanAction.class.getName());
    private MyjaphooController controller;
    private ViewContext context = ViewContext.EMPTYCONTEXT;

    public AbstractWankmanAction(MyjaphooController controller, String name, Icon icon, ViewContext context) {
        super(name, icon);
        this.controller = controller;
        this.context = context;
    }

    public AbstractWankmanAction(MyjaphooController controller, String name, Icon icon) {
        super(name, icon);
        this.controller = controller;
    }

    class ActionBackgroundJob extends MyjaphooBackgroundJob {

        private MyjaphooController controller;
        private ActionEvent e;

        public ActionBackgroundJob(MyjaphooController controller, ActionEvent e) {
            super(controller.getView(), controller);
            this.controller = controller;
            this.e = e;
            this.setInputBlocker(new Task.InputBlocker(this, Task.BlockingScope.APPLICATION, null) {

                @Override
                protected void block() {
                }

                @Override
                protected void unblock() {
                }
            });
        }

        @Override
        protected Object doInBackground() throws Exception {
            String actionName = getValue(Action.NAME).toString();
            Channel channel = ChannelManager.createChannel(AbstractWankmanAction.this.getClass(), actionName);
            channel.startActivity();
            try {
                AbstractWankmanAction.this.run(controller, e, context);
            } catch (Throwable t) {
                channel.errormessage("exception during action " + actionName, t);
                logger.error("exception during action " + actionName, t); //NOI18N
                throw new RuntimeException(t);
            } finally {
                channel.stopActivity();
            }

            return null;
        }

        @Override
        protected void failed(Throwable cause) {
            // this gets executed when the error is rethrown in the doInBackground method:
            super.failed(cause);
            controller.showAndLogErroDlg("Exception occured", "exception during action " + getValue(Action.NAME).toString(), cause); //NOI18N
        }

        @Override
        protected void finished() {
            super.finished();
            AbstractWankmanAction.this.finished();
        }
    }

    /**
     * @return the controller
     */
    public MyjaphooController getController() {
        return controller;
    }

    @Override
    final public void actionPerformed(ActionEvent e) {
        String actionName = getValue(Action.NAME).toString();
        logger.info("begin run action " + actionName); //NOI18N
        try {
            controller.getView().setActionStarted();

            ActionBackgroundJob test = new ActionBackgroundJob(controller, e);
            MyjaphooApp.getApplication().getContext().getTaskService().execute(test);

            //run(controller, e);
        } catch (Exception ex) {
            controller.showAndLogErroDlg("Exception occured", "exception during action " + actionName, ex); //NOI18N
        } finally {
            controller.getView().setActionStopped();
            // add this last action to the list of last - actions for faster recalling:
            if (this instanceof DisplayAsLastUsedActions) {
                controller.getView().getThumbPanel().addLastAction(new LastActionContextActionWrapper(controller, this));
            }
        }
        logger.info("finished run action " + actionName); //NOI18N
    }

    abstract public void run(MyjaphooController controller, ActionEvent e, ViewContext context) throws Exception;

    /**
     * can be overlaoded by sublcasses. will be called, when the action thread/task
     * hasb been finished.
     **/
    public void finished() {
    }

    /**
     * Methode, um directories auszuwählen.
     * Das letzte gewählte directory wird in den prefs gespeichert u. beim nächsten mal vorgewählt.
     * @param title dialog title
     * @return die ausgewählte datei oder null bei abbruch
     */
    public File chooseFile(String title) {
        return FileChoosing.chooseFile(getController(), title);
    }

    /**
     * Methode, um directories auszuwählen.
     * Das letzte gewählte directory wird in den prefs gespeichert u. beim nächsten mal vorgewählt.
     * @param title dialog title
     * @return die ausgewählte datei oder null bei abbruch
     */
    public File chooseDir(String title) {
        return FileChoosing.chooseDir(getController(), title);
    }
}
