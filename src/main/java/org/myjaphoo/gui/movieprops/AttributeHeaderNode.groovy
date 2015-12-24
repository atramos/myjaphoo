package org.myjaphoo.gui.movieprops

import groovy.util.logging.Log4j
import org.myjaphoo.MyjaphooAppPrefs
import org.myjaphoo.gui.icons.Icons
import org.myjaphoo.gui.util.Helper
import org.myjaphoo.gui.util.Utils
import org.myjaphoo.model.FileSubstitution
import org.myjaphoo.model.FileType
import org.myjaphoo.model.cache.CacheManager
import org.myjaphoo.model.db.AttributedEntity
import org.myjaphoo.model.db.MetaToken
import org.myjaphoo.model.db.MovieEntry
import org.myjaphoo.model.db.Token
import org.myjaphoo.model.filterparser.functions.Functions
import org.myjaphoo.model.filterparser.idents.Identifiers
import org.myjaphoo.model.logic.FasterFileSubstitution

/**
 * HeaderNode 
 * @author mla
 * @version $Id$
 *
 */
@Log4j
public class AttributeHeaderNode extends HeaderNode {

    private static FileSubstitution substitution = new FasterFileSubstitution();

    def AttributedEntity attributedEntity;

    def boolean attributesHaveBeenDeleted = false;

    public AttributeHeaderNode(PropertyNode parent, AttributedEntity attributedEntity) {
        super(parent, attributedEntity.name);
        this.attributedEntity = attributedEntity;

        def String attrFilterFuncName = null;
        if (attributedEntity instanceof MovieEntry) {
            buildMovieEntryInfos((MovieEntry) attributedEntity);
            attrFilterFuncName = Functions.ENTRYATTR.name;
        } else if (attributedEntity instanceof Token) {
            buildTokenInfos((Token) attributedEntity);
            attrFilterFuncName = Functions.TAGATTR.name;
        } else if (attributedEntity instanceof MetaToken) {
            buildMetaTokenInfos((MetaToken) attributedEntity);
            attrFilterFuncName = Functions.METATAGATTR.name
        }

        children.add(new CommentNode(this, attributedEntity));
        // build up child nodes:
        def attrNodes = attributedEntity.attributes.collect { String key, String value -> new AttributeNode(this, key, value) }
        attrNodes = attrNodes.sort { AttributeNode a1, AttributeNode a2 -> a1.name <=> a2.name }
        // set filter expressions for the attributes:
        attrNodes.each { AttributeNode a -> a.filterExpression = "${attrFilterFuncName}(\"${a.name}\") like \"${a.value}\"" }
        children.addAll(attrNodes);
    }

    def buildMetaTokenInfos(MetaToken metaToken) {
        this.setIcon(Icons.IR_METATAG.icon);
        filterExpression = "${Identifiers.METATAG.getName()}  is '${metaToken.getName()}'";
    }

    def buildTokenInfos(Token token) {
        this.setIcon(Icons.IR_TAG.icon);
        filterExpression = "${Identifiers.TAG.getName()}  is '${token.getName()}'";
    }

    def buildMovieEntryInfos(MovieEntry entry) {
        this.setIcon(Icons.IR_MOVIE.icon);

        HeaderNode info = new HeaderNode(this, "Info");
        this.getChildren().add(info);
        info.getChildren().add(new InfoNode(info, "File", entry.name));
        info.getChildren().add(new InfoNode(info, "Directory", entry.canonicalDir));
        info.getChildren().add(new InfoNode(info, "File Length", Utils.humanReadableByteCount(entry.getFileLength())));
        if (entry.getChecksumCRC32() != null) {
            info.getChildren().add(new InfoNode(info, "CRC32", Long.toHexString(entry.getChecksumCRC32())));
        }
        if (entry.getRating() != null) {
            info.getChildren().add(new InfoNode(info, "Rating", entry.rating.name));
        }
        if (MyjaphooAppPrefs.PRF_SHOW_FILLOCALISATION_HINTS.getVal()) {
            String located = substitution.locateFileOnDrive(entry.getCanonicalPath());
            if (located != null) {
                info.getChildren().add(new InfoNode(info, "Located", substitution.substitude(located)));
            } else {
                info.getChildren().add(new InfoNode(info, "Located", "---"));
            }
        }

        String fmt = ""; //NOI18N
        if (entry.getMovieAttrs().getFormat() != null) {
            fmt = Helper.prepareFormat(entry.getMovieAttrs().getFormat());
        }
        info.getChildren().add(new InfoNode(info, "Format", "$fmt ${entry.movieAttrs.width}x${entry.movieAttrs.height}"));

        // add only movieattributes for movies, not for pictures
        if (FileType.Movies.is(entry)) {
            String fmtBitRate = Utils.humanReadableByteCount(entry.getMovieAttrs().getBitrate());
            info.getChildren().add(new InfoNode(info, "Bitrate", fmtBitRate));
            info.getChildren().add(new InfoNode(info, "FPS", Integer.toString(entry.getMovieAttrs().getFps())));
            String len = org.apache.commons.lang.time.DurationFormatUtils.formatDurationHMS(entry.getMovieAttrs().getLength() * 1000);
            info.getChildren().add(new InfoNode(info, "Length", len));
        }
    }

    void save() {
        attributedEntity.comment = children.find { it instanceof CommentNode }.value;
        attributedEntity.attributes = children.findAll {
            it instanceof AttributeNode
        }.collect { AttributeNode a -> [a.name, a.value] }.collectEntries()

        try {
            if (attributedEntity instanceof MovieEntry) {
                CacheManager.getCacheActor().editMovie((MovieEntry) attributedEntity);
            } else if (attributedEntity instanceof Token) {
                CacheManager.getCacheActor().editToken((Token) attributedEntity);
            } else if (attributedEntity instanceof MetaToken) {
                CacheManager.getCacheActor().editMetaToken((MetaToken) attributedEntity);
            }
            // reset edited flag afterwards:
            children.each { it.edited = false}
        } catch (Exception ex) {
            log.error("can not save entry!", ex); //NOI18N
        }
    }

    boolean needsSaving() {
        return attributesHaveBeenDeleted || children.find { it.hasBeenChanged() }
    }
}
