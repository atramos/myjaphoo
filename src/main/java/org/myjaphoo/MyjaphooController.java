/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo;

import groovy.lang.Closure;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.mlsoft.eventbus.BasicEventBus;
import org.mlsoft.eventbus.EventBus;
import org.mlsoft.structures.AbstractTreeNode;
import org.myjaphoo.gui.MainApplicationController;
import org.myjaphoo.gui.PlayerHandler;
import org.myjaphoo.gui.action.CompleteRefresh;
import org.myjaphoo.gui.movietree.AbstractLeafNode;
import org.myjaphoo.gui.movietree.DiffNode;
import org.myjaphoo.gui.movietree.MovieStructureNode;
import org.myjaphoo.gui.movimp.ImportDialog;
import org.myjaphoo.gui.scripting.Scripting;
import org.myjaphoo.model.FileType;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.*;
import org.myjaphoo.model.dbcompare.integration.Integration;
import org.myjaphoo.model.logic.*;
import org.myjaphoo.model.logic.exceptions.NonexistentEntityException;
import org.myjaphoo.model.logic.imp.ImportDelegator;
import org.myjaphoo.model.logic.imp.MovieDelegator;
import org.myjaphoo.model.logic.imp.PicDelegator;
import org.myjaphoo.model.logic.imp.WmInfoImExport;
import org.myjaphoo.model.player.KMPlayerPlayer;
import org.myjaphoo.model.player.MPlayerPlayer;
import org.myjaphoo.model.player.Player;
import org.myjaphoo.model.player.VLCPlayer;
import org.myjaphoo.processing.*;
import org.myjaphoo.util.Filtering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.xml.bind.JAXBException;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * The controller for one WankmanView. Since multiple views can be opened, there will be exactly one controller
 * exist for each view.
 */
public class MyjaphooController {

    public static final Logger LOGGER = LoggerFactory.getLogger(MyjaphooController.class.getName());
    private MovieFilterController filter = new MovieFilterController();
    private MovieEntryJpaController jpa = new MovieEntryJpaController();
    private PathMappingJpaController pathJpa = new PathMappingJpaController();
    private ScriptJpaController scriptJpa = new ScriptJpaController();
    private ThumbnailJpaController thumbController = new ThumbnailJpaController();
    private FileSubstitutionImpl fileSubstitution = new FileSubstitutionImpl();
    private VLCPlayer vlcPlayer = new VLCPlayer();
    private MPlayerPlayer mPlayerPlayer = new MPlayerPlayer();
    private KMPlayerPlayer kmPlayerPlayer = new KMPlayerPlayer();
    /**
     * the main view, this controller belongs to.
     */
    private MyjaphooView view;
    /**
     * die zuletzt benutzen tokens.
     */
    private ArrayList<Token> lastUsedTokens = new ArrayList<Token>();
    private final static int MAXLASTTOKENSIZE = 10;
    private boolean previewThumbsInMovieTree = false;
    private MainThumbController mainThumbController;
    private MainMoviePanelController mainMoviePanelController;
    private MainTokenPanelController mainTokenPanelController;
    private MainMetaTokenPanelController mainMetaTokenPanelController;
    private Scripting scripting = new Scripting(this);
    private EventBus eventBus = new BasicEventBus();

    public MyjaphooController(MyjaphooView view) {
        this.view = view;
        this.mainThumbController = new MainThumbController(this);
        mainMoviePanelController = new MainMoviePanelController(this);
        mainTokenPanelController = new MainTokenPanelController(this);
        mainMetaTokenPanelController = new MainMetaTokenPanelController(this);
    }

    public PathMapping addMapping(File selectedFile, MovieStructureNode movieDirNode) {
        LOGGER.info("add mapping");
        PathMapping pm = new PathMapping();
        pm.setSubstitution(selectedFile.getAbsolutePath());
        pm.setPathPrefix(movieDirNode.getCanonicalDir());
        pathJpa.create(pm);
        CacheManager.getPathMappingCache().resetCache();
        return pm;
    }

    public void assignTokenToMovieNodes(Token token, List<MovieNode> selNodes) {
        updateLastUsedTokens(token);
        ArrayList<MovieEntry> movies = Filtering.nodes2Entries(selNodes);
        CacheManager.getCacheActor().assignToken2MovieEntry(token, movies);
    }

    public void assignMetaTokenToToken(MetaToken metaToken, Token token) {
        CacheManager.getCacheActor().assignMetaTokenToToken(metaToken, token);
    }

