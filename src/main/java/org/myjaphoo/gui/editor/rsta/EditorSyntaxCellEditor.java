/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.editor.rsta;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.table.*;
import java.util.EventObject;
import javax.swing.tree.*;
import java.io.Serializable;
import javax.swing.JTree;
/**
 * editor for filter expressions in the bookmark table.
 * Code copied from JDK´s DefaultCellEditor and adapted for rsynctextarea.
 * 
 * @author lang
 */
public class EditorSyntaxCellEditor  extends AbstractCellEditor 
    implements TableCellEditor, TreeCellEditor { 

//
//  Instance Variables
//

    /** The Swing component being edited. */
    private RSyntaxTextArea editorComponent = new RSyntaxTextArea();
   
    /**
     * The delegate class which handles all methods sent from the
     * <code>CellEditor</code>.
     */
    protected EditorDelegate delegate;
    /**
     * An integer specifying the number of clicks needed to start editing.
     * Even if <code>clickCountToStart</code> is defined as zero, it
     * will not initiate until a click occurs.
     */
    protected int clickCountToStart = 1;

//
//  Constructors
//

    /**
     * Constructs a <code>DefaultCellEditor</code> that uses a text field.
     *
     * @param textField  a <code>JTextField</code> object
     */
    public EditorSyntaxCellEditor() {
        RSTAHelper.initAsFilterEditor(editorComponent);
	this.clickCountToStart = 2;
        delegate = new EditorDelegate() {
            public void setValue(Object value) {
		editorComponent.setText((value != null) ? value.toString() : "");
            }

	    public Object getCellEditorValue() {
		return editorComponent.getText();
	    }
        };
	//editorComponent..addActionListener(delegate);
    }

  

    /**
     * Returns a reference to the editor component.
     *
     * @return the editor <code>Component</code>
     */
    public Component getComponent() {
	return editorComponent;
    }

//
//  Modifying
//

    /**
     * Specifies the number of clicks needed to start editing.
     *
     * @param count  an int specifying the number of clicks needed to start editing
     * @see #getClickCountToStart
     */
    public void setClickCountToStart(int count) {
	clickCountToStart = count;
    }

    /**
     * Returns the number of clicks needed to start editing.
     * @return the number of clicks needed to start editing
     */
    public int getClickCountToStart() {
	return clickCountToStart;
    }

//
//  Override the implementations of the superclass, forwarding all methods 
//  from the CellEditor interface to our delegate. 
//

    /**
     * Forwards the message from the <code>CellEditor</code> to
     * the <code>delegate</code>.
     * @see EditorDelegate#getCellEditorValue
     */
    public Object getCellEditorValue() {
        return delegate.getCellEditorValue();
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to
     * the <code>delegate</code>.
     * @see EditorDelegate#isCellEditable(EventObject)
     */
    public boolean isCellEditable(EventObject anEvent) { 
	return delegate.isCellEditable(anEvent); 
    }
    
    /**
     * Forwards the message from the <code>CellEditor</code> to
     * the <code>delegate</code>.
     * @see EditorDelegate#shouldSelectCell(EventObject)
     */
    public boolean shouldSelectCell(EventObject anEvent) { 
	return delegate.shouldSelectCell(anEvent); 
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to
     * the <code>delegate</code>.
     * @see EditorDelegate#stopCellEditing
     */
    public boolean stopCellEditing() {
	return delegate.stopCellEditing();
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to
     * the <code>delegate</code>.
     * @see EditorDelegate#cancelCellEditing
     */
    public void cancelCellEditing() {
	delegate.cancelCellEditing();
    }

//
//  Implementing the TreeCellEditor Interface
//

    /** Implements the <code>TreeCellEditor</code> interface. */
    public Component getTreeCellEditorComponent(JTree tree, Object value,
						boolean isSelected,
						boolean expanded,
						boolean leaf, int row) {
	String         stringValue = tree.convertValueToText(value, isSelected,
					    expanded, leaf, row, false);

	delegate.setValue(stringValue);
	return editorComponent;
    }

//
//  Implementing the CellEditor Interface
//
    /** Implements the <code>TableCellEditor</code> interface. */
    public Component getTableCellEditorComponent(JTable table, Object value,
						 boolean isSelected,
						 int row, int column) {
        delegate.setValue(value);
	return editorComponent;
    }


//
//  Protected EditorDelegate class
//

    /**
     * The protected <code>EditorDelegate</code> class.
     */
    protected class EditorDelegate implements ActionListener, ItemListener, Serializable {

        /**  The value of this cell. */
        protected Object value;

       /**
        * Returns the value of this cell. 
        * @return the value of this cell
        */
        public Object getCellEditorValue() {
            return value;
        }

       /**
        * Sets the value of this cell. 
        * @param value the new value of this cell
        */
    	public void setValue(Object value) { 
	    this.value = value; 
	}

       /**
        * Returns true if <code>anEvent</code> is <b>not</b> a
        * <code>MouseEvent</code>.  Otherwise, it returns true
        * if the necessary number of clicks have occurred, and
        * returns false otherwise.
        *
        * @param   anEvent         the event
        * @return  true  if cell is ready for editing, false otherwise
        * @see #setClickCountToStart
        * @see #shouldSelectCell
        */
        public boolean isCellEditable(EventObject anEvent) {
	    if (anEvent instanceof MouseEvent) { 
		return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
	    }
	    return true;
	}
    	
       /**
        * Returns true to indicate that the editing cell may
        * be selected.
        *
        * @param   anEvent         the event
        * @return  true 
        * @see #isCellEditable
        */
        public boolean shouldSelectCell(EventObject anEvent) { 
            return true; 
        }

       /**
        * Returns true to indicate that editing has begun.
        *
        * @param anEvent          the event
        */
        public boolean startCellEditing(EventObject anEvent) {
	    return true;
	}

       /**
        * Stops editing and
        * returns true to indicate that editing has stopped.
        * This method calls <code>fireEditingStopped</code>.
        *
        * @return  true 
        */
        public boolean stopCellEditing() { 
	    fireEditingStopped(); 
	    return true;
	}

       /**
        * Cancels editing.  This method calls <code>fireEditingCanceled</code>.
        */
       public void cancelCellEditing() { 
	   fireEditingCanceled(); 
       }

       /**
        * When an action is performed, editing is ended.
        * @param e the action event
        * @see #stopCellEditing
        */
        public void actionPerformed(ActionEvent e) {
            EditorSyntaxCellEditor.this.stopCellEditing();
	}

       /**
        * When an item's state changes, editing is ended.
        * @param e the action event
        * @see #stopCellEditing
        */
        public void itemStateChanged(ItemEvent e) {
	    EditorSyntaxCellEditor.this.stopCellEditing();
	}
    }
}
