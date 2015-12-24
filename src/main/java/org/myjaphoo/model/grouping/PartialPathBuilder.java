/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.grouping;

import org.myjaphoo.model.filterparser.expr.JoinedDataRow;
import org.myjaphoo.model.filterparser.processing.ProcessingRequirementInformation;


/**
 * grouping interface that creates a part of a full grouping structure for a
 * data row entry.
 *
 * @author lang
 */
public interface PartialPathBuilder extends ProcessingRequirementInformation {

    Path[] getPaths(JoinedDataRow row);

    public void preProcess(GroupingExecutionContext context);

}
