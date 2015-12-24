/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * InfoPanel.java
 *
 * Created on 25.10.2009, 17:48:32
 */
package org.myjaphoo.gui.infopanel

import groovy.swing.SwingBuilder
import org.apache.commons.lang.StringEscapeUtils
import org.jdesktop.swingx.*
import org.mlsoft.common.prefs.model.AbstractMetadata
import org.mlsoft.eventbus.GlobalBus
import org.mlsoft.eventbus.Subscribe
import org.myjaphoo.MovieNode
import org.myjaphoo.MyjaphooAppPrefs
import org.myjaphoo.MyjaphooController
import org.myjaphoo.MyjaphooView
import org.myjaphoo.gui.HeapIndicator
import org.myjaphoo.gui.icons.Icons
import org.myjaphoo.gui.util.Helper
import org.myjaphoo.gui.util.MJHyperlinkListener
import org.myjaphoo.gui.util.Utils
import org.myjaphoo.model.DuplicateHashMap
import org.myjaphoo.model.cache.CacheManager
import org.myjaphoo.model.db.MovieEntry
import org.myjaphoo.model.db.Token
import org.myjaphoo.model.dbcompare.DatabaseComparison
import org.myjaphoo.model.externalPrograms.ExternalProgram
import org.myjaphoo.model.externalPrograms.ExternalPrograms
import org.myjaphoo.model.logic.MyjaphooDB
import org.myjaphoo.model.logic.imp.ExternalProvider
import org.myjaphoo.model.logic.imp.ProviderFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.swing.*
import java.awt.*
import java.util.List

/**
 * @author mla
 */
public class InfoPanel extends JXPanel {

    private static final Logger logger = LoggerFactory.getLogger(InfoPanel.class);
    private final MyjaphooController controller;
    private TaskPaneWithText selPane = new TaskPaneWithText(java.util.ResourceBundle.getBundle("org/myjaphoo/gui/infopanel/resources/InfoPanel").getString("SELECTION"));
    private TaskPaneWithText dbInfoPane = new TaskPaneWithText(java.util.ResourceBundle.getBundle("org/myjaphoo/gui/infopanel/resources/InfoPanel").getString("DATABASE"));
    private TaskPaneWithText compDbInfoPane = new TaskPaneWithText(java.util.ResourceBundle.getBundle("org/myjaphoo/gui/infopanel/resources/InfoPanel").getString("COMPARISON DB"));
    private JXTaskPane paneVersion = new JXTaskPane(java.util.ResourceBundle.getBundle("org/myjaphoo/gui/infopanel/resources/InfoPanel").getString("VERSION"));
    private JXTaskPane paneHeap = new JXTaskPane(java.util.ResourceBundle.getBundle("org/myjaphoo/gui/infopanel/resources/InfoPanel").getString("HEAP"));
    private JXTaskPane paneTools = new JXTaskPane(java.util.ResourceBundle.getBundle("org/myjaphoo/gui/infopanel/resources/InfoPanel").getString("TOOLS"));
    private JXTaskPane paneProviders = new JXTaskPane(java.util.ResourceBundle.getBundle("org/myjaphoo/gui/infopanel/resources/InfoPanel").getString("PROVIDERS"));

    private JXTaskPaneContainer container = new JXTaskPaneContainer();

    class TaskPaneWithText {

        JXTaskPane pane;
        JEditorPane area = new JEditorPane("text/html", "<html>"); //NOI18N

        public TaskPaneWithText(String name) {
            pane = new JXTaskPane(name);
            pane.setCollapsed(true);
            pane.add(area);
            // disable editing, so that hyperlinks are activated
            area.setEditable(false);
            area.addHyperlinkListener(new MJHyperlinkListener(area));
        }
    }

