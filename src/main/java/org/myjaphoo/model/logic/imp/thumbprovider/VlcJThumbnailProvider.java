package org.myjaphoo.model.logic.imp.thumbprovider;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.model.db.Thumbnail;
import org.myjaphoo.model.externalPrograms.ExternalPrograms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A thumb nail provider using VLCJ.
 * @author lang
 * @version $Id$
 */
public class VlcJThumbnailProvider extends AbstractThumbnailProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(VlcJThumbnailProvider.class.getName());

    @Override
    public List<Thumbnail> createAllThumbNails(File file) {

        checkInit();


        ArrayList<Thumbnail> result = new ArrayList<Thumbnail>();
        if (isProperlyInitialized()) {
            try {
                Thumbnail t = createThumbNail(file, 0.1f);
                if (t != null) {
                    result.add(t);
                }
                t = createThumbNail(file, 0.3f);
                if (t != null) {
                    result.add(t);
                }
                t = createThumbNail(file, .5f);
                if (t != null) {
                    result.add(t);
                }
                t = createThumbNail(file, .7f);
                if (t != null) {
                    result.add(t);
                }
                t = createThumbNail(file, .9f);
                if (t != null) {
                    result.add(t);
                }

            } catch (Exception e) {
                LOGGER.error("error creating thumbnails with vlc!", e);
            }
        }
        return result;
    }

    private boolean isProperlyInitialized() {
        return initialisation != null && initialisation.booleanValue();
    }

    private Thumbnail createThumbNail(File file, float percentage) {

        try {
            //Process process = null;
            File tempFile = File.createTempFile("thumb", ".jpg");
            LOGGER.debug("extract thumb nail into temp file " + tempFile);
            tempFile.deleteOnExit();

            createThumb(file.getAbsolutePath(), MyjaphooAppPrefs.PRF_THUMBSIZE.getVal(), tempFile, percentage);

            return createThumbnailFromFile(tempFile);

        } catch (Exception ex) {
            LOGGER.error("error", ex);
            return null;
        }

    }

    private static final String[] VLC_ARGS = {
            "--intf", "dummy", /* no interface */
            "--vout", "dummy", /* we don't want video (output) */
            "--no-audio", /* we don't want audio (decoding) */
            "--no-osd",
            "--no-spu",
            "--no-video-title-show", /* nor the filename displayed */
            "--no-stats", /* no stats */
            "--no-sub-autodetect-file", /* we don't want subtitles */
            //"--no-inhibit", /* we don't want interfaces */
            "--no-disable-screensaver", /* we don't want interfaces */
            "--no-snapshot-preview", /* no blending in dummy vout */
    };

    private static Object semaphore = new Object();

    private static MediaPlayerFactory factory = null;

    private static MediaPlayerFactory getOrCreateFactory() {
        synchronized (semaphore) {
            if (factory == null) {
                factory = new MediaPlayerFactory(VLC_ARGS);
            }
            return factory;
        }

    }

    private static ThreadLocal<MediaPlayer> players = new ThreadLocal<MediaPlayer>() {
        @Override
        protected MediaPlayer initialValue() {
            MediaPlayerFactory factory = getOrCreateFactory();
            return factory.newHeadlessMediaPlayer();
        }
    };

    public void createThumb(String mrl, int imageWidth, File snapshotFile, final float tnposition) throws Exception {

        final MediaPlayer mediaPlayer = players.get();
        final CountDownLatch inPositionLatch = new CountDownLatch(1);
        final CountDownLatch snapshotTakenLatch = new CountDownLatch(1);

        MediaPlayerEventAdapter listener = new MediaPlayerEventAdapter() {

            @Override
            public void positionChanged(MediaPlayer mp, float newPosition) {
                if (newPosition >= tnposition * 0.9f) { /* 90% margin */
                    inPositionLatch.countDown();
                }
            }

            @Override
            public void snapshotTaken(MediaPlayer mp, String filename) {
                LOGGER.debug("snapshotTaken event(filename=" + filename + ")");
                snapshotTakenLatch.countDown();
            }
        };
        mediaPlayer.addMediaPlayerEventListener(listener);

        try {
            if (mediaPlayer.startMedia(mrl)) {
                mediaPlayer.setPosition(tnposition);
                inPositionLatch.await(10, TimeUnit.SECONDS);

                mediaPlayer.saveSnapshot(snapshotFile, imageWidth, 0);
                snapshotTakenLatch.await(10, TimeUnit.SECONDS);

                mediaPlayer.stop();
            }
        } finally {
            mediaPlayer.removeMediaPlayerEventListener(listener);
        }

    }

    @Override
    public boolean isAvailable() {
        checkInit();
        return isProperlyInitialized();
    }

    @Override
    public String getDescr() {
        return "VLC via VLCJ";
    }

    /** marker, for lazy initialisation. we try a initialisation for each instance of this class. */
    private Boolean initialisation = null;

    private void checkInit() {
        if (initialisation == null) {
            try {
                initVlcj();
                initialisation = true;
            } catch (UnsatisfiedLinkError e) {
                LOGGER.error("failed to initialize VLCJ!", e);
                initialisation = false;
            }
        }

    }


    private void initVlcj() {
        // add available search paths:
        // take the one from the configuration for vlc itself:
        if (ExternalPrograms.VLC.exists()) {
            File vlcExecFile = new File(ExternalPrograms.VLC.getProgramExecutable());
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), vlcExecFile.getParent());

        }
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
    }
}
