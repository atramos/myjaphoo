package org.myjaphoo.model.logic.imp;

import org.myjaphoo.model.logic.imp.movInfoProvider.MPlayerMovieAttributeProvider;
import org.myjaphoo.model.logic.imp.movInfoProvider.MovieAttributeProvider;
import org.myjaphoo.model.logic.imp.thumbprovider.ThumbnailProvider;
import org.myjaphoo.model.logic.imp.thumbprovider.ThumbnailProviders;

/**
 * Factory to get all providers for the import.
 * User: mla
 * Date: 08.04.13
 * Time: 17:12
 */
public class ProviderFactory {

    /**
     * Gets the "best" available provider.
     * If no provider is available, it throws an exception.
     *
     * @return
     */
    public static ThumbnailProvider getBestThumbnailProvider() {

        // try the order given by the enum of thumb nail providers:
        for (ThumbnailProviders tps: ThumbnailProviders.values()) {
            ThumbnailProvider thumbnailProvider = tps.createProvider();
            if (thumbnailProvider.isAvailable()) {
                return thumbnailProvider;
            }
        }
        // no alternatives anymore:
        throw new RuntimeException("no thumb nail creation possible! Please configure external tools properly! Either ffmpegthumbnailer, ffmpeg or vlc!");

    }
    /**
     * Gets the "best" available provider.
     * If no provider is available, it throws an exception.
     * @return the best provider
     */
    public static MovieAttributeProvider getBestMovieAttributeProvider() {

        // 1. mplayer:
        MovieAttributeProvider mi = new MPlayerMovieAttributeProvider();
        if (mi.isAvailable()) {
            return mi;
        }
        // no alternatives anymore:
        throw new RuntimeException("no movie attribute extraction possible! Please configure external tools properly! Either mplayer or vlc!");

    }
}
