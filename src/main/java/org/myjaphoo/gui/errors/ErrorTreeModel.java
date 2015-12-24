/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.errors;

import org.myjaphoo.gui.util.WrappedNode;
import org.myjaphoo.gui.util.WrappedNodeTreeModel;

/**
 *
 * @author mla
 */
public class ErrorTreeModel extends WrappedNodeTreeModel<ErrorTreeNode>  {

    private static final java.util.ResourceBundle BUNDLE = java.util.ResourceBundle.getBundle("org/myjaphoo/gui/errors/resources/ErrorPanel");
    
    private static String[] COLUMNS = new String[]{BUNDLE.getString("MESSAGE"), BUNDLE.getString("ROW") };

    public ErrorTreeModel(ErrorTreeNode root, boolean flat) {
        super(root, false, "",  flat, COLUMNS);
    }
    
    @Override
    public Object getValueAt(Object node, int column) {
        ErrorTreeNode mtn = ((WrappedNode<ErrorTreeNode>) node).getRef();
        switch (column) {
            case 0:
                return mtn;
            case 1:
                return mtn.getRow();
        }
        return null;
    }



    @Override
    protected boolean match(ErrorTreeNode tok, String typedText) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * overriden to display only nodes on the second level, but not the
     * nodes which are groups.
     */ 
    @Override
    public boolean shouldDisplayNodeInFlatMode(ErrorTreeNode node, int level) {
        return level >=1;
    }

    
}
