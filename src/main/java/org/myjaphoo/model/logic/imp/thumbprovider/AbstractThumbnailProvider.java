package org.myjaphoo.model.logic.imp.thumbprovider;

import org.myjaphoo.MyjaphooAppPrefs;
import org.myjaphoo.model.db.ThumbType;
import org.myjaphoo.model.db.Thumbnail;

import java.io.File;
import java.io.IOException;

/**
 * Base class for thumb nail providers. All have in common, that they dump the thumb to a local temp file
 * and then load the thumb from this one.
 */
public abstract class AbstractThumbnailProvider implements ThumbnailProvider {

    protected Thumbnail createThumbnailFromFile(File tempFile) throws IOException {
        Thumbnail t = new Thumbnail();
        t.setThumbnail(org.apache.commons.io.FileUtils.readFileToByteArray(tempFile));

        t.setType(ThumbType.NORMAL);

        t.setH(MyjaphooAppPrefs.PRF_THUMBSIZE.getVal());
        t.setW(MyjaphooAppPrefs.PRF_THUMBSIZE.getVal());

        return t;
    }
}