    public void deleteMovies(ArrayList<MovieEntry> nodes) {
        if (!MyjaphooDBPreferences.PRF_FO_DELETION_ALLOWED.getVal()) {
            return;
        }

        EntryListProcessor copyProcessor = new AbstractEntryListProcessor() {

            @Override
            public void process(MovieEntry entry) throws Exception {
                deleteMovie(entry);
            }
        };
        Processing.processMovieList(nodes, copyProcessor, "delete entries");

    }

    public ArrayList<Token> getLastUsedTokens() {
        return lastUsedTokens;
    }

    public ArrayList<AbstractLeafNode> getAllSelectedNodes() {
        return getView().getAllSelectedMovieNodes();
    }

    public Token createNewToken(String newTokenName, String descr, Token parent) {
        Token token = new Token();
        token.setName(newTokenName);
        token.setDescription(descr);
        CacheManager.getCacheActor().createToken(token, parent);
        updateLastUsedTokens(token);
        return token;
    }

    public MetaToken createNewMetaToken(String newTokenName, String descr, MetaToken parent) {
        MetaToken token = new MetaToken();
        token.setName(newTokenName);
        token.setDescription(descr);
        CacheManager.getCacheActor().createMetaToken(token, parent);
        return token;
    }

    /**
     * @return the currentSelectedDir
     */
    public AbstractTreeNode getCurrentSelectedDir() {
        return filter.getCurrentSelectedDir();
    }

    public void moveTokens(Token tokenParent, Token token2Move) {
        CacheManager.getCacheActor().moveToken(tokenParent, token2Move);
    }

    public void moveMetaTokens(MetaToken tokenParent, MetaToken token2Move) {
        CacheManager.getCacheActor().moveMetaTokens(tokenParent, token2Move);
    }

    public void removePathMapping(PathMapping bm) {
        try {
            pathJpa.destroy(bm.getId());
            CacheManager.getPathMappingCache().resetCache();
        } catch (NonexistentEntityException ex) {
            LOGGER.error("error", ex);
        }
    }

    public List<Token> getTokens() {
        List<Token> tokens = CacheManager.getCacheActor().getImmutableModel().getTokenSet().asList();
        return tokens;
    }

    public void playMovie(AbstractLeafNode node) {
        List<AbstractLeafNode> nodeList = new ArrayList<AbstractLeafNode>();
        nodeList.add(node);
        playMovies(nodeList);

    }

    public void removeToken(Token currentSelectedToken) {
        CacheManager.getCacheActor().removeToken(currentSelectedToken);
        filter.setCurrentToken(null);
    }

    public void removeMetaToken(MetaToken currentSelectedToken) {
        CacheManager.getCacheActor().removeMetaToken(currentSelectedToken);
        filter.setCurrentMetaToken(null);
    }

    public void unassignTokenToMovieNodes(Token token, List<MovieNode> nodes) {
        List<MovieEntry> movies = Filtering.nodes2Entries(nodes);
        CacheManager.getCacheActor().unassignTokenFromMovies(token, movies);
    }

    public void copyMovies(final List<MovieNode> nodes, final String toDir, final boolean createWmInfoFile, final FileCopying.PathOptionForCopying pathOption) throws IOException, JAXBException {

        List<FileCopyInstruction> fcis = FileCopyInstruction.createInstructions(nodes, toDir, createWmInfoFile, pathOption);


        ListProcessor<FileCopyInstruction> copyProcessor = new ListProcessor<FileCopyInstruction>() {

            FileCopying fileCopying = new FileCopying();

            @Override
            public void process(FileCopyInstruction fci) throws Exception {
                fileCopying.copyMovie(fci);
            }

            @Override
            public void startProcess() {
            }

            @Override
            public void stopProcess() {
            }

            @Override
            public String shortName(FileCopyInstruction fci) {
                return fci.getMovieEntry().getName();
            }

            @Override
            public String longName(FileCopyInstruction fci) {
                return fci.getMovieEntry().getCanonicalPath();
            }
        };
        String info = "copy to " + toDir; //NOI18N
        if (createWmInfoFile) {
            info += ", with wminfo files"; //NOI18N
        }
        info += ", " + pathOption.getDescription();
        Processing.processBigList(fcis, copyProcessor, info);
    }

    private void extractMovieInfos(MovieEntry entry) throws NonexistentEntityException, Exception {
        if (FileType.Pictures.is(entry)) {
            PicDelegator pd = new PicDelegator();
            pd.getMediaInfos(entry);

        } else if (FileType.Movies.is(entry)) {
            MovieDelegator md = new MovieDelegator();
            md.getMediaInfos(entry);
        }
        CacheManager.getCacheActor().editMovie(entry);
    }

