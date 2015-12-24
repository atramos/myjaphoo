package org.myjaphoo.gui.icons

import groovy.xml.MarkupBuilder

import javax.swing.*
import java.awt.image.BufferedImage

/**
 * Icons 
 * @author mla
 * @version $Id$
 *
 */
class Icons {



    public static class IconRes {

        public final URL url;
        public final ImageIcon icon;

        public IconRes(String ressource) {
            url = Icons.class.getResource(ressource);
            icon = new ImageIcon(url);

        }

        public BufferedImage createBufferedImage() throws IOException {
            return javax.imageio.ImageIO.read(url);
        }

        public String htmlFragment() {
            return "<img src=\"" + url.toString() + "\" /> ";
        }

        public void append(MarkupBuilder mkb) {
            mkb.img(src: url.toString())
        }
    }
    public static final IconRes IR_THUMBCARDVIEWN = new IconRes("led-icons/application_view_detail.png");
    public static final IconRes IR_THUMBVIEW = new IconRes("led-icons/application_view_tile.png");
    public static final IconRes IR_NOTUNIQUE = new IconRes("led-icons/direction.png");
    public static final IconRes IR_RATING = new IconRes("led-icons/award_star_gold.png");
    public static final IconRes IR_NOTLOCATED = new IconRes("led-icons/disconnect.png");
    public static final IconRes IR_DUPLICATES = new IconRes("led-icons/slides.png");
    public static final IconRes IR_CONDENSED = new IconRes("condensed.png");
    public static final IconRes IR_INCOMPDATABASE = new IconRes("led-icons/databases.png");
    public static final IconRes IR_MOVIE = new IconRes("led-icons/film.png");
    public static final IconRes IR_PICTURE = new IconRes("led-icons/picture.png");
    public static final IconRes IR_TEXT = new IconRes("led-icons/text_dropcaps.png");
    public static final IconRes IR_COMPRESSED = new IconRes("led-icons/compress.png");
    public static final IconRes IR_HOME = new IconRes("icons-circular/home.png");
    public static final IconRes IR_INFO = new IconRes("icons-circular/information.png");
    public static final IconRes IR_BOOKMARK = new IconRes("bookmarks.png");
    public static final IconRes IR_ERROR = new IconRes("led-icons/error.png");
    public static final IconRes IR_EMPTY_PIC = new IconRes("smerrell_Polaroid_2.png");
    public static final IconRes IR_APP16 = new IconRes("sheikh_tuhin_Gallery_Icon16x16.png");
    public static final IconRes IR_TAG = new IconRes("tag.png");
    public static final IconRes IR_MEDIA_PLAYBACK = new IconRes("media-playback-start-3.png");
    public static final IconRes IR_COPY = new IconRes("edit-copy-7.png");
    public static final IconRes IR_TAG_ADD = new IconRes("tag-blue-add.png");
    public static final IconRes IR_TAG_DEL = new IconRes("tag-blue-delete.png");
    public static final IconRes IR_DB_REMOVE = new IconRes("db_remove.png");
    public static final IconRes IR_DELETE = new IconRes("mail-delete.png");
    public static final IconRes IR_YES = new IconRes("icons-circular/yes.png");
    public static final IconRes IR_NO = new IconRes("icons-circular/no.png");
    public static final IconRes IR_WINDOW_CLOSE = new IconRes("window-close.png");
    public static final IconRes IR_FILTER = new IconRes("filter.png");
    public static final IconRes IR_GO_DOWN = new IconRes("go-down.png");
    // TODO metatag icon for now just the same as tag:
    public static final IconRes IR_METATAG = new IconRes("tag.png");

    public static final IconRes IR_BOOKMARK_FOLDER = new IconRes("bookmark-folder.png");

    public static final IconRes IR_GROOVY = new IconRes("/groovy/ui/ConsoleIcon.png");

    public static final IconRes IR_ARROW_LEFT = new IconRes("arrow_left.png");
    public static final IconRes IR_ARROW_RIGHT = new IconRes("arrow_right.png");
}
