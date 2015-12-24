/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import org.jdesktop.application.Task;

/**
 * Erweiterung von jdesktop tasks, welches die app blocked,
 * und einen activity listener installiert.
 * @author lang
 */
abstract public class MyjaphooBackgroundJob<R, T> extends Task<R, T> {

    private MyjaphooView view;
    private MyjaphooController controller;


    public MyjaphooBackgroundJob(MyjaphooView view, MyjaphooController controller) {
        super(MyjaphooApp.getApplication().getInstance());
        this.view = view;
        this.controller = controller;

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
    protected void finished() {
        super.finished();
    }
}
