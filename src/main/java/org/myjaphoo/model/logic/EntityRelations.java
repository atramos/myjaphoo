/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic;

import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;

/**
 * Hilfs-Funktionen, um relationen zwischen den Entities zu ändern.
 * @author mla
 */
public class EntityRelations {

    /**
     * Entfernt ein Metatoken von allen möglichen Verlinkungen zu anderen Entities.
     */
    public static void unlinkMetaToken(MetaToken tokDel) {
        for (Token token : tokDel.getAssignedTokens()) {
            token.getAssignedMetaTokens().remove(tokDel);
        }
        tokDel.getAssignedTokens().clear();
        MetaToken parent = tokDel.getParent();
        parent.getChildren().remove(tokDel);
    }

    public static void unlinkToken(Token tok) {
        // alle relationen auf movies entfernen:
        for (MovieEntry entry : tok.getMovieEntries()) {
            entry.getTokens().remove(tok);
        }
        tok.getMovieEntries().clear();
        // relationen auf metatoken entfernen:
        for (MetaToken mt : tok.getAssignedMetaTokens()) {
            mt.getAssignedTokens().remove(tok);
        }
        tok.getAssignedMetaTokens().clear();
        // parent relation:
        if (tok.getParent() != null) {
            tok.getParent().getChildren().remove(tok);
            tok.setParent(null);
        }
    }

    public static void unlinkMovieEntry(MovieEntry mv) {
        for (Token token: mv.getTokens()) {
            token.getMovieEntries().remove(mv);
        }
        mv.getTokens().clear();
    }    
    
    public static void linkTokenToMetatoken(Token token, MetaToken metaToken) {
        if (!token.getAssignedMetaTokens().contains(metaToken)) {
            token.getAssignedMetaTokens().add(metaToken);
        }
        if (!metaToken.getAssignedTokens().contains(token)) {
            metaToken.getAssignedTokens().add(token);
        }
    }

    public static void unlinkTokenFromMetatoken(Token token, MetaToken metatoken) {
        if (token.getAssignedMetaTokens().contains(metatoken)) {
            token.getAssignedMetaTokens().remove(metatoken);
        }
        if (metatoken.getAssignedTokens().contains(token)) {
            metatoken.getAssignedTokens().remove(token);
        }
    }

    public static void unlinkTokenFromMovie(MovieEntry movieEntry, Token token) {
        if (movieEntry.getTokens().contains(token)) {
            movieEntry.getTokens().remove(token);
        }
        if (token.getMovieEntries().contains(movieEntry)) {
            token.getMovieEntries().remove(movieEntry);
        }
    }

    public static void move(MetaToken tok2Move, MetaToken newParent) {
        MetaToken oldParent = tok2Move.getParent();
        oldParent.getChildren().remove(tok2Move);
        tok2Move.setParent(newParent);
        newParent.getChildren().add(tok2Move);
    }
}
