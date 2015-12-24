package org.myjaphoo.command

import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Shell
import org.myjaphoo.MyjaphooDBPreferences
import org.myjaphoo.gui.movimp.ImportDialogFeedback
import org.myjaphoo.gui.movimp.ImportWithActorsSwingWorker
import org.myjaphoo.model.logic.imp.ImportDelegator
import org.myjaphoo.model.logic.imp.ImportTypes

/**
 * StatisticsCommand
 *
 * @author mla
 * @version $Id$
 */
public class ImportCommand extends CommandSupport implements ImportDialogFeedback {

    ImportCommand(final Shell shell) {
        super(shell, 'imp', '\\imp')
    }

    @Override
    public Object execute(List<String> args) {
        assert args.size() == 2;
        String mode = args.get(0)
        String folder = args.get(1);

        ImportTypes types = ImportTypes.valueOf(mode);

        File dir = null;
        if (folder == null) {
            dir =  new File(MyjaphooDBPreferences.PRF_PRESELECTEDDIR_IN_IMPORTFILEDIALOG.getVal());
        } else {
            dir = new File(folder);
        }

            ImportDelegator delegator = types.createImportDelegator();
        ImportWithActorsSwingWorker worker = new ImportWithActorsSwingWorker(this, delegator, dir);
            worker.execute();

        waitTillFinished(worker);

        MyjaphooDBPreferences.PRF_PRESELECTEDDIR_IN_IMPORTFILEDIALOG.setVal(dir.getAbsolutePath());
    }

    def waitTillFinished(ImportWithActorsSwingWorker worker) {
        while (!worker.isDone() && !worker.isCancelled()) {
            Thread.sleep(500);
        }
    }

    @Override
    void setStatusBarMessage(String message) {
        this.shell.println(message);
    }

    @Override
    void setStatusBarActionStopped() {
        this.shell.println("stopped!");
    }

    @Override
    void setStatusBarProgress(int perc) {
        this.shell.println("importing... $perc %...");
    }

    @Override
    void updatefileAndRemainingTime(String filePath, String formatDurationWords) {
        this.shell.println("importing $filePath, estimated time to end $formatDurationWords");
    }
}