    public void extractMovieInfos(final List<MovieEntry> nodes) throws IOException, JAXBException {

        EntryListProcessor extractProcessor = new AbstractEntryListProcessor() {

            @Override
            public void process(MovieEntry entry) throws Exception {
                extractMovieInfos(entry);
            }
        };
        Processing.processBigList(nodes, new DelayedCacheActorEventsWrapper<MovieEntry>(extractProcessor), "extract infos ");
    }

    public void integrateComparisonDifferences(final List<DiffNode> nodes) throws IOException, JAXBException {

        ListProcessor<DiffNode> extractProcessor = new ListProcessor<DiffNode>() {
            Integration integration = new Integration();

            @Override
            public void startProcess() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void process(DiffNode entry) throws Exception {
                integration.integrate(entry.getDbdiff());
            }

            @Override
            public void stopProcess() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String shortName(DiffNode diffNode) {
                return diffNode.getName();
            }

            @Override
            public String longName(DiffNode diffNode) {
                return diffNode.getCanonicalPath();
            }
        };
        Processing.processBigList(nodes, new DelayedCacheActorEventsWrapper<DiffNode>(extractProcessor), "integrate differences ");
    }


    public void playMovies(final List<AbstractLeafNode> nodes) {
        internPlayMovies(nodes, vlcPlayer);
    }

    private void internPlayMovies(final List<AbstractLeafNode> nodes, Player player) {
        PlayerHandler.playMovies(nodes, player, filter.getCurrentDisplayedMovieNodes(), getView().getFrame());
    }

    private void deleteMovie(MovieEntry movieEntry) {
        if (!MyjaphooDBPreferences.PRF_FO_DELETION_ALLOWED.getVal()) {
            return;
        }
        String sourcepath = fileSubstitution.locateFileOnDrive(movieEntry.getCanonicalPath());
        if (sourcepath != null) {
            File toDel = new File(sourcepath);
            if (!toDel.delete()) {
                throw new RuntimeException("Can not delete File " + sourcepath);
            }
        }

        CacheManager.getCacheActor().removeMovieEntry(movieEntry);

    }

    private void updateLastUsedTokens(Token token) {
        if (!lastUsedTokens.contains(token)) {
            lastUsedTokens.add(0, token);
            if (lastUsedTokens.size() > MAXLASTTOKENSIZE) {
                lastUsedTokens.remove(MAXLASTTOKENSIZE - 1);
            }
        }
    }

    /**
     * @return the view
     */
    public MyjaphooView getView() {
        return view;
    }

    public void recreateThumbs(ArrayList<MovieEntry> entries) {
        EntryListProcessor recreateThumbProcessor = new AbstractEntryListProcessor() {

            @Override
            public void process(MovieEntry entry) {
                recreateThumb(entry);
            }
        };
        Processing.processMovieList(entries, recreateThumbProcessor, "recreate Thumbs");
    }

    private void recreateThumb(MovieEntry entry) {
        ImportDelegator id = null;
        if (FileType.Pictures.is(entry)) {
            id = new PicDelegator();
        } else {
            id = new MovieDelegator();
        }
        recreateThumbnails(entry, id);
    }

    private void recreateThumbnails(MovieEntry entry, ImportDelegator id) {
        String foundPath = fileSubstitution.locateFileOnDrive(entry.getCanonicalPath());
        if (foundPath != null) {
            // delete all old thumbs:

            entry = MyjaphooDB.singleInstance().ensureObjIsAttached(entry);

            ArrayList<Thumbnail> copyOfList = new ArrayList<>(entry.getThumbnails());
            for (Thumbnail tn : copyOfList) {
                thumbController.removeThumb(tn);
            }
            List<Thumbnail> newThumbs = id.createAllThumbNails(entry, new File(foundPath));
            for (Thumbnail tn: newThumbs) {
                tn.setMovieEntry(entry);
                thumbController.create(tn);
            }
        }
    }

    public void setPreviewThumbsInMovieTree(boolean b) {
        previewThumbsInMovieTree = b;
    }

    /**
     * @return the previewThumbsInMovieTree
     */
    public boolean isPreviewThumbsInMovieTree() {
        return previewThumbsInMovieTree;
    }

    public void createWmInfoFiles(ArrayList<MovieEntry> nodes2Entries) {
        EntryListProcessor copyProcessor = new AbstractEntryListProcessor() {

            @Override
            public void process(MovieEntry entry) throws Exception {
                createWmInfoFileForMovie(entry);
            }
        };
        Processing.processMovieList(nodes2Entries, copyProcessor, "create wmfiles ");
    }

