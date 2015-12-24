package org.myjaphoo.gui.movieprops

import groovy.transform.TypeChecked
import org.myjaphoo.MovieNode
import org.myjaphoo.gui.icons.Icons
import org.myjaphoo.model.db.MovieEntry

/**
 * MovieNodeInfoNode 
 * @author mla
 * @version $Id$
 *
 */
@TypeChecked
class MovieNodeInfoNode extends HeaderNode {

    private final
    static ResourceBundle localeBundle = ResourceBundle.getBundle("org/myjaphoo/gui/util/resources/Helper");

    MovieNodeInfoNode(PropertyNode parent, MovieNode movieNode) {
        super(parent, movieNode.getName())
        this.setIcon(Icons.IR_MOVIE.icon);

        if (!movieNode.isUnique()) {
            def uniqueNode = new InfoNode(this, "Uniqueness", localeBundle.getString("(NOT UNIQUE GROUPED!)"))
            uniqueNode.icon = Icons.IR_NOTUNIQUE.icon;
            children.add(uniqueNode);
        }
        if (movieNode.getCondensedDuplicatesSize() > 0) {
            HeaderNode condensed = new HeaderNode(this, "Condensed");
            condensed.icon = Icons.IR_CONDENSED.icon;
            this.children.add(condensed);
            for (MovieEntry dupEntry : movieNode.getCondensedDuplicates()) {
                HeaderNode cEntry = new HeaderNode(condensed, dupEntry.name);
                condensed.children.add(cEntry);
                cEntry.getChildren().add(new InfoNode(cEntry, "Directory", dupEntry.canonicalDir));
            }

        }
        if (movieNode.hasDups) {
            HeaderNode duplicates = new HeaderNode(this, "Duplicates");
            duplicates.icon = Icons.IR_DUPLICATES.icon;
            this.children.add(duplicates);
            for (MovieEntry dupEntry : movieNode.getDupsInDatabase()) {
                HeaderNode cEntry = new HeaderNode(duplicates, dupEntry.name);
                duplicates.children.add(cEntry);
                cEntry.getChildren().add(new InfoNode(cEntry, "Directory", dupEntry.canonicalDir));
            }
        }
        ExifExtraction.createExifNodes(this, movieNode);
    }
}
