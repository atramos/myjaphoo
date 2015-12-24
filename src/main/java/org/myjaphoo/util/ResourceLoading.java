package org.myjaphoo.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ResourceLoading
 * @author mla
 * @version $Id$
 */
public class ResourceLoading {

    /**
     * Scans a resource path and lists all found files.
     * If this sources are packaged within a jar file, then the jar file gets scanned,
     * otherwise the resources are scanned by the given local file that the resource name denotes.
     * @param resourcePath
     * @return
     * @throws IOException
     */
    public static List<String> listResourcesFromPath(String resourcePath) throws IOException {
        ArrayList<String> resourcePathNames = new ArrayList<>();
        // get jar containing this sources:
        final File jarFile = new File(ResourceLoading.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        if (jarFile.isFile()) {  // Run with JAR file
            try (final JarFile jar = new JarFile(jarFile)) {
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    if (name.startsWith(resourcePath + "/")) { //filter according to the path
                        resourcePathNames.add(name);
                    }
                }
            }
        } else { // Run with IDE
            final URL url = ResourceLoading.class.getResource("/" + resourcePath);
            if (url != null) {
                try {
                    final File apps = new File(url.toURI());
                    for (File app : apps.listFiles()) {
                        resourcePathNames.add(app.getName());
                    }
                } catch (URISyntaxException ex) {
                    // never happens
                }
            }
        }
        return resourcePathNames;
    }
}
