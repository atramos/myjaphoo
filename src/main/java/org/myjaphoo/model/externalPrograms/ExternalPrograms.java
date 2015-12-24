package org.myjaphoo.model.externalPrograms;

import static org.myjaphoo.MyjaphooAppPrefs.*;

/**
 * All external programs that get used in this application.
 *
 * @author mla
 * @version $Id$
 */
public class ExternalPrograms {

    /**
     * external program vlc.
     */
    public static final ExternalProgram VLC = new ExternalProgram("VLC",
            "VLC is needed for playback of videos, import of videos (thumb nails)",
            PRF_WINDOWS_VLCEXE,
            PRF_LINUX_VLCEXE,
            "c:/programme/videoLAN/vlc/vlc.exe",
            "vlc",
            "/usr/bin/vlc");

    /** external program mplayer. */
    public static final ExternalProgram MPLAYER = new ExternalProgram("Mplayer",
            "Mplayer is used to import properties of videos. It could be alternatively used for playback of videos",
            PRF_WINDOWS_MPLAYER_EXE,
            PRF_LINUX_MPLAYER_EXE,
            "mplayer",
            "c:/programme/mplayer/mplayer.exe",
            "/usr/bin/mplayer");

    /** external program kmplayer. */
    public static final ExternalProgram KMPLAYER = new ExternalProgram("KMplayer",
            "KMplayer could be alternatively used for playback of videos",
            PRF_LINUX_KMPLAYER_EXE,
            PRF_LINUX_KMPLAYER_EXE,
            "/usr/bin/kmplayer");

    /** ffmpegthumbnailer. */
    public static final ExternalProgram FFMPEGTHUMBNAILER = new ExternalProgram("ffmpegthumbnailer",
            "ffmpegthumbnailer could be alternatively used to create thumbnails for videos during import (instead of VLC)",
            PRF_WINDOWS_FFMPEGTHUMBNAILER_EXE,
            PRF_LINUX_FFMPEGTHUMBNAILER_EXE,
            "ffmpegthumbnailer",
            "c:/programme/ffmpegthumbnailer/ffmpegthumbnailer.exe",
            "/usr/bin/ffmpegthumbnailer");

    /** ffmpeg. */
    public static final ExternalProgram FFMPEG = new ExternalProgram("ffmpeg",
            "ffmpeg could be alternatively used to create thumbnails for videos during import (instead of VLC)",
            PRF_WINDOWS_FFMPEG_EXE,
            PRF_LINUX_FFMPEG_EXE,
            "ffmpeg",
            "c:/programme/ffmpeg/bin/ffmpeg.exe",
            "/usr/bin/ffmpeg");

    public static final ExternalProgram[] ALL_PRGS = {VLC, MPLAYER, KMPLAYER, FFMPEGTHUMBNAILER, FFMPEG};

}
