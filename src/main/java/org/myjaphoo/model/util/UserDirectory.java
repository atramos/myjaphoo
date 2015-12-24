/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myjaphoo.model.util;

import java.io.File;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

/**
 * <p>Supply the directory name where user's preferences, log files and 
 * database are located. </p>
 * 
 * <p>By default, a directory name of <code>'${user.home}/.myjaphoo'</code> is
 * supplied. The user can define another directory name by setting the 
 * parameter <code>'wankman.model.util.UserDirectory'</code> 
 * to the application command line. For example: <code>
 * -Dwankman.model.util.UserDirectory=${user.home}/.myjaphoo_copy1</code></p>
 * 
 * <p>The directory name is processed for possible variable 
 * interpolation, like in the previous example. System properties names can
 * be used for interpolation. The system prefix '<code>sys:</code>' can be used
 * in variables; for example: <code>
 * -Dwankman.model.util.UserDirectory=${sys:user.home}/.myjaphoo_copy1</code>
 * </p>
 * 
 * <p>The resulting directory name is formated to make it
 * coherent with system specific directory separator '<code>\</code>', 
 * or '<code>/</code>'. A directory separator is added at the end of the
 * name.</p>
 *
 * @author gsd
 */
public class UserDirectory {
    // The name of the application command line parameter passed as 
    // system property
    private static String userDirPropName = "wankman.model.util.UserDirectory";

    /**
     * <p>Get the directory name where user's preferences, log files and 
     * database should be located.</p>
     * 
     * @return the directory name supplied on the application command line 
     * parameter or the expanded directory name 
     * <code>${user.home}/.myjaphoo/</code>. In either cases, the directory 
     * name supplied end with a '<code>\</code>', or '<code>/</code>'.
     */
    public static String getDirectory() {
        // Fetch the command line parameter from system properties
        String userDirPropValue = System.getProperty(userDirPropName);
        
        // Fallback content if the property does not exist
        if(userDirPropValue == null){
            // Supply an hidden directory in the user space, named after the 
            // application.
            userDirPropValue = "${sys:user.home}/.myjaphoo";
        }

        // Do variable interpolation. It can resolve a variable like
        // ${user.home} or ${sys:user.home} or other system defined property
        CompositeConfiguration aConfig = new CompositeConfiguration();
        aConfig.addConfiguration(new SystemConfiguration());
        // Have to use a different property name than 
        // "wankman.model.util.UserDirectory" because this name 'removes' the
        // property from the System properties!
        String tempName = "TempProperty";
        aConfig.setProperty(tempName, userDirPropValue);
        Configuration interpolatedConfig = aConfig.interpolatedConfiguration();
        userDirPropValue = (String) interpolatedConfig.getProperty(tempName);
        
        // Format the name according to the operating system rules
        // This is a best effort to get well formated directory name
        try {
            File file = new File(userDirPropValue);
            userDirPropValue = file.getCanonicalPath();
        } catch (Exception ex) {
            // Nothing to do since this method might be used to set
            // the logger.
        }
        return userDirPropValue + File.separator;           
    }
}
