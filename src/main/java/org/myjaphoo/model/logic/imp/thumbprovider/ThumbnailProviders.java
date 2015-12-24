package org.myjaphoo.model.logic.imp.thumbprovider;

import org.myjaphoo.model.db.Thumbnail;

import java.io.File;
import java.util.List;

/**
 * All available thumb nail providers that could be used in the application.
 *
 */
public enum ThumbnailProviders {

    NONE {
        @Override
        public ThumbnailProvider createProvider() {
            return new ThumbnailProvider() {
                @Override
                public List<Thumbnail> createAllThumbNails(File file) {
                    throw new IllegalArgumentException();
                }

                @Override
                public boolean isAvailable() {
                    return false;
                }

                @Override
                public String getDescr() {
                    throw new IllegalArgumentException();
                }
            };
        }
    },

    FFMPEGTHUMBNAILER {
        @Override
        public ThumbnailProvider createProvider() {
            return new FfmpegthumbnailerThumbnailProvider();
        }
    },

    VLCJ {
        @Override
        public ThumbnailProvider createProvider() {
            return new VlcJThumbnailProvider();
        }
    },

    FFMPEG {
        @Override
        public ThumbnailProvider createProvider() {
            return new FfmpegThumbnailProvider();
        }
    };


    public abstract ThumbnailProvider createProvider();

}
