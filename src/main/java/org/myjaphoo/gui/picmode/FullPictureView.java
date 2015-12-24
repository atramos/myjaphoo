package org.myjaphoo.gui.picmode;

import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * <p>
 * �berschrift:
 * </p>
 * <p>
 * Beschreibung:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Organisation:
 * </p>
 *
 * @author unbekannt
 * @version 1.0
 */
public class FullPictureView extends JFrame {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/picmode/resources/FullPictureView");

    private static List<FullPictureView> pictureViews = new ArrayList<FullPictureView>();
    public static final Logger LOGGER = LoggerFactory.getLogger(FullPictureView.class.getName());
    private static final float SCALEFACTORFACTOR = 1.5f;
    //private ImageIcon splashIm;
    private Dimension screenDim;
    private JLabel label; // = new JLabel();
    private JScrollPane pane;
    private JViewport viewport;
    //private JMenuBar menu;
    private Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    private JScrollBar vbar, hbar;
    private int lastMouseY, lastMouseX;
    private Cursor handCursor, defaultCursor;
    // popup menu
    private JMenuItem slideshow_item;
    private JMenuItem stop_slideshow_item;
    private ImageIterator imgList;
    private int width, height;
    /** sollen tooltip infos angezeigt werden oder nicht? ! */
    private boolean infoMode = false;

