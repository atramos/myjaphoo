/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.logic;

import org.myjaphoo.model.AbstractFileSubstitution;
import org.myjaphoo.model.db.PathMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A "faster" version of the file substitution which is therefore not so reliable. It should therefore be used
 * for e.g. gui displaying of such information, where it could be otherwise slow down extremely, if the files get localized
 * each and every time.
 * @author lang
 */
public class FasterFileSubstitution extends AbstractFileSubstitution {

    private final static Logger logger = LoggerFactory.getLogger(FileSubstitutionImpl.class.getName());

    private FasterSubstitutionCache cache = new FasterSubstitutionCache();


    public FasterFileSubstitution() {
    }

    /**
     * quasi dasselbe wie substitute: suche mittels substitution, ob u. wo das file existiert.
     * Falls es nicht auffindbar ist, dann returniere null
     * @param canonicalPath
     * @return
     */
    @Override
    public String locateFileOnDrive(String canonicalPath) {

        String result = tryLocateByDynamicConfiguredSubstitutors(canonicalPath);
        if (result != null) {
            return result;
        }

        // zuerst ohne substitution testen:
        if (fileExists(canonicalPath)) {
            logger.trace("located without substitution: " + canonicalPath);
            return canonicalPath;
        }
        // mache alle filenamen wieder canonisch zu diesem filesystem/betriebssystem:
        canonicalPath = reCanonical(canonicalPath);

        List<PathMapping> allMappings = cache.getCache();
        return checkAllPathMappings(canonicalPath, allMappings);
    }


}
