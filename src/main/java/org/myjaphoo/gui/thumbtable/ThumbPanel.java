/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.gui.thumbtable;

import net.infonode.gui.icon.button.DropDownIcon;
import net.infonode.util.Direction;
import org.apache.commons.lang.time.StopWatch;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.mlsoft.eventbus.GlobalBus;
import org.mlsoft.eventbus.Subscribe;
import org.mlsoft.swing.ComponentForRestCreator;
import org.mlsoft.swing.JPopupMenuButton;
import org.mlsoft.swing.ResizeLayout;
import org.mlsoft.swing.bcb.BreadCrumbBar;
import org.mlsoft.swing.bcb.BreadCrumbClickEvent;
import org.myjaphoo.MovieNode;
import org.myjaphoo.ThumbDisplayFilterResult;
import org.myjaphoo.gui.OrderType;
import org.myjaphoo.gui.ThumbTypeDisplayMode;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.thumbtable.groupedthumbs.GroupedThumbView;
import org.myjaphoo.gui.util.ColorizationType;
import org.myjaphoo.gui.util.Tables;
import org.myjaphoo.model.ThumbMode;
import org.myjaphoo.model.cache.ChangeSet;
import org.myjaphoo.model.cache.events.MoviesChangedEvent;
import org.myjaphoo.model.cache.events.MoviesRemovedEvent;
import org.myjaphoo.model.cache.events.TagsAssignedEvent;
import org.myjaphoo.model.cache.events.TagsUnassigendEvent;
import org.myjaphoo.util.Filtering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
/*
 * ThumbPanel.java
 *
 * Created on 25.10.2009, 18:38:06
 */

/**
 *
 * @author mla
 */
public class ThumbPanel extends javax.swing.JPanel {

    private final static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/thumbtable/resources/ThumbPanel");
    private ThumbPanelController controller;
    public static final Logger LOGGER = LoggerFactory.getLogger(ThumbPanel.class.getName());
    private EnumComboBoxModel<ColorizationType> comboModelColorizationType =
            new EnumComboBoxModel<ColorizationType>(ColorizationType.class);
    private EnumComboBoxModel<OrderType> comboModelOrderType =
            new EnumComboBoxModel<OrderType>(OrderType.class);
    private EnumComboBoxModel<ThumbTypeDisplayMode> comboModelTDM =
            new EnumComboBoxModel<ThumbTypeDisplayMode>(ThumbTypeDisplayMode.class);

    /** need to check, if we are in between tab event handling. */
    private boolean inTabStateSettingProcess = false;
    
