package org.myjaphoo.gui

import groovy.swing.SwingBuilder
import groovy.transform.CompileStatic
import org.myjaphoo.model.db.BookMark
import org.myjaphoo.model.filterparser.Substitution
import org.myjaphoo.model.logic.MovieEntryJpaController
import org.myjaphoo.model.logic.ScriptJpaController
import org.myjaphoo.util.MJObservableList

/**
 * main controller. Contains models/data which have all view in common.
 * @author mla
 * @version $Id$
 *
 */
@CompileStatic
class MainApplicationController {

    /** all chronic entries. */
    MJObservableList chronicList = new MJObservableList();

    /** all bookmark entries. */
    MJObservableList bookmarkList = new MJObservableList();

    MJObservableList scriptList = new MJObservableList();

    /** singleton */
    private static MainApplicationController mainApplicationController;

    private MainApplicationController() {
        reloadBookmarkList();
        reloadChronicList();
        reloadScriptList();
    }

    def reloadBookmarkList() {
        SwingBuilder sb = new SwingBuilder()
        sb.edt {
            MovieEntryJpaController jpa = new MovieEntryJpaController();
            bookmarkList.clear();
            bookmarkList.addAll(jpa.findBookMarkEntities());
        }
    }

    def reloadScriptList() {
        SwingBuilder sb = new SwingBuilder()
        sb.edt {
            ScriptJpaController jpa = new ScriptJpaController();
            scriptList.clear();
            scriptList.addAll(jpa.findScriptEntities());
        }
    }

    def reloadChronicList() {
        SwingBuilder sb = new SwingBuilder()
        sb.edt {
            MovieEntryJpaController jpa = new MovieEntryJpaController();
            chronicList.clear();
            chronicList.addAll(jpa.findChronicEntryEntities());
        }
    }

    public static MainApplicationController getInstance() {
        if (mainApplicationController == null) {
            mainApplicationController = new MainApplicationController();
        }
        return mainApplicationController;
    }

    public Map<String, Substitution> createSubstitutions() {
        HashMap<String, Substitution> subs = new HashMap<String, Substitution>();
        for (Object bmo : bookmarkList) {
            BookMark bm = (BookMark) bmo;
            subs.put(bm.getName(), new Substitution(bm.getName(), bm.getView().getCombinedFilterExpression()));
        }
        return subs;
    }
}
