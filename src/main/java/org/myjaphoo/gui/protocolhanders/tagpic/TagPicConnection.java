package org.myjaphoo.gui.protocolhanders.tagpic;

import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.gui.picmode.Picture;
import org.myjaphoo.gui.thumbtable.thumbcache.TokenThumbCache;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Protocol connection for internal protocol to deliver tag images.
 * see
 * http://stackoverflow.com/questions/9388264/jeditorpane-with-inline-image
 * for explanation.
 */
public class TagPicConnection extends URLConnection {

    private static final Logger logger = LoggerFactory.getLogger(TagPicConnection.class);

    private static int thumbSize = MyjaphooAppPrefs.PRF_THUMBSIZE.getVal();

    public TagPicConnection(URL u) {
        super(u);
    }

    @Override
    public void connect() throws IOException {
        connected = true;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        String data = url.toString();
        data = data.replaceFirst("tagpic:", "");
        logger.info("tag pic protocol for " + data);
        // this should be now a tag name.
        // get the tag and return the picture:
        Token token = findTag(data);
        if (token != null) {
            logger.info("return input stream for tag " + token.getName());
            ImageIcon icon = TokenThumbCache.getInstance().loadImageForToken(token, thumbSize, null);
            if (icon != null) {
                return Picture.toInputStream(icon);
            }
        }
        return new ByteArrayInputStream(new byte[0]);
    }


    private Token findTag(String name) {
        for (Token tag : CacheManager.getCacheActor().getImmutableModel().getTokenSet().asList()) {
            if (tag.getName().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }
}
