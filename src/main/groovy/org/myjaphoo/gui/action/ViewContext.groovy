package org.myjaphoo.gui.action

import groovy.transform.TypeChecked
import org.myjaphoo.MovieNode
import org.myjaphoo.gui.movietree.AbstractLeafNode
import org.myjaphoo.gui.movietree.DiffNode
import org.myjaphoo.model.FileType
import org.myjaphoo.model.db.MovieEntry
import org.myjaphoo.model.db.Token
import org.myjaphoo.util.Filtering

/**
 * Hält Informationen zu dem augenblicklichen Kontext.
 * Actions bekommen diesen Kontext, um z.b. zu erfahren,
 * auf welchen Nodes zu arbeiten ist, etc.
 * Durch Auslagern in eine separate Klasse, können die gleichen Actions
 * mit verschiedenen Kontexten verwendet werden, je nach z.b. Aufruf
 * aus dem Movie Tree oder aber aus dem ThumbTree.
 * So ein Kontext wird üblicherweise für jeden Zusammenbau von z.b.
 * PopupMenüs erzeugt (und abhängig vom Klicken u. Zustand mit den Kontextdaten
 * befüllt).
 *
 * Prinzipiell erhält jedes WM Action einen Kontext,auch wenn es keinen benötigt.
 * Dafür gibt es einen EmptyContext.
 * @author mla
 */
@TypeChecked
class ViewContext {

    public static final ViewContext EMPTYCONTEXT = new ViewContext(Collections.EMPTY_LIST);
    /** die selektierten nodes des Kontextes. */
    private List<MovieNode> selMovies;
    private List<DiffNode> selDiffNodes;
    private List<AbstractLeafNode> selNodes;

    private boolean hasMovies;

    private boolean hasPics;

    public ViewContext(List<AbstractLeafNode> allSelectedNodes) {
        this.selNodes = Collections.unmodifiableList(allSelectedNodes);
        this.selMovies = Collections.unmodifiableList(Filtering.filterMovieNodes(selNodes));
        this.selDiffNodes = Collections.unmodifiableList(Filtering.filterDiffNodes(selNodes));

        this.hasMovies = selMovies.any { FileType.Movies.is(it) }
        this.hasPics = selMovies.any { FileType.Pictures.is(it) }
    }

    public boolean hasMovies() {
       return hasMovies;
    }

    public boolean hasPics() {
        return hasPics;
    }

    /**
     * @return the selMovies
     */
    public List<MovieNode> getSelMovies() {
        return selMovies;
    }

    /**
     * @return the selNodes
     */
    public List<AbstractLeafNode> getSelNodes() {
        return selNodes;
    }

    public static List<AbstractLeafNode> asNodes(List<MovieNode> nodes) {
        List<AbstractLeafNode> result = new ArrayList<AbstractLeafNode>();
        result.addAll(nodes);
        return result;
    }

    public boolean hasDiffNodes() {
        return getSelDiffNodes().size() > 0;
    }

    public List<DiffNode> getSelDiffNodes() {
        return selDiffNodes;
    }

    public Set<Token> getAssignedTokens() {
        TreeSet<Token> tokens = new TreeSet<Token>();
        for (MovieEntry entry : Filtering.entries(getSelNodes())) {
            tokens.addAll(entry.getTokens());
        }
        return tokens;
    }
}
