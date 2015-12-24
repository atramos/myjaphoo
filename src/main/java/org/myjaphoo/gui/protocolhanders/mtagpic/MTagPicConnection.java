package org.myjaphoo.gui.protocolhanders.mtagpic;

import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.gui.picmode.Picture;
import org.myjaphoo.gui.thumbtable.thumbcache.MetaTokenThumbCache;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.MetaToken;
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
public class MTagPicConnection extends URLConnection {

    private static final Logger logger = LoggerFactory.getLogger(MTagPicConnection.class);

    private static int thumbSize = MyjaphooAppPrefs.PRF_THUMBSIZE.getVal();

    public MTagPicConnection(URL u) {
        super(u);
    }

    @Override
    public void connect() throws IOException {
        connected = true;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        String data = url.toString();
        data = data.replaceFirst("mtagpic:", "");
        logger.info("mtag pic protocol for " + data);
        // this should be now a metatag name.
        // get the tag and return the picture:
        MetaToken token = findMetaTag(data);
        if (token != null) {
            logger.info("return input stream for meta tag " + token.getName());
            ImageIcon icon = (ImageIcon) MetaTokenThumbCache.getInstance().loadImageForToken(token, thumbSize, null);
            return Picture.toInputStream(icon);
        } else {
            return new ByteArrayInputStream(new byte[0]);
        }
    }


    private MetaToken findMetaTag(String name) {
        for (MetaToken tag : CacheManager.getCacheActor().getImmutableModel().getMetaTokenSet().asList()) {
            if (tag.getName().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }
}
