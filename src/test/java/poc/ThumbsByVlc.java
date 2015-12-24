package poc;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.io.File;
import java.util.concurrent.CountDownLatch;

/**
 * @author mla
 * @version $Id$
 *          To change this template use File | Settings | File Templates.
 */
public class ThumbsByVlc {


    public static void main(String[] args) throws Exception {
        new ThumbsByVlc().testit();
    }

    public void testit() throws Exception {

        NativeLibrary.addSearchPath(
                RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC"
        );
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);


        createThumb("L:\\lockervomhocker\\film01.mp4", 128, new File("c:\\temp\\test.jpg"));
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

    private static final float VLC_THUMBNAIL_POSITION = 30.0f / 100.0f;

    public void createThumb(String mrl, int imageWidth, File snapshotFile) throws Exception {


        MediaPlayerFactory factory = new MediaPlayerFactory(VLC_ARGS);
        MediaPlayer mediaPlayer = factory.newHeadlessMediaPlayer();

        final CountDownLatch inPositionLatch = new CountDownLatch(1);
        final CountDownLatch snapshotTakenLatch = new CountDownLatch(1);

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void positionChanged(MediaPlayer mediaPlayer, float newPosition) {
                if (newPosition >= VLC_THUMBNAIL_POSITION * 0.9f) { /* 90% margin */
                    inPositionLatch.countDown();
                }
            }

            @Override
            public void snapshotTaken(MediaPlayer mediaPlayer, String filename) {
                System.out.println("snapshotTaken(filename=" + filename + ")");
                snapshotTakenLatch.countDown();
            }
        });

        if (mediaPlayer.startMedia(mrl)) {
            mediaPlayer.setPosition(VLC_THUMBNAIL_POSITION);
            inPositionLatch.await(); // Might wait forever if error

            mediaPlayer.saveSnapshot(snapshotFile, imageWidth, 0);
            snapshotTakenLatch.await(); // Might wait forever if error

            mediaPlayer.stop();
        }

        mediaPlayer.release();
        factory.release();
    }
}
