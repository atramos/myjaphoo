/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic;

import org.hibernate.jpa.QueryHints;
import org.mlsoft.structures.Trees;
import org.myjaphoo.model.db.MovieEntry;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.dbconfig.Commit;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.dbconfig.Loader;
import org.myjaphoo.processing.AbstractEntryListProcessor;
import org.myjaphoo.processing.EntryListProcessor;
import org.myjaphoo.processing.Processing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;


/**
 *
 * @author lang
 */
public class TokenJpaController extends AbstractDao{
    
    private static Logger logger = LoggerFactory.getLogger(TokenJpaController.class.getName());
    
    public TokenJpaController(DBConnection dbConn) {
        super(dbConn);
    }
    
    public TokenJpaController() {
        super(null);
    }
    
    public void removeToken(final Token currentSelectedToken) {
        if (currentSelectedToken.getChildren().size() > 0) {
            throw new RuntimeException("token has children: can not remove it!");
        }
        if (currentSelectedToken.getParent() == null) {
            throw new RuntimeException("Root can not be removed!");
        }
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                Token tokDel = em.merge(currentSelectedToken);
                EntityRelations.unlinkToken(tokDel);
                em.remove(tokDel);
            }
        };
    }
    
    public void assignTok(MovieEntry movieEntry, Token token) {

        movieEntry = MyjaphooDB.singleInstance().ensureObjIsAttached(movieEntry);
        token = MyjaphooDB.singleInstance().ensureObjIsAttached(token);

        if (!movieEntry.getTokens().contains(token)) {
            movieEntry.getTokens().add(token);
        }
        if (!token.getMovieEntries().contains(movieEntry)) {
            token.getMovieEntries().add(movieEntry);
        }
    }
    
    public void assignToken2MovieEntry(final Token token, List<MovieEntry> movies) {
        EntryListProcessor processor = new AbstractEntryListProcessor() {
            
            @Override
            public void process(MovieEntry entry) throws Exception {
                assignTok(entry, token);
            }
        };
        Processing.processtInOneTransaction(movies, processor, "assign '" + token.getName() + "' to movies");
    }
    
    public void create(final Token token, final Token parentToken) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                if (parentToken != null) {
                    Token mergedParentToken = em.merge(parentToken);
                    mergedParentToken.getChildren().add(token);
                    token.setParent(mergedParentToken);
                }
                em.persist(token);
            }
        };
    }
    
    public void edit(final Token token) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                if (token.getParent().equals(token)) {
                    // selbstzuweisung: nicht möglich:
                    throw new RuntimeException("Token has itself as parent!!");
                }
                em.merge(token);
            }
        };
    }
    
    public Token findTokenByName(final String tokenname) {
        return new Loader<Token>(getDbConn()) {
            @Override
            protected Token run(EntityManager em) {
                return findTokenByName(em, tokenname);
            }
        }.execute();
    }
    
    public static Token findTokenByName(EntityManager em, final String tokenname) {
        try {
            Query q = em.createQuery("select object(o) from Token as o where o.name=:nam");
            q.setParameter("nam", tokenname);
            return (Token) q.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    
    public void moveToken(final Token newParent, final Token token2Move) {
        logger.info("move " + token2Move.getName() + " under " + newParent.getName());
        if (token2Move.equals(newParent)) {
            throw new RuntimeException("Token has itself as parent!!");
        }
        Trees.checkCircle(newParent, token2Move);
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                Token tok2Move = em.merge(token2Move);
                Token newPar = em.merge(newParent);
                Token oldParent = tok2Move.getParent();
                oldParent.getChildren().remove(tok2Move);
                tok2Move.setParent(newParent);
                newPar.getChildren().add(tok2Move);
            }
        };
    }

    public List<Token> fetchAll() {
        return new Loader<List<Token>>(getDbConn()) {
            @Override
            protected List<Token> run(EntityManager em) {
                Query q = em.createQuery("select o from Token as o left join fetch o.attributes left join fetch o.movieEntries");
                q.setHint(QueryHints.HINT_READONLY, true);
                return q.getResultList();
            }
        }.execute();
    }
    
    public Token findRootToken() {
        return new Loader<Token>(getDbConn()) {
            @Override
            protected Token run(EntityManager em) {
                return findRootToken(em);
            }
        }.execute();
    }
    
    public static Token findRootToken(EntityManager em) {
        Query q = em.createQuery("select object(o) from Token as o where o.parent is null");
        return (Token) q.getSingleResult();
    }
    
    public void unassignTokenFromMovies(final Token token, final Collection<MovieEntry> movies) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                Token mergedToken = em.merge(token);
                for (MovieEntry movieEntry : movies) {
                    MovieEntry mergedMovie = em.merge(movieEntry);
                    EntityRelations.unlinkTokenFromMovie(mergedMovie, mergedToken);
                }
            }
        };
    }
}
