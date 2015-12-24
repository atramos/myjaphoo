package org.myjaphoo.model.logic.imp.thumbprovider;

import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.model.db.Thumbnail;
import org.myjaphoo.model.externalPrograms.ExternalPrograms;
import org.myjaphoo.model.logic.exec.Exec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Thumb nail provider which uses ffmpegthumbnailer. Currently the best provider.
 *
 * @author mla
 * @version $Id$
 */
public class FfmpegthumbnailerThumbnailProvider extends AbstractThumbnailProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(FfmpegthumbnailerThumbnailProvider.class.getName());

    @Override
    public List<Thumbnail> createAllThumbNails(File file) {
        ArrayList<Thumbnail> result = new ArrayList<Thumbnail>();
        try {
            Thumbnail t = createThumbNail(file, 10);
            if (t != null) {
                result.add(t);
            }
            t = createThumbNail(file, 30);
            if (t != null) {
                result.add(t);
            }
            t = createThumbNail(file, 50);
            if (t != null) {
                result.add(t);
            }
            t = createThumbNail(file, 70);
            if (t != null) {
                result.add(t);
            }
            t = createThumbNail(file, 90);
            if (t != null) {
                result.add(t);
            }

        } catch (Exception e) {
            LOGGER.error("error creating thumbnails with ffmpegthumbnailer!", e);
        }
        return result;
    }

    @Override
    public boolean isAvailable() {
        return ExternalPrograms.FFMPEGTHUMBNAILER.exists();
    }

    @Override
    public String getDescr() {
        return "ffmpegthumbnailer via command line";
    }


    private Exec.Result genCommand(File file, File tempFile, int percentage) throws IOException, InterruptedException {
        ArrayList<String> args = new ArrayList<String>();
        args.add("-i");
        args.add(file.getAbsolutePath());
        args.add("-o");
        args.add(tempFile.getAbsolutePath());
        args.add("-t");
        args.add(Integer.toString(percentage));
        args.add("-s");
        args.add(Integer.toString(MyjaphooAppPrefs.PRF_THUMBSIZE.getVal()));

        return ExternalPrograms.FFMPEGTHUMBNAILER.execAndWait(args);
    }

    private Thumbnail createThumbNail(File file, int percentage) {
        try {
            File tempFile = File.createTempFile("thumb", ".jpg");
            tempFile.deleteOnExit();
            Exec.Result result = genCommand(file, tempFile, percentage);

            if (result.exitVal == 0) {
                return createThumbnailFromFile(tempFile);
            } else {
                return null;
            }
        } catch (Exception ex) {
            LOGGER.error("error", ex);
            return null;
        }

    }

}
