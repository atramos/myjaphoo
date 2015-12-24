/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.db.upgrades;

import org.myjaphoo.model.db.DatabaseVersion;
import org.myjaphoo.model.logic.dbhandling.TransactionBoundaryDelegator;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 *
 * @author lang
 */
public class DatabaseVersionDao {

    TransactionBoundaryDelegator tr;

    public DatabaseVersionDao(TransactionBoundaryDelegator tr) {
        this.tr = tr;
    }

    public DBUpgrade getLatestUpgrade() {
        return tr.doLoading(new TransactionBoundaryDelegator.LoaderBlock<DBUpgrade>() {
            @Override
            public DBUpgrade runLoadBlock(EntityManager em) {
                Query q = em.createQuery("from DatabaseVersion order by id desc");
                List result = q.getResultList();
                if (result.size() == 0) {
                    // boot strap with first marker:
                    return DBUpgrade.V_1_0_0;
                } else {
                    DatabaseVersion v = (DatabaseVersion) result.get(0);
                    return v.getDbversion();
                }
            }
        });
    }
}
