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
 * thumb nail provider using ffmpeg.
 * @author mla
 * @version $Id$

 */
public class FfmpegThumbnailProvider extends AbstractThumbnailProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(FfmpegThumbnailProvider.class.getName());

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
            LOGGER.error("error creating thumbnails with ffmpeg!", e);
        }
        return result;
    }

    @Override
    public boolean isAvailable() {
        return ExternalPrograms.FFMPEG.exists();
    }

    @Override
    public String getDescr() {
        return "ffmpgeg via command line";
    }

    private Thumbnail createThumbNail(File file, int percentage) {
        try {
            File tempFile = File.createTempFile("thumb", ".jpg");
            tempFile.deleteOnExit();
            tempFile.delete();
            Exec.Result result = genCommand(file, tempFile);

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

    private Exec.Result genCommand(File file, File tempFile) throws IOException, InterruptedException {
        // ffmpeg -y -i m:\podcast\200809_nC_Podcast.mp4  -f image2 -ss 1  -vframes 1 -s 128x128 -an thumb.jpg
        ArrayList<String> args = new ArrayList<String>();
        args.add("-i");
        args.add(file.getAbsolutePath());
        args.add("-f");
        args.add("image2");
        args.add("-ss");
        args.add("10");
        args.add("-s");
        args.add(Integer.toString(MyjaphooAppPrefs.PRF_THUMBSIZE.getVal()) + "x" + Integer.toString(MyjaphooAppPrefs.PRF_THUMBSIZE.getVal()));
        args.add("-an");
        args.add("-vframes");
        args.add("1");
        args.add(tempFile.getAbsolutePath());

        return ExternalPrograms.FFMPEG.execAndWait(args);
    }
}
