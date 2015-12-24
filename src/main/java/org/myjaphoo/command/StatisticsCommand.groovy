package org.myjaphoo.command

import org.codehaus.groovy.tools.shell.CommandSupport
import org.codehaus.groovy.tools.shell.Shell
import org.myjaphoo.MyjaphooAppPrefs
import org.myjaphoo.gui.util.Utils
import org.myjaphoo.model.DuplicateHashMap
import org.myjaphoo.model.WmEntitySet
import org.myjaphoo.model.cache.CacheManager
import org.myjaphoo.model.db.MovieEntry
import org.myjaphoo.model.db.Token
import org.myjaphoo.model.logic.MyjaphooDB

/**
 * StatisticsCommand
 *
 * @author mla
 * @version $Id$
 */
public class StatisticsCommand extends CommandSupport {

    StatisticsCommand(final Shell shell) {
        super(shell, 'statistics', '\\stats')
    }

    @Override
    public Object execute(List<String> args) {
        WmEntitySet model = CacheManager.getCacheActor().getImmutableModel();
        int numMovies = model.getMovies().size();
        long size = 0;

        for (MovieEntry entry : model.getMovies()) {
            size += entry.getFileLength();
        }
        List<Token> allTokens = model.tokenSet.asList();

        DuplicateHashMap dupMap = model.getDupHashMap();

        return """
Database: "${MyjaphooAppPrefs.PRF_DATABASENAME.getVal()}" ${MyjaphooDB.singleInstance().getConfiguration().getFilledConnectionUrl()}
Movies: $numMovies, size: ${Utils.humanReadableByteCount(size)}
Tokens: ${allTokens.size()} with ${calcTokenAssigments(allTokens)} assignments
Duplicates: ${dupMap.calcDuplicationCount()}, wasting ${Utils.humanReadableByteCount(dupMap.calcWastedMem())}
               """

    }

    private int calcTokenAssigments(List<Token> allTokens) {
        int count = 0;
        for (Token token : allTokens) {
            count += token.getMovieEntries().size();
        }
        return count;
    }

}
