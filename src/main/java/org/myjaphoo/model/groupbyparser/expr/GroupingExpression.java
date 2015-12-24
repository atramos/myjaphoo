/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.groupbyparser.expr;

import org.myjaphoo.model.grouping.PartialPathBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mla
 */
public class GroupingExpression {

    private ArrayList<Grouping> groupings = new ArrayList<Grouping>();

    private ArrayList<Aggregation> aggregations = new ArrayList<>();

    private String text;

    private AggregatedExpression havingClause;

    public void addGrouping(Grouping grouping) {
        getGroupings().add(grouping);
    }

    public void addAggregation(Aggregation aggregation) {
        getAggregations().add(aggregation);
    }

    /**
     * @return the groupings
     */
    public ArrayList<Grouping> getGroupings() {
        return groupings;
    }


    public List<PartialPathBuilder> createGrouper() {
        List<PartialPathBuilder> groupies = new ArrayList<PartialPathBuilder>();
        for (Grouping grouping : groupings) {
            groupies.add(grouping.createPartialPathBuilder());
        }

        return groupies;
    }

    public ArrayList<Aggregation> getAggregations() {
        return aggregations;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public AggregatedExpression getHavingClause() {
        return havingClause;
    }

    public void setHavingClause(AggregatedExpression havingClause) {
        this.havingClause = havingClause;
    }
}
