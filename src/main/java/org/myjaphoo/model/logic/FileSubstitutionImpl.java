/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.myjaphoo.model.logic;

import org.myjaphoo.model.AbstractFileSubstitution;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.PathMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author lang
 */
public class FileSubstitutionImpl extends AbstractFileSubstitution {

    private final static Logger logger = LoggerFactory.getLogger(FileSubstitutionImpl.class.getName());

    public FileSubstitutionImpl() {
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

        List<PathMapping> allMappings = CacheManager.getPathMappingCache().getCachedPathMappings();
        return checkAllPathMappings(canonicalPath, allMappings);
    }

}