    private ComponentForRestCreator buttonComponentRestCreator = new ComponentForRestCreator() {

        @Override
        public JComponent createOtherComponent(Vector<Component> theRest) {
            DropDownIcon i = new DropDownIcon(12, Direction.DOWN);
            JPopupMenuButton restComp = new JPopupMenuButton(i);
            
            JPopupMenu restm = new JPopupMenu();
            restComp.setPopupmenu(restm);
        
            for (Component c : theRest) {
                // we need to copy the items because we insert them into another component:
                final JButton item = (JButton) c;
                JMenuItem restItem = new JMenuItem();
                restItem.setText(item.getText());
                restItem.setIcon(item.getIcon());
                restItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        item.doClick();
                    }
                });
                restm.add(restItem);
            }
            return restComp;
        }
    };
    
    
    /** Creates new form ThumbPanel */
    public ThumbPanel(final ThumbPanelController controller) {
        this.controller = controller;
        initComponents();

        jToolBarLastActions.setLayout(new ResizeLayout(buttonComponentRestCreator));
        // need to  update ui after setting layout in toolbars:
        jToolBarLastActions.updateUI();
        
        ListSelectionListener listener = new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                LOGGER.debug("event: ThumbPanel.ListSelectionChanged");
                ArrayList<MovieNode> currSelectedMovies = Filtering.filterMovieNodes(getAllSelectedMovieNodes());
                controller.currSelectionChanged(currSelectedMovies);
                updateInfoLabel(-1, currSelectedMovies.size());
            }
        };
        // selection listener anmelden fÃ¼r info-panel:
        jTable1.getSelectionModel().addListSelectionListener(listener);
        jTable1.getColumnModel().getSelectionModel().addListSelectionListener(listener);
        jTable1.addMouseListener(new PopupMouseListener(controller, (ThumbDisplayingComponent) jTable1));

        jList1.getSelectionModel().addListSelectionListener(listener);
        jList1.addMouseListener(new PopupMouseListener(controller, (ThumbDisplayingComponent) jList1));

        // additional mouse listener for double click in the jlist thumb view:
        jList1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                LOGGER.debug("event: ThumbPanel.mouseClicked");
                if (evt.getClickCount() == 2) {// double click
                    if (evt.isControlDown()) {
                        ArrayList<AbstractLeafNode> nodes = getAllSelectedNodesFromExtendedTableView();
                        controller.playMovies(nodes);
                    } else {
                        Object element = jList1.getModel().getElementAt(jList1.locationToIndex(evt.getPoint()));
                        if (element instanceof AbstractLeafNode) {
                            controller.playMovie((AbstractLeafNode) element);
                        }

                    }
                }
            }
        });

        jXTreeTable1.getSelectionModel().addListSelectionListener(listener);
        jXTreeTable1.getColumnModel().getSelectionModel().addListSelectionListener(listener);
        jXTreeTable1.addMouseListener(
                new PopupMouseListener(controller, (ThumbDisplayingComponent) jXTreeTable1));

        SpinnerNumberModel sm = new SpinnerNumberModel(100, 10, 600, 20);

        jSpinner2.setModel(sm);
        
        final BreadCrumbBar bcb = (BreadCrumbBar) jBreadCrumbBar;
        bcb.setRenderer(new BreadCrumbTreeCellRenderer());

        GlobalBus.bus.register(this);

        jToggleButtonThumbView.setSelected(!controller.isCardView());
        jToggleButtonThumbCard.setSelected(controller.isCardView());
        jComboBoxThumbTypeMode.setModel(comboModelTDM);
    }

    @Subscribe(onETD = true)
    public void breadCrumbClicked(BreadCrumbClickEvent bcce) {
        LOGGER.debug("event: ThumbPanel.breadCrumbClicked");
        controller.breadCrumbPathChanged(bcce.treePath);
        refillThumbView(controller.getThumbsModel());
    }
    
    
    @Subscribe(onETD = true)
    public void moviesHaveBeenRemovedEvent(MoviesRemovedEvent mre) {
        LOGGER.debug("event: ThumbPanel.moviesHaveBeenRemovedEvent");
        StopWatch w = new StopWatch();
        w.start();
        if (controller.getThumbMode() == ThumbMode.EXTENDED_TABLEVIEW) {
            ((DetailedThumbTable) jTable1).updateRemovedNodes(mre);
        } else if (controller.getThumbMode() == ThumbMode.STRIPES) {
            ((StripeThumbTable) jXTreeTable1).updateRemovedNodes(mre);
        } else {
            ((ThumbTable) jList1).updateRemovedNodes(mre);
        }
        w.stop();
        LOGGER.debug("updated movie remove changes in thumbmodel, duration: " + w.toString()); //NOI18N
    }    
    
    @Subscribe(onETD = true)
    public void moviesChanged(MoviesChangedEvent e) {
        LOGGER.debug("event: ThumbPanel.moviesChanged");
        updateMovieNodes(e);
    }

    @Subscribe(onETD = true)
    public void tagsAssigned(TagsAssignedEvent e) {
        LOGGER.debug("event: ThumbPanel.tagsAssigned");
        updateMovieNodes(e);
    }

    @Subscribe(onETD = true)
    public void tagsUnassigned(TagsUnassigendEvent e) {
        LOGGER.debug("event: ThumbPanel.tagsUnassigned");
        updateMovieNodes(e);
    }

    private void updateMovieNodes(ChangeSet e) {
        StopWatch w = new StopWatch();
        w.start();
        if (controller.getThumbMode() == ThumbMode.EXTENDED_TABLEVIEW) {
            ((DetailedThumbTable) jTable1).updateNodes(e);
        } else if (controller.getThumbMode() == ThumbMode.STRIPES) {
            ((StripeThumbTable) jXTreeTable1).updateNodes(e);
        } else {
            ((ThumbTable) jList1).updateNodes(e);
        }
        w.stop();
        LOGGER.debug("updated changes in thumbmodel, duration: " + w.toString()); //NOI18N
    }
    private int maxLastActions = 8;
    private List<Action> lastActions = new ArrayList<Action>();

    public void addLastAction(Action action) {
        if (notYetAdded(action)) {
            // build button: more flexible: can show icon + text
            JButton button = new JButton(action);

            jToolBarLastActions.add(button);

            lastActions.add(action);
            if (lastActions.size() > maxLastActions) {
                jToolBarLastActions.remove(0);
                lastActions.remove(0);
            }
            jToolBarLastActions.validate();
        }
    }

    private boolean notYetAdded(Action action) {
        // search the action by name:
        String name = (String) action.getValue(Action.NAME);
        for (Action knownAction : lastActions) {
            String nameOfKnownAction = (String) knownAction.getValue(Action.NAME);
            if (name.equals(nameOfKnownAction)) {
                return false;
            }
        }
        return true;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jCheckBox2 = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabelInfo = new javax.swing.JLabel();
        jToggleButtonThumbView = new javax.swing.JToggleButton();
        jToggleButtonThumbCard = new javax.swing.JToggleButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jComboBoxThumbTypeMode = new javax.swing.JComboBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new org.myjaphoo.gui.thumbtable.DetailedThumbTable(controller);
        jXPanel1 = new org.jdesktop.swingx.JXPanel();
        jToolBar4 = new javax.swing.JToolBar();
        jComboBoxColorType = new javax.swing.JComboBox();
        jComboBoxOrdering = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new org.myjaphoo.gui.thumbtable.ThumbTable(controller);
        jXPanel2 = new org.jdesktop.swingx.JXPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        jXTreeTable1 = new org.myjaphoo.gui.thumbtable.StripeThumbTable(controller);
        jToolBarLastActions = new javax.swing.JToolBar();
        jBreadCrumbBar = new org.mlsoft.swing.bcb.BreadCrumbBar();

        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar2.setRollover(true);
        jToolBar2.setName("jToolBar2"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.myjaphoo.MyjaphooApp.class).getContext().getResourceMap(ThumbPanel.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jToolBar2.add(jLabel1);

        jSpinner2.setName("jSpinner2"); // NOI18N
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner2StateChanged(evt);
            }
        });
        jToolBar2.add(jSpinner2);

        jCheckBox1.setText(resourceMap.getString("jCheckBox1.text")); // NOI18N
        jCheckBox1.setFocusable(false);
        jCheckBox1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jCheckBox1.setName("jCheckBox1"); // NOI18N
        jCheckBox1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jCheckBox1);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar2.add(jSeparator1);

        jCheckBox2.setText(resourceMap.getString("jCheckBox2.text")); // NOI18N
        jCheckBox2.setFocusable(false);
        jCheckBox2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBox2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jCheckBox2.setName("jCheckBox2"); // NOI18N
        jCheckBox2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jToolBar2.add(jCheckBox2);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar2.add(jSeparator2);

        jLabelInfo.setText(resourceMap.getString("jLabelInfo.text")); // NOI18N
        jLabelInfo.setName("jLabelInfo"); // NOI18N
        jToolBar2.add(jLabelInfo);

        jToggleButtonThumbView.setIcon(resourceMap.getIcon("jToggleButtonThumbView.icon")); // NOI18N
        jToggleButtonThumbView.setText(resourceMap.getString("jToggleButtonThumbView.text")); // NOI18N
        jToggleButtonThumbView.setToolTipText(resourceMap.getString("jToggleButtonThumbView.toolTipText")); // NOI18N
        jToggleButtonThumbView.setFocusable(false);
        jToggleButtonThumbView.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonThumbView.setName("jToggleButtonThumbView"); // NOI18N
        jToggleButtonThumbView.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonThumbView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonThumbViewActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButtonThumbView);

        jToggleButtonThumbCard.setIcon(resourceMap.getIcon("jToggleButtonThumbCard.icon")); // NOI18N
        jToggleButtonThumbCard.setText(resourceMap.getString("jToggleButtonThumbCard.text")); // NOI18N
        jToggleButtonThumbCard.setToolTipText(resourceMap.getString("jToggleButtonThumbCard.toolTipText")); // NOI18N
        jToggleButtonThumbCard.setFocusable(false);
        jToggleButtonThumbCard.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButtonThumbCard.setName("jToggleButtonThumbCard"); // NOI18N
        jToggleButtonThumbCard.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButtonThumbCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonThumbCardActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButtonThumbCard);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar2.add(jSeparator3);

        jComboBoxThumbTypeMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxThumbTypeMode.setName("jComboBoxThumbTypeMode"); // NOI18N
        jComboBoxThumbTypeMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxThumbTypeModeActionPerformed(evt);
            }
        });
        jToolBar2.add(jComboBoxThumbTypeMode);

        jPanel1.add(jToolBar2, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setName("jTable1"); // NOI18N
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTabbedPane1.addTab(resourceMap.getString("jScrollPane1.TabConstraints.tabTitle"), jScrollPane1); // NOI18N

        jXPanel1.setName("jXPanel1"); // NOI18N
        jXPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar4.setRollover(true);
        jToolBar4.setName("jToolBar4"); // NOI18N

        jComboBoxColorType.setModel(comboModelColorizationType);
        jComboBoxColorType.setName("jComboBoxColorType"); // NOI18N
        jComboBoxColorType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxColorTypeActionPerformed(evt);
            }
        });
        jToolBar2.add(jComboBoxColorType);

        jComboBoxOrdering.setModel(comboModelOrderType);
        jComboBoxOrdering.setName("jComboBoxOrdering"); // NOI18N
        jComboBoxOrdering.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxOrderingActionPerformed(evt);
            }
        });
        jToolBar4.add(jComboBoxOrdering);

        jXPanel1.add(jToolBar4, java.awt.BorderLayout.NORTH);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setName("jList1"); // NOI18N
        jScrollPane3.setViewportView(jList1);

        jXPanel1.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(resourceMap.getString("jXPanel1.TabConstraints.tabTitle"), jXPanel1); // NOI18N

        jXPanel2.setName("jXPanel2"); // NOI18N
        jXPanel2.setLayout(new java.awt.BorderLayout());

        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N
        jXPanel2.add(jToolBar1, java.awt.BorderLayout.NORTH);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jXTreeTable1.setCellSelectionEnabled(true);
        jXTreeTable1.setName("jXTreeTable1"); // NOI18N
        jXTreeTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jXTreeTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jXTreeTable1);

        jXPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(resourceMap.getString("jXPanel2.TabConstraints.tabTitle"), jXPanel2); // NOI18N

        jPanel1.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jToolBarLastActions.setRollover(true);
        jToolBarLastActions.setName("Last Actions"); // NOI18N
        jPanel1.add(jToolBarLastActions, java.awt.BorderLayout.NORTH);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jBreadCrumbBar.setRollover(true);
        jBreadCrumbBar.setName("jBreadCrumbBar"); // NOI18N
        add(jBreadCrumbBar, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        LOGGER.debug("event: ThumbPanel.table1.mouseClicked");
        if (evt.getClickCount() == 2) {// double click
            if (evt.isControlDown()) {
                ArrayList<AbstractLeafNode> nodes = getAllSelectedNodesFromExtendedTableView();
                controller.playMovies(nodes);
            } else {
                int selrow = Tables.getModelSelectedRow(jTable1);
                AbstractLeafNode node = (AbstractLeafNode) jTable1.getModel().getValueAt(selrow, 0);
                controller.playMovie(node);
            }
        }
}//GEN-LAST:event_jTable1MouseClicked
    public ArrayList<AbstractLeafNode> getAllSelectedNodesFromExtendedTableView() {
        return ((DetailedThumbTable) jTable1).getAllSelectedNodes();
    }

    public ArrayList<AbstractLeafNode> getAllSelectedNodesFromAltThumbView() {
        return ((ThumbTable) jList1).getAllSelectedNodes();
    }

    public ThumbDisplayingComponent getCurrentDisplayingComponent() {
        switch (controller.getThumbMode()) {
            case ALTTHUMB:
                return (ThumbDisplayingComponent) jList1;
            case EXTENDED_TABLEVIEW:
                return (ThumbDisplayingComponent) jTable1;
            case STRIPES:
                return ((ThumbDisplayingComponent) jXTreeTable1);
            default:
                throw new RuntimeException("no view visible, which can select movie nodes!"); //NOI18N
        }
    }

    public ArrayList<AbstractLeafNode> getAllSelectedMovieNodes() {
        return getCurrentDisplayingComponent().getAllSelectedNodes();
    }

    public void resetModels() {
        LOGGER.debug("event: ThumbPanel.resetModels");
        ((DetailedThumbTable) jTable1).refreshModelData(new ArrayList<AbstractLeafNode>());
        //jTable1.setModel(new DefaultTableModel());
        jList1.setModel(new DefaultListModel());
        jXTreeTable1.setTreeTableModel(new DefaultTreeTableModel());
    }

    private void jComboBoxColorTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxColorTypeActionPerformed
        if (evt.getSource() == jComboBoxColorType) {
            LOGGER.debug("event: ThumbPanel.comboColorTypeClicked");
            ColorizationType selColorType = comboModelColorizationType.getSelectedItem();
            //System.out.println(selColorType.toString());
            controller.getColorization().setType(selColorType);
            refillThumbView(controller.getThumbsModel());
        }
}//GEN-LAST:event_jComboBoxColorTypeActionPerformed

    private void jComboBoxOrderingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxOrderingActionPerformed
        if (evt.getSource() == jComboBoxOrdering) {
            LOGGER.debug("event: ThumbPanel.comboOrderClicked");
            OrderType selOrderType = comboModelOrderType.getSelectedItem();
            //System.out.println(selColorType.toString());
            controller.setOrder(selOrderType);
            refillThumbView(controller.getThumbsModel());
        }
}//GEN-LAST:event_jComboBoxOrderingActionPerformed

    
    private void setTabbedState(ThumbMode mode) {
        inTabStateSettingProcess = true;
        try {
            switch (mode) {
                case EXTENDED_TABLEVIEW: jTabbedPane1.setSelectedIndex(0); break;
                case ALTTHUMB: jTabbedPane1.setSelectedIndex(1); break;
                case STRIPES: jTabbedPane1.setSelectedIndex(2); break;
                default:
                    throw new RuntimeException("internal error!");
            }
        } finally {
            inTabStateSettingProcess = false;
        }
    }
    
    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        if (!inTabStateSettingProcess) {
            LOGGER.debug("event: ThumbPanel.tabbedPaneClicked");
            final int selectedIndex = jTabbedPane1.getSelectedIndex();
            if (selectedIndex == 0) {
                controller.setThumbMode(ThumbMode.EXTENDED_TABLEVIEW);
            } else if (selectedIndex == 1) {
                controller.setThumbMode(ThumbMode.ALTTHUMB);
            } else {
                controller.setThumbMode(ThumbMode.STRIPES);
            }
            refillThumbView(controller.getThumbsModel());
        }
}//GEN-LAST:event_jTabbedPane1StateChanged

    private void jXTreeTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jXTreeTable1MouseClicked
        LOGGER.debug("event: ThumbPanel.treetable.mouseClicked");
        if (evt.getClickCount() == 2) {// double click
            if (evt.isControlDown()) {
                ArrayList<AbstractLeafNode> nodes = ((StripeThumbTable) jXTreeTable1).getAllSelectedNodes();
                controller.playMovies(nodes);
            } else {
                StripeThumbTable stt = ((StripeThumbTable) jXTreeTable1);
                if (stt.isfirstColClicked()) {
                    stt.handleFirstColClickedEvent();
                } else {
                    ArrayList<AbstractLeafNode> nodes = stt.getClickedStripeNodes();
                    controller.playMovies(nodes);
                }

            }
        }
    }//GEN-LAST:event_jXTreeTable1MouseClicked

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner2StateChanged
        LOGGER.debug("event: ThumbPanel.spinnerZoom clicked");
        int percentage = Integer.parseInt(jSpinner2.getModel().getValue().toString());
        controller.getZoom().setZoomPercentage(percentage);
        refillThumbView(controller.getThumbsModel());
    }//GEN-LAST:event_jSpinner2StateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        LOGGER.debug("event: ThumbPanel.checkBoxZoom clicked");
        controller.getZoom().setLoadDirectFromFile(!controller.getZoom().isLoadDirectFromFile());
        refillThumbView(controller.getThumbsModel());
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        LOGGER.debug("event: ThumbPanel.preventGroupingDups clicked");
        controller.setPreventGroupingDups(!controller.isPreventGroupingDups());
        refillThumbView(controller.getThumbsModel());
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jToggleButtonThumbViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonThumbViewActionPerformed
        if (evt.getSource() == jToggleButtonThumbView) {
            LOGGER.debug("event: ThumbPanel.thumbView action clicked");
            jToggleButtonThumbCard.setSelected(false);
            controller.setCardView(false);
            refillThumbView(controller.getThumbsModel());
        }
    }//GEN-LAST:event_jToggleButtonThumbViewActionPerformed

    private void jToggleButtonThumbCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonThumbCardActionPerformed
        if (evt.getSource() == jToggleButtonThumbCard) {
            LOGGER.debug("event: ThumbPanel.thumbCard view clicked");
            jToggleButtonThumbView.setSelected(false);
            controller.setCardView(true);
            refillThumbView(controller.getThumbsModel());
        }
    }//GEN-LAST:event_jToggleButtonThumbCardActionPerformed

    private void jComboBoxThumbTypeModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxThumbTypeModeActionPerformed
        if (evt.getSource() == jComboBoxThumbTypeMode) {
            controller.setThumbTypeDisplayMode(comboModelTDM.getSelectedItem());
            refillThumbView(controller.getThumbsModel());
        }
    }//GEN-LAST:event_jComboBoxThumbTypeModeActionPerformed
    public void refillThumbView(ThumbDisplayFilterResult thumbModelResult) {
        LOGGER.debug("event: ThumbPanel.refillThumbView");
        StopWatch w = new StopWatch();
        w.start();
        setTabbedState(controller.getThumbMode());
        if (controller.getThumbMode() == ThumbMode.EXTENDED_TABLEVIEW) {
            fillThumbTable(thumbModelResult.getPlainThumbList());
        } else if (controller.getThumbMode() == ThumbMode.STRIPES) {
            fillStripesThumbTable(thumbModelResult.getGroupedThumbView());
        } else {
            fillAlternativeThumbView(thumbModelResult.getPlainThumbList());
        }
        ((BreadCrumbBar) jBreadCrumbBar).setTreeModel(controller.getBreadCrumbTreeModel());
        ((BreadCrumbBar) jBreadCrumbBar).setSelectedPath(controller.getBreadCrumbTreePath());
        updateInfoLabel(thumbModelResult.getNumOfNodes(), 0);
        w.stop();
        LOGGER.info("refill thumb view (table) duration: " + w.toString()); //NOI18N
    }

    /**
     * alternativer thumb view basierend auf einem jtable
     */
    private void fillAlternativeThumbView(List<AbstractLeafNode> currentMovieNodes) {
        ((ThumbTable) jList1).refreshThumbView(currentMovieNodes);
    }

    private void fillThumbTable(List<AbstractLeafNode> movieNodes) {
        ((DetailedThumbTable) jTable1).refreshModelData(movieNodes);
    }

    private void fillStripesThumbTable(GroupedThumbView currentThumbsBreakDownByChildren) {
        ((StripeThumbTable) jXTreeTable1).refreshModelData(currentThumbsBreakDownByChildren, jScrollPane2.getWidth());
    }

