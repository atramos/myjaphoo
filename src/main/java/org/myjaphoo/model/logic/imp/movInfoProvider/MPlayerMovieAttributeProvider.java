package org.myjaphoo.model.logic.imp.movInfoProvider;

import org.apache.commons.lang.StringUtils;
import org.myjaphoo.model.db.MovieAttrs;
import org.myjaphoo.model.externalPrograms.ExternalPrograms;
import org.myjaphoo.model.logic.exec.Exec;
import org.myjaphoo.model.logic.imp.MovieExtractionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * A movie attribute provider that uses mplayer.
 * User: lang
 * Date: 08.04.13
 * Time: 17:07
 */
public class MPlayerMovieAttributeProvider implements MovieAttributeProvider {

    public static final Logger LOGGER = LoggerFactory.getLogger(MPlayerMovieAttributeProvider.class.getName());

    @Override
    public MovieAttrs extract(File file) {
        try {
            ArrayList<String> args = new ArrayList<String>();
            args.add("-identify");
            args.add(file.getAbsolutePath());
            args.add("-ao");
            args.add("null");
            args.add("-vo");
            args.add("null");
            args.add("-frames");
            args.add("0");
            //mplayer -identify "$1" -ao null -vo null -frames 0 2>/dev/null | grep ^ID_
            Exec.Result result = ExternalPrograms.MPLAYER.execAndWait(args);

            MovieExtractionInfo info = parseResult(result.outputStream);
            return info.genMovieAttrs();
        } catch (Exception ex) {
            LOGGER.error("error", ex);
            throw new RuntimeException("movie info extraction failed!", ex);
        }
    }

    @Override
    public boolean isAvailable() {
        return ExternalPrograms.MPLAYER.exists();
    }

    @Override
    public String getDescr() {
        return "mplayer via commandline";
    }

    private MovieExtractionInfo parseResult(String outputStream) throws IOException {
        System.out.println(outputStream);
        MovieExtractionInfo info = new MovieExtractionInfo(outputStream);

        StringReader reader = new StringReader(outputStream);
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("ID_")) {
                parseIdRow(info, line);
            }
        }
        return info;
    }

    private void parseIdRow(MovieExtractionInfo info, String line) {
        String[] pair = StringUtils.split(line, "=");
        if (pair != null && pair.length == 2) {
            System.out.println("found value : " + pair[0] + " = " + pair[1]);
            info.put(pair[0], pair[1]);
        }

    }
}
