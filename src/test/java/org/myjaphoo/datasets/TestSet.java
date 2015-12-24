/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.datasets;

import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Rating;
import org.myjaphoo.model.db.Token;

import java.util.ArrayList;

/**
 * Container für ein Test set an Daten. Inclusive Helper methoden zum erstellen
 * der Daten.
 * @author mla
 */
public class TestSet {

    public ArrayList<MovieEntry> entries = new ArrayList<MovieEntry>();
    public ArrayList<Token> tokens = new ArrayList<Token>();
    public ArrayList<MetaToken> metaTokens = new ArrayList<MetaToken>();

    private long idgen = 0;

    public Token createToken(int i) {
        return createToken("token");
    }

    public Token createToken(String name) {
        Token token = new Token();
        token.setId(idgen++);
        token.setName(name);
        tokens.add(token);
        return token;
    }

    public Token createAndAssignToken(MovieEntry entry, String tagName) {
        Token tag = createToken(tagName);
        ass(entry, tag);
        return tag;
    }

    public MetaToken createMetaToken(int i) {
        return createMetaToken("metatoken");
    }

    public MetaToken createMetaToken(String name) {
        MetaToken token = new MetaToken();
        token.setId(idgen++);
        token.setName(name);
        metaTokens.add(token);
        return token;
    }

    public void ass(MovieEntry e1, Token t1) {
        e1.getTokens().add(t1);
        t1.getMovieEntries().add(e1);
    }

    public MovieEntry createEntry(int i) {
        return createEntry(createName(i), createCanonDir(i), i, i, createRating(i));
    }

    public MovieEntry createEntry(String name, String dir, long filelen, long chksum, Rating rating) {
        MovieEntry entry = new MovieEntry();
        entry.setId(idgen++);
        entry.setName(name);
        entry.setCanonicalDir(dir);
        entry.setFileLength(filelen);
        entry.setChecksumCRC32(chksum);
        entry.setRating(rating);
        entries.add(entry);
        return entry;
    }

    private String createName(int i) {
        return "name";
    }

    private String createCanonDir(int i) {
    
        return "aaa/bbb/ccc";
    }

    private Rating createRating(int i) {
        return Rating.GOOD;
    }

    public void ass(MetaToken mt1, Token t1) {
        mt1.getAssignedTokens().add(t1);
        t1.getAssignedMetaTokens().add(mt1);
    }
}