    private void createWmInfoFileForMovie(MovieEntry movieEntry) throws JAXBException, FileNotFoundException, IOException {
        WmInfoImExport exp = new WmInfoImExport();
        String sourcepath = fileSubstitution.locateFileOnDrive(movieEntry.getCanonicalPath());
        String infoFile = sourcepath + ".wminfo";
        movieEntry = MyjaphooDB.singleInstance().ensureObjIsAttached(movieEntry);
        exp.export(new File(infoFile), movieEntry);
    }

    public void setRating(final Rating rating, List<MovieNode> nodes) {
        ArrayList<MovieEntry> entries = Filtering.nodes2Entries(nodes);
        EntryListProcessor changeRatingProcessor = new AbstractEntryListProcessor() {

            @Override
            public void process(MovieEntry entry) throws Exception {
                entry.setRating(rating);
                CacheManager.getCacheActor().editMovie(entry);
            }
        };
        Processing.processMovieList(entries, changeRatingProcessor, "change ratings to " + rating.getName());
    }

    public void showAndLogErroDlg(String title, String errMsg, Throwable e) {
        LOGGER.error(errMsg, e);
        ErrorInfo info = new ErrorInfo(title, errMsg, null, null, e, Level.SEVERE, null);
        JXErrorPane.showDialog(getView().getFrame(), info);
    }