//    public void registerKeyEvent(KeyStroke keyStroke, String actionName, Action action) {
//        jTable1.getInputMap().put(keyStroke, actionName);
//        jTable1.getActionMap().put(actionName, action);
//        jList1.getInputMap().put(keyStroke, actionName);
//        jList1.getActionMap().put(actionName, action);
//        jXTreeTable1.getInputMap().put(keyStroke, actionName);
//        jXTreeTable1.getActionMap().put(actionName, action);
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar jBreadCrumbBar;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBoxColorType;
    private javax.swing.JComboBox jComboBoxOrdering;
    private javax.swing.JComboBox jComboBoxThumbTypeMode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelInfo;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButtonThumbCard;
    private javax.swing.JToggleButton jToggleButtonThumbView;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBarLastActions;
    private org.jdesktop.swingx.JXPanel jXPanel1;
    private org.jdesktop.swingx.JXPanel jXPanel2;
    private org.jdesktop.swingx.JXTreeTable jXTreeTable1;
    // End of variables declaration//GEN-END:variables
    private int lastNumOfNodes;
    private int lastNumSelected;

    private void updateInfoLabel(int numOfNodes, int numSelected) {
        if (numOfNodes < 0) {
            numOfNodes = lastNumOfNodes;
        }
        if (numSelected < 0) {
            numSelected = lastNumSelected;
        }

        jLabelInfo.setText(MessageFormat.format(localeBundle.getString("SELECTED"), numOfNodes, numSelected));

        // update cache:
        lastNumOfNodes = numOfNodes;
        lastNumSelected = numSelected;
    }
}
