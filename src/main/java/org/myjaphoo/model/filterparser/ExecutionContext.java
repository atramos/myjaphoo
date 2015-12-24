/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.myjaphoo.model.filterparser;

import org.myjaphoo.model.filterparser.expr.FunctionCall;
import org.myjaphoo.model.filterparser.functions.NumberAggregateFunction;
import org.myjaphoo.model.groovyparser.GroovyFilterBaseClass;
import org.myjaphoo.model.groovyparser.GroovyFilterParser;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Contains information that is necessary for execution of filtering and
 * grouping expressions. This class holds states during a filter process
 * (necessary e.g. for functions that hold state). Each new filtering process
 * should create a new execution context.
 *
 * @author mla
 */
public class ExecutionContext {


    private IdentityHashMap<FunctionCall, Long> aggregatedValues = new IdentityHashMap<>();

    private boolean aggregationMode = true;

    private Map<String, Integer> seqMap = new HashMap<String, Integer>();
    private int seqCounter = 0;

    /**
     * empty groovy filter base class to access user defined methods of the expando meta class
     */
    private GroovyFilterBaseClass baseClass = new GroovyFilterBaseClass() {

        /** context variables, that could be used by scripts to store variables during filter run. */
        private Map<String, Object> contextVars = new HashMap<>();

        @Override
        public Object run() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public Map<String, Object> getContextVars() {
            return contextVars;
        }

    };

    /**
     * wrapper around the script
     */
    private GroovyFilterParser.GroovyScriptWrapper scriptWrapper = new GroovyFilterParser.GroovyScriptWrapper(baseClass);


    public String getSequenceNumber(String s) {
        Integer seq = seqMap.get(s);
        if (seq != null) {
            return seq.toString();
        } else {
            seq = seqCounter;
            seqCounter++;
            seqMap.put(s, seq);
            return seq.toString();
        }
    }

    public GroovyFilterParser.GroovyScriptWrapper getScriptWrapper() {
        return scriptWrapper;
    }


    public Long getAggregatedValue(FunctionCall call) {
        Long val = aggregatedValues.get(call);
        return val;
    }

    public void setAggregatedValue(FunctionCall call, Long val) {
        aggregatedValues.put(call, val);
    }

    public void closeAggregations() {
         aggregationMode = false;
    }

    public boolean isAggregationMode() {
        return aggregationMode;
    }
}