    public boolean confirm(String msgToConfirmWithYes) {
        return JOptionPane.showConfirmDialog(getView().getFrame(), msgToConfirmWithYes, "Please Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public void message(String msg) {
        JOptionPane.showMessageDialog(getView().getFrame(), msg);
    }

    public String getInputValue(String msg) {
        return JOptionPane.showInputDialog(getView().getFrame(),
                msg, "Please input value", JOptionPane.QUESTION_MESSAGE);
    }

    public void playMoviesMPlayer(List<AbstractLeafNode> nodes) {
        internPlayMovies(nodes, mPlayerPlayer);
    }

    public void playMoviesKMPlayer(List<AbstractLeafNode> nodes) {
        internPlayMovies(nodes, kmPlayerPlayer);
    }

    public void removeMovieEntriesFromDatabase(List<MovieEntry> movies) {
        if (!MyjaphooDBPreferences.PRF_FO_REMOVING_ALLOWED.getVal()) {
            return;
        }

        EntryListProcessor copyProcessor = new AbstractEntryListProcessor() {

            @Override
            public void process(MovieEntry entry) throws Exception {
                removeEntryFromDatabase(entry);
            }
        };
        Processing.processMovieList(movies, copyProcessor, "remove entries from database");

    }

    private void removeEntryFromDatabase(MovieEntry movieEntry) {
        if (!MyjaphooDBPreferences.PRF_FO_REMOVING_ALLOWED.getVal()) {
            return;
        }
        CacheManager.getCacheActor().removeMovieEntry(movieEntry);
    }

    public void removeTokenRelations(List<MovieNode> nodes, final List<Token> tokens2RemoveRelations) {
        ArrayList<MovieEntry> entries = Filtering.nodes2Entries(nodes);
        EntryListProcessor changeRatingProcessor = new AbstractEntryListProcessor() {

            @Override
            public void process(MovieEntry entry) throws Exception {
                for (Token token : tokens2RemoveRelations) {
                    CacheManager.getCacheActor().unassignTokenFromMovies(token, Arrays.asList(entry));
                }
            }
        };
        String tokenstr = toString(tokens2RemoveRelations);
        Processing.processMovieList(entries, changeRatingProcessor, "remove Token relations " + tokenstr);
    }

    private String toString(List list) {
        StringBuilder b = new StringBuilder();
        for (Object o : list) {
            if (b.length() > 0) {
                b.append(", ");
            }
            b.append(o.toString());
        }
        return b.toString();
    }

    public MovieFilterController getFilter() {
        return filter;
    }

    public List<ChangeLog> getAllChangeLogs() {
        return jpa.findChangeLogEntities();
    }

    public void createChangeLog(ChangeLogType changeLogType, String txt, List<MovieNode> targets) {
        createChangeLog2(changeLogType, txt, Filtering.nodes2Entries(targets));
    }

    public void createChangeLog2(ChangeLogType changeLogType, String txt, List<MovieEntry> targets) {
        ChangeLog log = new ChangeLog();
        log.setCltype(changeLogType);
        log.setCreated(new Date());
        log.setMsg(txt);
        log.setObjDescription(cutMax(createMovList(targets), 4000));
        jpa.create(log);
    }

    private String createMovList(List<MovieEntry> targetMovies) {
        if (targetMovies == null) {
            return null;
        }
        StringBuilder b = new StringBuilder();
        for (MovieEntry entry : targetMovies) {
            b.append(entry.getCanonicalPath());
            b.append(";");
        }
        return b.toString();
    }

    private String cutMax(String str, int max) {
        if (str == null) {
            return null;
        }
        if (str.length() > max) {
            return str.substring(0, max);
        } else {
            return str;
        }
    }

    public void addNewBookMark(String bookmarkname) {
        BookMark bm = getFilter().createBookMarkFromCurrentChronic();
        bm.setName(bookmarkname);
        bm.getView().setCreated(new Date());
        jpa.create(bm);
        MainApplicationController.getInstance().getBookmarkList().add(bm);
    }

    public void deleteBookMark(BookMark bm) {
        jpa.removeBookMark(bm);
        MainApplicationController.getInstance().getBookmarkList().remove(bm);
    }


    public void deleteScript(SavedGroovyScript bm) {
        scriptJpa.removeScript(bm);
        MainApplicationController.getInstance().getScriptList().remove(bm);
    }

    public void updateScript(SavedGroovyScript bm) {
        scriptJpa.edit(bm);
        MainApplicationController.getInstance().getScriptList().fireListElementChanged(bm);
    }

    public void addScript(SavedGroovyScript script) {
        scriptJpa.create(script);
        MainApplicationController.getInstance().getScriptList().add(script);
    }

    public void updateBookMark(BookMark bm) {
        jpa.edit(bm);
        MainApplicationController.getInstance().getBookmarkList().fireListElementChanged(bm);
    }

    /**
     * @return the mainThumbController
     */
    public MainThumbController getMainThumbController() {
        return mainThumbController;
    }

    /**
     * @return the mainMoviePanelController
     */
    public MainMoviePanelController getMainMoviePanelController() {
        return mainMoviePanelController;
    }

    /**
     * @return the mainTokenPanelController
     */
    public MainTokenPanelController getMainTokenPanelController() {
        return mainTokenPanelController;
    }

    /**
     * @return the mainMetaTokenPanelController
     */
    public MainMetaTokenPanelController getMainMetaTokenPanelController() {
        return mainMetaTokenPanelController;
    }

    public void unAssignMetaTokenFromToken(MetaToken currMetaToken, List<Token> toks2Remove) {
        CacheManager.getCacheActor().unAssignMetaTokenFromToken(currMetaToken, toks2Remove);
    }

    /**
     * @return the scripting
     */
    public Scripting getScripting() {
        return scripting;
    }

    /**
     * Shows the bookmark in the view.
     *
     * @param bm
     */
    public void showBookMark(BookMark bm) {
        // do not put the same bookmark entry back to the filter (would lead to save bookmarks as chronic...)
        // instead create new chronic entry based on the bookmark:
        ChronicEntry entry = bm.toChronic();
        getFilter().popChronic(entry);
        getView().updateMovieAndThumbViews();
    }

    /**
     * @return the eventBus
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    public void addCoverFrontPicture(File file, MovieEntry entry) throws Exception {
        MovieDelegator md = new MovieDelegator();
        md.addFrontCoverThumb(entry, file);
    }

    public void addCoverBackPicture(File file, MovieEntry entry) throws Exception {
        MovieDelegator md = new MovieDelegator();
        md.addBackCoverThumb(entry, file);
    }


    public void showImportDialog() {
        ImportDialog dlg = new ImportDialog(view.getFrame(), this);
        doImport(dlg);
    }

    public void showImportDialog(String dir) {
        ImportDialog dlg = new ImportDialog(view.getFrame(), this, dir);
        doImport(dlg);
    }

    private void doImport(ImportDialog dlg) {
        dlg.setLocationRelativeTo(view.getFrame());
        MyjaphooApp.getApplication().show(dlg);
        // trigger a complete refresh:
        new CompleteRefresh(this).actionPerformed(new ActionEvent(this, 0, "refresh"));
    }

    public void executeBackground(Closure closure) {
        view.executeBackground(closure);
    }


    public void historyBack() {
        ChronicEntry chronicEntry = getFilter().getBackHistory();
        if (chronicEntry != null) {
            getFilter().popChronic(chronicEntry);
            getView().updateChronic(chronicEntry);
        }
    }

    public void historyForward() {
        ChronicEntry chronicEntry = getFilter().getForwardHistory();
        if (chronicEntry != null) {
            getFilter().popChronic(chronicEntry);
            getView().updateChronic(chronicEntry);
        }
    }
}
