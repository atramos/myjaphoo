/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.myjaphoo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.movietree.grouping.GroupAlgorithm;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.cache.EntityCacheActor;
import org.myjaphoo.model.db.DataView;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.dbcompare.DatabaseComparison;
import org.myjaphoo.model.filterparser.FilterParser;
import org.myjaphoo.model.filterparser.ParserException;
import org.myjaphoo.model.filterparser.expr.AbstractBoolExpression;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.Expressions;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.DefaultProcessingRequirementInformation;
import org.myjaphoo.model.filterparser.processing.FilterAndGroupingProcessor;
import org.myjaphoo.model.filterparser.processing.ProcessingRequirementInformation;
import org.myjaphoo.model.filterparser.syntaxtree.StringLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * experimentelle optimierte variante, die movies im speicher hält (zusammen mit
 * assignden tokens), u. damit weitere zugriffe verhindert. Ggf. müssen daraufhin
 * db- aktionen auf die gecachten objekte vorher mit einem merge eingeleitet
 * werden.
 *
 * @author lang
 */
public class MovieInMemoryFilter implements MovieFilterInterface {

    public static final Logger LOGGER = LoggerFactory.getLogger(MovieInMemoryFilter.class.getName());

    public static final Expressions.Predicate FIND_LITERALS = new Expressions.Predicate() {
        @Override
        public boolean eval(Expression expr) {
            // stop on first level of subst expr. to not scan into nested substitutions
            return !(expr instanceof StringLiteral);
        }
    };

    private List<String> usedLiterals = new ArrayList<>();

    @Override
    public List<JoinedDataRow> loadFilteredEntries(DataView dataView, List<? extends GroupAlgorithm> groupingAlgorithm) throws ParserException {

        // check prefilters and combine them if necessary with the main filter:
        // pre-parse them to get possible parser errors first:
        AbstractBoolExpression preFilterExpr = parseExpr(dataView.getPreFilterExpression());

        // and now parse the main filter separate:
        AbstractBoolExpression mainFilterExpr = parseExpr(dataView.getFilterExpression());

        usedLiterals.clear();
        List<StringLiteral> literals = Expressions.findAllExprParts(preFilterExpr, StringLiteral.class, FIND_LITERALS);
        for (StringLiteral literal : literals) {
            usedLiterals.add(literal.getLiteral());
        }

        literals = Expressions.findAllExprParts(mainFilterExpr, StringLiteral.class, FIND_LITERALS);
        for (StringLiteral literal : literals) {
            usedLiterals.add(literal.getLiteral());
        }

        // if necessary combine them:
        String combinedExpr = dataView.getCombinedFilterExpression();

        return filter(combinedExpr, combinedProcessingInstructions(groupingAlgorithm));
    }

    private ProcessingRequirementInformation combinedProcessingInstructions(List<? extends ProcessingRequirementInformation> groupingAlgorithm) {
        DefaultProcessingRequirementInformation pri = new DefaultProcessingRequirementInformation();
        for (ProcessingRequirementInformation p: groupingAlgorithm) {
            pri.join(p);
        }
        return pri;
    }

    public static List<JoinedDataRow> filter(String filterpattern, ProcessingRequirementInformation reqForGroupings) throws ParserException {
        AbstractBoolExpression expr = parseExpr(filterpattern);

        Collection<MovieEntry> allMovies = CacheManager.getCacheActor().getImmutableModel().getMovies();
        final EntityCacheActor comparisonDBCacheActor = DatabaseComparison.getInstance().getCacheActor();

        Collection<MovieEntry> comparisonMovies = comparisonDBCacheActor != null? comparisonDBCacheActor.getImmutableModel().getMovies() : null;

        StopWatch watch = new StopWatch();
        watch.start();
        ArrayList<JoinedDataRow> resultList = FilterAndGroupingProcessor.filterEntries(allMovies, comparisonMovies, expr, reqForGroupings);
        watch.stop();
        LOGGER.info("finished creating filtered entry list: duration:" + watch.toString()); //NOI18N

        return resultList;
    }


    public static AbstractBoolExpression parseExpr(String strExpr) {
        AbstractBoolExpression expr = null;
        if (!StringUtils.isEmpty(strExpr)) {
            FilterParser parser = new FilterParser(MainApplicationController.getInstance().createSubstitutions());
            expr = parser.parse(strExpr);
        }
        return expr;
    }

    public List<String> getUsedLiterals() {
        return usedLiterals;
    }
}
