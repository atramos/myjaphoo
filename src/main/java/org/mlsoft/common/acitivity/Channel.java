/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.common.acitivity;

/**
 * @author mla
 */
public interface Channel {

    int getId();

    void startActivity();

    void setProgressSteps(int steps);

    void nextProgress();

    void message(String message);

    void errormessage(String message);

    void errormessage(String message, Throwable t);

    void emphasisedMessage(String message);

//    void startSubActivity(String name);
//
//    void stopSubActivity();

    void progress(int percentage);

    void stopActivity();

    String getActivityTitle();

    String getDuration();

    long getTime();

    int getLastPercentage();
}
