/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.movietree.grouping;

import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.MovieNode;
import org.myjaphoo.gui.movietree.AbstractMovieTreeNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.model.filterparser.ExecutionContext;
import org.myjaphoo.model.filterparser.expr.Expression;
import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.values.Value;
import org.myjaphoo.model.groupbyparser.expr.Aggregation;
import org.myjaphoo.model.groupbyparser.expr.AggregatedExpression;
import org.myjaphoo.model.grouping.GroupingExecutionContext;
import org.myjaphoo.model.grouping.PartialPathBuilder;
import org.myjaphoo.model.grouping.Path;

import java.util.Iterator;
import java.util.List;


/**
 * Ein abstrakter grouper, der die Gruppierung durch angegebene Pfade durchführt.
 * Der Pfad wird durch ein String-array angegeben. Jeder Pfad muss für sich
 * natürlich eindeutig sein.
 * @author mla
 */
public class PartialGrouper extends AbstractByPathGrouper {

    private PartialPathBuilder[] groupies;

    private String text;

    private AggregatedExpression havingClause;

    private List<Aggregation> aggregations;

    public PartialGrouper(List<PartialPathBuilder> groupies, AggregatedExpression aggregatedExpression, List<Aggregation> aggregations) {
        this(aggregatedExpression, aggregations, groupies.toArray(new PartialPathBuilder[groupies.size()]));
    }

    public PartialGrouper(AggregatedExpression aggregatedExpression, List<Aggregation> aggregations,PartialPathBuilder... groupies) {
        this.havingClause = aggregatedExpression;
        this.groupies = groupies;
        this.aggregations = aggregations;
    }

    @Override
    public void preProcess(GroupingExecutionContext context) {
        super.preProcess(context);
        for (PartialPathBuilder groupie : getGroupies()) {
            groupie.preProcess(context);
        }
    }


    @Override
    public Path[] getPaths(JoinedDataRow row) {

        Path[] createdPaths = new Path[0];
        for (PartialPathBuilder groupie : getGroupies()) {
            Path[] paths = groupie.getPaths(row);
            createdPaths = multiply(createdPaths, paths);
        }
        return createdPaths;
    }

    private Path[] multiply(Path[] paths, Path[] createdPaths) {
        if (paths == null || createdPaths == null) {
            return null;
        }
        if (paths.length == 0) {
            // this is the first multiply, no other pathes exists, so just return the created paths:
            return createdPaths;
        }
        return cartesianMultiply(paths, createdPaths);

    }

    private Path[] cartesianMultiply(Path[] paths, Path[] createdPaths) {
        Path[] result = new Path[paths.length * createdPaths.length];
        int i = 0;
        for (Path p1 : paths) {
            for (Path p2 : createdPaths) {
                result[i] = new Path(p1, p2);
                i++;
            }
        }
        return result;
    }

    /**
     * @return the groupies
     */
    public PartialPathBuilder[] getGroupies() {
        return groupies;
    }

    @Override
    public boolean needsTagRelation() {
        for (PartialPathBuilder builder : groupies) {
            if (builder.needsTagRelation()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean needsMetaTagRelation() {
        for (PartialPathBuilder builder : groupies) {
            if (builder.needsMetaTagRelation()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void aggregate(MovieStructureNode root) {
        if (aggregations != null) {
            aggregateOnAllStructureNodes(root);
        }
    }

    @Override
    public void pruneByHavingClause(MovieStructureNode root) {
        if (havingClause != null) {
            pruneByHavingClause(root, havingClause);
        }
    }

    private void aggregateOnAllStructureNodes(MovieStructureNode node) {
        // recursively aggregate on all levels of structure nodes:
        for (AbstractMovieTreeNode child: node.getChildren()) {
            if (child instanceof MovieStructureNode) {
                aggregateOnAllStructureNodes((MovieStructureNode) child);
            }
        }

        // calc aggregated values for this level and node:
        for (Aggregation aggregation: aggregations) {
            Value result = calcAggregation(node, aggregation.getExpr());
            node.putAggregatedValue(aggregation.getExpr().getDisplayExprTxt(), new Double(result.asLong()));
        }
    }

    private Value calcAggregation(MovieStructureNode node, AggregatedExpression expression) {
        // new context for this aggregation (agg-functions hold their status there).
        ExecutionContext context = new ExecutionContext();
        // fill the aggregations:
        for (AbstractTreeNode child : node.getChildren()) {
            if (child instanceof MovieNode) {
                JoinedDataRow row = ((MovieNode) child).getRow();
                expression.populateAggregations(context, row);
            }
        }
        // "close" the aggregation mode now:
        context.closeAggregations();
        // and evaluate the clause with the previously aggregated values:
        return expression.evaluate(context, null);
    }


    private void pruneByHavingClause(MovieStructureNode node, AggregatedExpression aggregatedExpression) {
        Iterator<AbstractMovieTreeNode> afterDeleting = node.getChildren().iterator();
        while (afterDeleting.hasNext()) {
            AbstractMovieTreeNode child = afterDeleting.next();
            if (child instanceof MovieStructureNode) {
                pruneByHavingClause((MovieStructureNode) child);
                // if the child has after pruning no more children, then we can remove it:
                if (child.getChildCount() == 0) {
                    afterDeleting.remove();
                    child.setParent(null);
                }
            }
        }

        // calc aggregated values and evaluate having condition:
        if (isFalse(calcAggregation(node, aggregatedExpression))) {
            // prune all this child nodes:
            Iterator<AbstractMovieTreeNode> iterator = node.getChildren().iterator();
            while (iterator.hasNext()) {
                AbstractMovieTreeNode child = iterator.next();
                if (child instanceof MovieNode) {
                    iterator.remove();
                    child.setParent(null);
                }
            }
        }
    }

    private boolean isFalse(Value value) {
        return !value.asBool();
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
