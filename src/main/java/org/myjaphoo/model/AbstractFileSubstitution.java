package org.myjaphoo.model;

import org.apache.commons.lang.StringUtils;
import org.myjaphoo.model.cache.CacheManager;
import org.myjaphoo.model.db.PathMapping;
import org.myjaphoo.model.logic.ConfigurableFileSubstitution;
import org.myjaphoo.model.registry.ComponentRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * AbstractFileSubstitution
 * @author mla
 * @version $Id$
 */
public abstract class AbstractFileSubstitution implements FileSubstitution {

    private final static Logger logger = LoggerFactory.getLogger(AbstractFileSubstitution.class.getName());


    @Override
    public String substitude(String canonicalPath) {
        String located = locateFileOnDrive(canonicalPath);
        if (located == null) {
            logger.trace("not substituted, use " + canonicalPath);
            return canonicalPath;
        } else {
            return located;
        }
    }

 /* @wiki:ScriptingExtensions
  ===== ConfigurableFileSubstitution =====

  This is an extension to plug in additional file substitution logic.
  Multiple configurable file substitutors could be registered in the application.
  If a path could not be localized, the configured substitutors are tried out in sequence until the first returns
  a path.

  ==== Interface ====

  The following interface must be implemented:

  ${f.listing(org.myjaphoo.model.logic.ConfigurableFileSubstitution.class)}
 */
    protected String tryLocateByDynamicConfiguredSubstitutors(String canoncialPath) {
        Collection<ConfigurableFileSubstitution> configuredFs = ComponentRegistry.registry.getEntryCollection(ConfigurableFileSubstitution.class);
        for (ConfigurableFileSubstitution cfs : configuredFs) {
            String result = cfs.locateFileOnDrive(canoncialPath);
            if (result != null) {
                return result;
            }
        }
        return null;
    }


    protected boolean fileExists(String filename) {
        File tester = new File(filename);
        return tester.exists();
    }

    protected String checkAllPathMappings(String canonicalPath, List<PathMapping> allMappings) {
        for (PathMapping pathMapping : allMappings) {

            logger.trace("canonicalPath:'" + canonicalPath + "' prefix:'" + pathMapping.getPathPrefix() + "'");

            String pathPrefix = reCanonical(pathMapping.getPathPrefix());

            if (canonicalPath.startsWith(pathPrefix)) {
                String substituted = StringUtils.replaceOnce(canonicalPath, pathPrefix, pathMapping.getSubstitution());
                final String absolutePath = new File(substituted).getAbsolutePath();

                if (fileExists(absolutePath)) {
                    logger.trace("substituted to " + absolutePath);
                    return absolutePath;
                }
            }
        }
        logger.trace("could not be located " + canonicalPath);
        return null;
    }

    /**
     * erzeugt einen neuen kanonischen namen unter diesen betriebssystem.
     * Notwendig, um kanonisch gespeicherte path-namen, die unter einem
     * anderen betriebssystem gespeichert wurden, diese unter diesem
     * betriebssystem wieder zu vereinheitlichen.
     * @param pathName pathname
     * @return erneut kanonisch erzeugter pfadname
     */
    protected String reCanonical(String pathName) {
        try {
            return new File(pathName).getCanonicalPath();
        } catch (IOException ex) {
            //logger.error("error recanonising pathname " + pathName, ex);
            return pathName;
        }
    }

    /**
     * tries to map back a existing file name to a substituted path.
     * Is the inverse of locateFileOnDrive.
     * Note, that it is not necessarily a full inverse function. It depends of course on the order of the mappings
     * and if the mappings overlay and therefore which mappings matches first.
     * So you will not necessarily get a valid entry path of an entry which itself points by
     * a path mapping to this valid file path
     * @param realFilePath
     * @return
     */
    @Override
    public String mapBack(String realFilePath) {
        realFilePath = reCanonical(realFilePath);
        List<PathMapping> allMappings = CacheManager.getPathMappingCache().getCachedPathMappings();
        for (PathMapping pathMapping : allMappings) {
            String substitution = reCanonical(pathMapping.getSubstitution());
            if (realFilePath.startsWith(substitution)) {
                String mappedBack = StringUtils.replaceOnce(realFilePath, substitution, pathMapping.getPathPrefix());
                return mappedBack;
            }
        }
        return null;
    }
}