    /**
     * Creates new form InfoPanel
     */
    public InfoPanel(final MyjaphooController controller) {
        this.controller = controller;
        try {
            setLayout(new BorderLayout());
            add(new JScrollPane(container));

            container.setBackground(this.getBackground());

            container.add(selPane.pane);
            selPane.pane.setCollapsed(false);
            container.add(dbInfoPane.pane);

            container.add(compDbInfoPane.pane);

            container.add(paneVersion);
            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.myjaphoo.MyjaphooApp.class).getContext().getResourceMap(MyjaphooView.class);
            String title = resourceMap.getString("Application.title");
            String homepagelink = resourceMap.getString("Application.homepage");
            String homepagelinkDocs = resourceMap.getString("Application.homepage.docs");
            final JXLabel versionLabel = new JXLabel(title);
            versionLabel.setIcon(Icons.IR_INFO.icon);
            paneVersion.add(versionLabel);

            if (Desktop.isDesktopSupported()) {
                JXHyperlink homepage = new JXHyperlink();
                homepage.setURI(new URI(homepagelink));
                homepage.setIcon(Icons.IR_HOME.icon);
                paneVersion.add(homepage);

                JXHyperlink homepageDocs = new JXHyperlink();
                homepageDocs.setURI(new URI(homepagelinkDocs));
                homepageDocs.setIcon(Icons.IR_HOME.icon);
                paneVersion.add(homepageDocs);
            }

            container.add(paneProviders);
            container.add(paneTools);
            updateToolProviderInfos();

            container.add(paneHeap);
            // heap display label:
            //HeapIndicatorLabel hiLabel = new HeapIndicatorLabel(controller.getView().getFrame());
            HeapIndicator hiLabel = new HeapIndicator(controller.getView().getFrame());
            paneHeap.add(hiLabel);
            paneHeap.setCollapsed(false);

            initDbValues();
        } catch (URISyntaxException ex) {
            logger.error("error uri!", ex); //NOI18N
        }
        GlobalBus.bus.register(this);
    }

    @Subscribe(onETD = true)
    public void plafChanged(AbstractMetadata changedConfigEntry) {
        // no matter what entry,
        // we just update the tools and provider info stuff:
        updateToolProviderInfos();
    }

    private void updateToolProviderInfos() {
        paneProviders.removeAll();
        // add infos about the providers:
        try {
            paneProviders.add(createProviderInfo(ProviderFactory.getBestThumbnailProvider(), "This is the tool used to generate thumb nails for videos"));
        } catch (RuntimeException e) {
            paneProviders.add(createNotAvailableLabel("no thumb nail provider"));
        }
        try {
            paneProviders.add(createProviderInfo(ProviderFactory.getBestMovieAttributeProvider(), "This is the tool used to extract video attributes"));
        } catch (RuntimeException e) {
            paneProviders.add(createNotAvailableLabel("no movie attribute provider"));
        }
        paneTools.removeAll();
        for (ExternalProgram prg : ExternalPrograms.ALL_PRGS) {
            paneTools.add(createAvailabilityInfo(prg));
        }
    }

    private JXLabel createNotAvailableLabel(String s) {
        Icon icon = Icons.IR_NO.icon;
        JXLabel l = new JXLabel(s, icon, SwingConstants.LEFT);
        return l;
    }

    private JXLabel createProviderInfo(ExternalProvider provider, String tooltip) {
        String txt = provider.getDescr();
        Icon icon = provider.isAvailable() ? Icons.IR_YES.icon : Icons.IR_NO.icon;
        JXLabel l = new JXLabel(txt, icon, SwingConstants.LEFT);
        l.setToolTipText(tooltip);
        return l;
    }

    private JXLabel createAvailabilityInfo(ExternalProgram prg) {
        String txt = prg.getName();
        Icon icon = prg.exists() ? Icons.IR_YES.icon : Icons.IR_NO.icon;
        JXLabel l = new JXLabel(txt, icon, SwingConstants.LEFT);
        String tooltip = !prg.exists() ? "not properly configured. Check preferences!<br>" : "";
        tooltip = "<html><h2>" + txt + "</h2>" + tooltip + prg.getPrgExplanation() + "</html>";
        l.setToolTipText(tooltip);
        return l;
    }

    public void currSelectionChanged(List<MovieNode> currSelectedMovies) {
        long size = 0;
        for (MovieNode node : currSelectedMovies) {
            if (node != null && node.getMovieEntry() != null) {
                size += node.getMovieEntry().getFileLength();
            }
        }
        if (currSelectedMovies.size() == 1) {
            // set the same infos from the tooltip into the area:
            selPane.area.setText(Helper.createThumbTipText(currSelectedMovies.get(0)));
        } else {
            selPane.area.setText(currSelectedMovies.size() + " sel. Files<br>" + Utils.humanReadableByteCount(size));
        }
    }

    public void structureUpdated(MyjaphooController controller) {
        /*
        infoModel.movTreeUnique.setValue(controller.getFilter().isTreeShowsMoviesUnique() ? "Ja" : "Nein");
        infoModel.movTreeStructure.setValue(controller.getFilter().getUserDefinedStruct());
        infoModel.nodeChanged(infoModel.movTreeUnique);
        infoModel.nodeChanged(infoModel.movTreeStructure);
         * 
         */
    }

    public void updateDBComparisonValues(DatabaseComparison dbcomp) {
        DatabaseComparison.Info info = dbcomp.getInstance().getInfo();
        if (info.isComparisonDBOpened) {
            StringBuilder b = new StringBuilder();
            b.append(StringEscapeUtils.escapeHtml(info.comparisonDBName) + "<br>");
            b.append(info.numOfMovies + " files, having " + Utils.humanReadableByteCount(info.sizeOfMovies) + "<br>");
            b.append(info.numOfDuplicates + " duplicates, wasting  " + Utils.humanReadableByteCount(info.wastedMem) + "<br>");
            compDbInfoPane.area.setText(b.toString());
        } else {
            compDbInfoPane.area.setText("");
        }
    }


    private void initDbValues() {

        StringBuilder b = new StringBuilder();
        b.append("DB: ").append(MyjaphooAppPrefs.PRF_DATABASENAME.getVal()).append("<br>");

        String connUrl = MyjaphooDB.singleInstance().getConfiguration().getFilledConnectionUrl();
        b.append(StringEscapeUtils.escapeHtml(connUrl));
        b.append("<br>");

        SwingBuilder swingBuilder = new SwingBuilder();

        controller.executeBackground {
            Collection<MovieEntry> allEntries = CacheManager.getCacheActor().getImmutableModel().getMovies();

            int anz = allEntries.size();
            long size = 0;
            for (MovieEntry entry : allEntries) {
                size += entry.getFileLength();
            }
            b.append(anz + " files, having " + Utils.humanReadableByteCount(size) + "<br>");

            // token infos:
            List<Token> allTokens = CacheManager.getCacheActor().getImmutableModel().getTokenSet().asList();
            b.append(allTokens.size() + " tags with " + calcTokenAssigments(allTokens) + " assignments<br>");


            DuplicateHashMap dupMap = CacheManager.getCacheActor().getImmutableModel().getDupHashMap();
            b.append(dupMap.calcDuplicationCount() + " duplicates, wasting  " + Utils.humanReadableByteCount(dupMap.calcWastedMem()) + "<br>");

            swingBuilder.edt {
                dbInfoPane.area.setText(b.toString());
            }
        }


    }

    private int calcTokenAssigments(List<Token> allTokens) {
        int count = 0;
        for (Token token : allTokens) {
            count += token.getMovieEntries().size();
        }
        return count;
    }


}