    public FullPictureView(JFrame parent, List<AbstractLeafNode> list) {
        super(localeBundle.getString("VIEWER"));
        this.imgList = new ImageIterator(list);

        // listener zum darstellen von im hintergrund geladenen bildern:
        imgList.addListener(new PicturePreLoadingCache.PictureLoadingEventListener() {

            @Override
            public void pictureLoadedEvent(final String path, final BufferedImage image) {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        if (imgList.isCurrent(path)) {
                            displayImage(new ImageIcon(image));
                        }
                    }
                });
            }
        });
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowEventHandler());
        /*
        try {
        splashIm = loadScaledImage(imgList.getCurrent());
        } catch (Exception ex) {
        LOGGER.log(Level.SEVERE, null, ex);
        }
         */
        init();
    }

    class WindowEventHandler extends WindowAdapter {
        public void windowClosing(WindowEvent evt) {
            closeView();
        }
    }

    public void init() {
        handCursor = new Cursor(Cursor.HAND_CURSOR);
        defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        screenDim = Toolkit.getDefaultToolkit().getScreenSize();

        setSize((int) screenDim.getWidth(), (int) screenDim.getHeight());
        viewport = new JViewport();
        viewport.setView(new JLabel());

        pane.setViewport(viewport);
        getContentPane().validate();

        findOutWidthHeight();
        redisplayImage();


        // doing the key scroll
        vbar = pane.getVerticalScrollBar();
        hbar = pane.getHorizontalScrollBar();

        addKeyListener(new Listen());

        // menu section
        //menu = new JMenuBar();

        final JMenuItem NextImage_item = new JMenuItem(localeBundle.getString("NEXT IMAGE"));
        final JMenuItem PreviousImage_item = new JMenuItem(localeBundle.getString("PREVIOUS IMAGE"));

        slideshow_item = new JMenuItem(localeBundle.getString("RUN SLIDE SHOW"));
        stop_slideshow_item = new JMenuItem(localeBundle.getString("STOP SLIDE SHOW"));
        stop_slideshow_item.setEnabled(false);

        final JMenuItem exit_item = new JMenuItem(localeBundle.getString("EXIT"));
        final JCheckBoxMenuItem Fullscreen_item = new JCheckBoxMenuItem(
                localeBundle.getString("FULL SCREEN"), true);

        NextImage_item.addActionListener(
                // open the next image on file list
                new ActionListener() {

                    public void actionPerformed(ActionEvent event) {
                        loadAndDisplayImage(imgList.next(width, height));
                    }
                });

        PreviousImage_item.addActionListener(
                // open the previous image on list
                new ActionListener() {

                    public void actionPerformed(ActionEvent event) {
                        loadAndDisplayImage(imgList.prev(width, height));
                    }
                });

        exit_item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                //imageiterator.Quit();
                closeView();
            }
        });

        slideshow_item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
            }
        });

        stop_slideshow_item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {

                slideshow_item.setEnabled(true);
                stop_slideshow_item.setEnabled(false);
            }
        });

        Fullscreen_item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (!Fullscreen_item.getState()) {
                    dispose();
                    // when close fullscreen window, select the file that
                    // currently display.

                }
            }
        });
        /*
        menu.add(NextImage_item);
        menu.add(PreviousImage_item);
        menu.add(Fullscreen_item);
        menu.add(slideshow_item);
        menu.add(stop_slideshow_item);
        menu.add(exit_item);
        setJMenuBar(menu);
        getContentPane().add(menu);
         */
        // add mouse listener for window
        addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {
                label_mouseDragged(e);
            }
        });
        addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    dispose();
                }
            }

            public void mousePressed(MouseEvent event) {
                label_mousePressed(event);
            }

            public void mouseReleased(MouseEvent event) {
                label_mouseReleased(event);
            }

            public void mouseEntered(MouseEvent event) {
            }

            public void mouseExited(MouseEvent event) {
            }
        });
        // end the add listener section

        Rectangle winDim = getBounds();
        setLocation((screenDim.width - winDim.width) / 2,
                (screenDim.height - winDim.height) / 2);
        getContentPane().add(pane);
        pane.setBounds(0, 0, screenDim.width, screenDim.height);
        setVisible(true);
        pictureViews.add(this);
    }

    public void closeView() {
        imgList.destruct();
        setVisible(false);
        dispose();
        pictureViews.remove(this);
    }

    public static boolean exeEscAction() {
        boolean hasHadViews = pictureViews.size() > 0;
        List<FullPictureView> listCopy = new ArrayList<>(pictureViews);
        for (FullPictureView pv : listCopy) {
            pv.closeView();
        }
        return hasHadViews;
    }

    public boolean loadAndDisplayImage(ImageIterator.ImageInfo ii) {
        try {
            ImageIcon splashIm = loadScaledImage(ii);
            if (splashIm != null) {
                displayImage(splashIm);
            }
            setTitle(ii.descr);
        } catch (Exception ex) {
            LOGGER.error("loading image failed", ex); //NOI18N
            displayLoadingFailed(ii);
        }
        return true;
    }

    private void displayLoadingFailed(ImageIterator.ImageInfo ii) {

        screenDim = Toolkit.getDefaultToolkit().getScreenSize();

        setSize((int) screenDim.getWidth(), (int) screenDim.getHeight());
        setForeground(Color.black);
        label = new JLabel(MessageFormat.format(localeBundle.getString("FAILED TO DISPLAY"), ii.descr));

        viewport = new JViewport();
        viewport.setView(label);

        pane.setViewport(viewport);
        getContentPane().validate();

    }

    private void displayImage(ImageIcon splashIm) {

        screenDim = Toolkit.getDefaultToolkit().getScreenSize();

        setSize((int) screenDim.getWidth(), (int) screenDim.getHeight());
        setForeground(Color.black);
        label = new JLabel(splashIm);
        String txt = imgList.getcurrentToolTipText();
        label.setToolTipText(txt);

        if (infoMode) {
            label.setText(txt);
        }

        if (splashIm.getIconWidth() > screenDim.getWidth() || splashIm.getIconHeight() > screenDim.getHeight()) { // mean
            // the
            // image
            // is
            // larger
            // than
            // screen
            // size
            setCursor(handCursor);
        } else {
            setCursor(defaultCursor);
        }

        viewport = new JViewport();
        viewport.setView(label);

        pane.setViewport(viewport);
        getContentPane().validate();

    }

    void label_mouseDragged(MouseEvent e) {
        int mouseY = e.getY();
        int mouseX = e.getX();

        if (mouseY > lastMouseY) {
            vbar.setValue(vbar.getValue() - vbar.getBlockIncrement());
            if (mouseX > lastMouseX) {
                hbar.setValue(hbar.getValue() - hbar.getBlockIncrement());
            } else if (mouseX < lastMouseX) {
                hbar.setValue(hbar.getValue() + hbar.getBlockIncrement());
            }
        } else if (mouseY < lastMouseY) {
            vbar.setValue(vbar.getValue() + vbar.getBlockIncrement());
            if (mouseX > lastMouseX) {
                hbar.setValue(hbar.getValue() - hbar.getBlockIncrement());
            } else if (mouseX < lastMouseX) {
                hbar.setValue(hbar.getValue() + hbar.getBlockIncrement());
            }
        }

        lastMouseY = mouseY;
        lastMouseX = mouseX;
    }

    void label_mousePressed(MouseEvent e) {
        lastMouseY = e.getY();
        lastMouseX = e.getX();
        if (e.isPopupTrigger()) {
            int x = e.getX();
            int y = e.getY();
            // if click point is too closed to the edge of screen
            if (x >= (int) d.getWidth() - 117) {
                x = (int) d.getWidth() - 117;
            }
            if (y >= (int) d.getHeight() - 92) {
                y = (int) d.getHeight() - 92;
            }
            // menu.show(pane, x, y);
        }
    }

    void label_mouseReleased(MouseEvent e) {
        lastMouseY = e.getY();
        lastMouseX = e.getX();
        if (e.isPopupTrigger()) {
            int x = e.getX();
            int y = e.getY();
            // if click point is too closed to the edge of screen
            if (x >= (int) d.getWidth() - 117) {
                x = (int) d.getWidth() - 117;
            }
            if (y >= (int) d.getHeight() - 92) {
                y = (int) d.getHeight() - 92;
            }
            // menu.show(pane, x, y);
        }
    }

    class Listen extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            boolean shift = e.getModifiers() == KeyEvent.SHIFT_MASK;
            boolean up = e.getKeyCode() == KeyEvent.VK_UP;
            boolean down = e.getKeyCode() == KeyEvent.VK_DOWN;
            if (shift && up) {
                loadAndDisplayImage(imgList.next(width, height));
            } else if (up) {
                vbar.setValue(vbar.getValue() - vbar.getBlockIncrement());
            } else if (shift && down) {
                loadAndDisplayImage(imgList.prev(width, height));
            } else if (down) {
                vbar.setValue(vbar.getValue() + vbar.getBlockIncrement());
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                hbar.setValue(hbar.getValue() - hbar.getBlockIncrement());
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                hbar.setValue(hbar.getValue() + hbar.getBlockIncrement());
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                // wird vom WankmanView bereits abgefangen u. gehandelt.
            } else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN || e.getKeyCode() == KeyEvent.VK_SPACE) {
                loadAndDisplayImage(imgList.next(width, height));
            } else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                loadAndDisplayImage(imgList.prev(width, height));
            } else if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_ADD) {
                ScaleBigger();
            } else if (e.getKeyCode() == KeyEvent.VK_MINUS || e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
                ScaleSmaller();
            } else if (e.getKeyChar() == 'i') {
                toggleInfoMode();
            }

        }

        private void ScaleSmaller() {
            scaleFactor = scaleFactor / SCALEFACTORFACTOR;
            redisplayImage();
        }

        private void ScaleBigger() {
            scaleFactor = scaleFactor * SCALEFACTORFACTOR;
            redisplayImage();
        }
    }

    private void toggleInfoMode() {
        infoMode = !infoMode;
        redisplayImage();
    }

    private void redisplayImage() {
        loadAndDisplayImage(imgList.getCurrent(width, height));
    }
    private float scaleFactor = 1;

    private void findOutWidthHeight() {
        Rectangle winDim = getContentPane().getBounds();

        if (winDim.getHeight() == 0 && winDim.getWidth() == 0) {
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

            width = (int) dim.getWidth();
            height = (int) dim.getHeight() - 35;

        } else {
            width = (int) winDim.getWidth();
            height = (int) winDim.getHeight();
        }
    }

    private ImageIcon loadScaledImage(ImageIterator.ImageInfo ii) throws Exception {

        findOutWidthHeight();

        BufferedImage img = ii.image;

        if (img == null) {
            return null;
        }
        if (Math.abs(scaleFactor) < 0.001) {
            return new ImageIcon(img);
        } else {
            LOGGER.info("resizing with factor " + scaleFactor); //NOI18N
            img = Picture.getScaledInstance(img, scaleFactor);
            return new ImageIcon(img);
        }

    }
}
