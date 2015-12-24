package poc;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;

/**
 * @author lang
 * @version $Id$
 *          To change this template use File | Settings | File Templates.
 */
public class TestVlcj  {

    public static void main(String[] args) throws Exception {
        new TestVlcj().start();
    }

   public void start() throws InterruptedException {

       NativeLibrary.addSearchPath(
           RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC"
         );
       Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

       SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               Tutorial2B("blbb");
           }
       });

       Thread.sleep(10000);

   }



    private void Tutorial2B(String... args) {
      JFrame frame = new JFrame("vlcj Tutorial");

        EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

      frame.setContentPane(mediaPlayerComponent);

      frame.setLocation(100, 100);
      frame.setSize(1050, 600);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);

      mediaPlayerComponent.getMediaPlayer().playMedia(args[0]);
    }
}
