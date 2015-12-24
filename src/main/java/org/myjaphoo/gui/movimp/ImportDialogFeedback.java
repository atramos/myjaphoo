package org.myjaphoo.gui.movimp;

/**
 * abstract class to give feedback about import. Whereas a swing gui is used or a shell text output.
 *
 *
 * @author mla
 * @version $Id$
 */
public interface ImportDialogFeedback {
    void setStatusBarMessage(String message);

    void setStatusBarActionStopped();

    void setStatusBarProgress(int perc);

    void updatefileAndRemainingTime(String filePath, String formatDurationWords);
}
