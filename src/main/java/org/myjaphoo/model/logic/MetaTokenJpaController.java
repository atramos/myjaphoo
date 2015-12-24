/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.logic;

import org.hibernate.jpa.QueryHints;
import org.mlsoft.structures.Trees;
import org.myjaphoo.model.db.MetaToken;
import org.myjaphoo.model.db.Token;
import org.myjaphoo.model.dbconfig.Commit;
import org.myjaphoo.model.dbconfig.DBConnection;
import org.myjaphoo.model.dbconfig.Loader;
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
public class MetaTokenJpaController extends AbstractDao{

    private static Logger logger = LoggerFactory.getLogger(MetaTokenJpaController.class.getName());
    private MovieEntryJpaController jpa;
    /** genau einmal beim start auf migration prüfen. dieses flag signalisiert, ob die prüfung schon lief. */
    private static boolean runMigration = false;

    
    public MetaTokenJpaController(DBConnection dbConn) {
        super(dbConn);
        jpa = new MovieEntryJpaController(dbConn);
        if (!runMigration) {
            checkMigration();
            runMigration = true;
        }
    }
    
    public MetaTokenJpaController() {
        this(null);
    }

    /**
     * Migrationsprüfung durchführen. Nur einmal mittels einer static variable geprüft.
     */
    private void checkMigration() {
        // init root token, if it does not exists:
        if (getMetaTokenCount() == 0) {
            MetaToken roottoken = new MetaToken();
            roottoken.setName("InternRootMetaToken");
            create(roottoken, null);
        }
    }

    public void removeToken(final MetaToken currentSelectedToken) {
        if (currentSelectedToken.getChildren().size() > 0) {
            throw new RuntimeException("meta token has children: can not remove it!");
        }
        if (currentSelectedToken.getParent() == null) {
            throw new RuntimeException("Root can not be removed!");
        }
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                MetaToken tokDel = em.merge(currentSelectedToken);
                EntityRelations.unlinkMetaToken(tokDel);
                em.remove(tokDel);
            }
        };
    }

    public void assignTok(final Token token, final MetaToken metatoken) {
        logger.info("assign token/metatkoken: " + token.getName() + "<->" + metatoken.getName());
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                Token mergedTok = MyjaphooDB.smartAttach(em, token);
                MetaToken mergedMetaToken = MyjaphooDB.smartAttach(em, metatoken);
                EntityRelations.linkTokenToMetatoken(mergedTok, mergedMetaToken);
            }
        };
    }

    public void create(final MetaToken metatoken, final MetaToken parentMetaToken) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                if (parentMetaToken != null) {
                    MetaToken mergedParentMetaToken = em.merge(parentMetaToken);
                    mergedParentMetaToken.getChildren().add(metatoken);
                    metatoken.setParent(mergedParentMetaToken);
                }
                em.persist(metatoken);
            }
        };
    }

    public void edit(final MetaToken metatoken) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                if (metatoken.getParent().equals(metatoken)) {
                    // selbstzuweisung: nicht möglich:
                    throw new RuntimeException("Metatoken has itself as parent!!");
                }
                em.merge(metatoken);
            }
        };
    }

    public void moveToken(final MetaToken newParent, final MetaToken token2Move) {
        logger.info("move " + token2Move.getName() + " under " + newParent.getName());
        if (token2Move.equals(newParent)) {
            throw new RuntimeException("Metatoken has itself as parent!!");
        }
        Trees.checkCircle(newParent, token2Move);
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                MetaToken tok2Move = em.merge(token2Move);
                MetaToken newPar = em.merge(newParent);
                EntityRelations.move(tok2Move, newPar);
            }
        };
    }

    public List<MetaToken> fetchAll() {
        return new Loader<List<MetaToken>>(getDbConn()) {
            @Override
            protected List<MetaToken> run(EntityManager em) {
                Query q = em.createQuery("select o from MetaToken as o  left join fetch o.attributes left join fetch o.assignedTokens");
                q.setHint(QueryHints.HINT_READONLY, true);
                return q.getResultList();
            }
        }.execute();
    }

    public MetaToken findRootToken() {
        return new Loader<MetaToken>(getDbConn()) {
            @Override
            protected MetaToken run(EntityManager em) {
                Query q = em.createQuery("select object(o) from MetaToken as o where o.parent is null");
                return (MetaToken) q.getSingleResult();
            }
        }.execute();
    }

    public int getMetaTokenCount() {
        return new Loader<Integer>(getDbConn()) {
            @Override
            protected Integer run(EntityManager em) {
                return ((Long) em.createQuery("select count(o) from MetaToken as o").getSingleResult()).intValue();
            }
        }.execute().intValue();
    }

    public void unassignMetaToken(final MetaToken metatoken, final Collection<Token> tokens) {
        new Commit(getDbConn()) {
            @Override
            protected void run(EntityManager em) {
                MetaToken mergedMetaTok = em.merge(metatoken);
                for (Token token : tokens) {
                    logger.info("unassign token/metatoken: " + mergedMetaTok.getName() + "<->" + token.getName());
                    Token mergedTok = em.merge(token);
                    EntityRelations.unlinkTokenFromMetatoken(mergedTok, mergedMetaTok);
                }
            }
        };
    }

    public MetaToken findTokenByName(final String tokenName) {
        return new Loader<MetaToken>(getDbConn()) {
            @Override
            protected MetaToken run(EntityManager em) {
                try {
                    Query q = em.createQuery("select object(o) from MetaToken as o where o.name=:nam");
                    q.setParameter("nam", tokenName);
                    return (MetaToken) q.getSingleResult();
                } catch (NoResultException nre) {
                    return null;
                }
            }
        }.execute();
    }
}
