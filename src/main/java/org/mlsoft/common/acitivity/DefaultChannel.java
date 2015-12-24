/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.common.acitivity;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lang
 */
public class DefaultChannel implements Channel {

    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultChannel.class.getName());

    private int id;
    /** last percentage. -1 means, that no progress activity has yet been started. */
    private int lastPercentage = -1;
    private int progressSteps = 0;
    private int currentStep = 0;
    private Class clazz;
    private StopWatch watch = new StopWatch();
    private String activityTitle;

    public DefaultChannel(Class clazz, String activityTitle) {
        this.clazz = clazz;
        this.activityTitle = activityTitle;
        id = createId();

    }

    public Class getFilterClass() {
        return clazz;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void startActivity() {
        ChannelManager.getChannelManager().startActivity(this, activityTitle);
        watch.start();
    }

    @Override
    public void setProgressSteps(int steps) {
        progressSteps = steps;
        currentStep = 0;
        lastPercentage = 0;
    }

    @Override
    public void nextProgress() {
        assert progressSteps != 0 : "progressStep is 0! was not set!";
        currentStep++;
        progress(currentStep * 100 / progressSteps);
    }

    @Override
    public void message(String message) {
        ChannelManager.getChannelManager().message(this, message);
    }
    @Override
    public void emphasisedMessage(String message) {
        ChannelManager.getChannelManager().emphasisedMessage(this, message);
    }

//    @Override
//    public void startSubActivity(String name) {
//        ChannelManager.getChannelManager().startSubActivity(this, activityTitle);
//    }
//
//    @Override
//    public void stopSubActivity() {
//        ChannelManager.getChannelManager().stopSubActivity(this, activityTitle);
//    }

    @Override
    public void errormessage(String message) {
        ChannelManager.getChannelManager().errormessage(this, message);
    }

    @Override
    public void errormessage(String message, Throwable t) {
        ChannelManager.getChannelManager().errormessage(this, message, t);
    }

    @Override
    public void progress(int percentage) {
        if (percentage > 100) {
            percentage = 100;
        }
        if (lastPercentage != percentage) {
            lastPercentage = percentage;
            ChannelManager.getChannelManager().progress(this, percentage);
        }
    }

    /**
     * last percentage. if not yet any progress has started, then the last percentage is 100%. This is relevant
     * for channels which do not deliver any progress at all to calc the overal percentage.
     * @return
     */
    public int getLastPercentage() {
        return lastPercentage<0 ? 100: lastPercentage;
    }


    @Override
    public void stopActivity() {
        watch.stop();
        ChannelManager.getChannelManager().stopActivity(this, activityTitle + ": " + watch.toString());
    }
    private static int idCounter = 0;

    private synchronized int createId() {
        idCounter++;
        return idCounter;
    }

    @Override
    public String getActivityTitle() {
        return activityTitle;
    }

    @Override
    public String getDuration() {
        return watch.toString();
    }
    
    @Override
    public long getTime() {
        return watch.getTime();
    }
    
}
