/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mlsoft.common.acitivity

import groovy.transform.CompileStatic
import org.mlsoft.common.acitivity.events.*
import org.mlsoft.eventbus.GlobalBus

import java.util.concurrent.CopyOnWriteArrayList

/**
 * Channel Manager.
 * @author mla
 */
@CompileStatic
public class ChannelManager {

    private static ChannelManager instance = null;

    private List<Channel> listOfRunningChannels = new CopyOnWriteArrayList<Channel>();

    private ChannelManager() {
    }

    public static Channel createChannel(Class clazz, String activityTitle) {
        return new DefaultChannel(clazz, activityTitle);
    }

    public static ChannelManager getChannelManager() {
        if (instance == null) {
            instance = new ChannelManager();
        }
        return instance;
    }

    public void startActivity(Channel channel, String activityTitle) {
        listOfRunningChannels.add(channel);
        GlobalBus.bus.post(new ActivityStartedEvent(channel, activityTitle, listOfRunningChannels.size() > 1));
    }

    public void message(Channel channel, String message) {
        GlobalBus.bus.post(new MessageEvent(channel, message, false));
    }

    public void errormessage(Channel channel, String message) {
        GlobalBus.bus.post(new ErrorMessageEvent(channel, message, null));
    }

    public void errormessage(Channel channel, String message, Throwable t) {
        GlobalBus.bus.post(new ErrorMessageEvent(channel, message, t));
    }

    public void progress(Channel channel, int percentage) {
        if (percentage > 100) {
            percentage = 100;
        }
        int overallPercentage = calcOveralPercentage();
        GlobalBus.bus.post(new ProgressEvent(channel, percentage, overallPercentage));
    }

    private int calcOveralPercentage() {
        int sum = 0;
        if (listOfRunningChannels.size() > 0) {
            for (Channel channel : listOfRunningChannels) {
                sum += channel.getLastPercentage();
            }
            return (int) sum / listOfRunningChannels.size();
        } else {
            return 0;
        }
    }

    public void stopActivity(Channel channel, String activityTitle) {
        GlobalBus.bus.post(new ActivityFinishedEvent(channel, activityTitle, listOfRunningChannels.size()>=2));
        listOfRunningChannels.remove(channel);
    }


    void emphasisedMessage(Channel channel, String message) {
        GlobalBus.bus.post(new MessageEvent(channel, message, true));
    }
}
