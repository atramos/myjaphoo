package org.myjaphoo.model.logic;

import junit.framework.TestCase;
import org.myjaphoo.LoggingConfiguration;
import org.myjaphoo.model.db.PrefsStringVal;

/**
 * @author mla
 * @version $Id$
 *          To change this template use File | Settings | File Templates.
 */
public class DbPreferencesTest extends TestCase{

    private PreferencesDao dao = new PreferencesDao();

    public void testDao() {

        LoggingConfiguration.configurate();

        PrefsStringVal pf = new PrefsStringVal();

        String myId = "test" + System.currentTimeMillis();
        pf.setId(myId);
        pf.setStringValue("hello");
        dao.create(pf);

        PrefsStringVal prefVal = dao.find(myId, PrefsStringVal.class);
    }

}
