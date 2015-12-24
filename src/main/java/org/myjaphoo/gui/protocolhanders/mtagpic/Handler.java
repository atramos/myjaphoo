package org.myjaphoo.gui.protocolhanders.mtagpic;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * Protocol connection for internal protocol to deliver meta-tag images.
 * see
 * http://stackoverflow.com/questions/9388264/jeditorpane-with-inline-image
 * for explanation.
 * The handlre gets registered as the package name where this handler lies in.
 * So this gets registered as tagpic protocol.
 *
 */
public class Handler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new MTagPicConnection(u);
    }

    public static void install() {
        String pkgName = Handler.class.getPackage().getName();
        String pkg = pkgName.substring(0, pkgName.lastIndexOf('.'));

        String protocolHandlers = System.getProperty("java.protocol.handler.pkgs", "");
        if (!protocolHandlers.contains(pkg)) {
            if (!protocolHandlers.isEmpty()) {
                protocolHandlers += "|";
            }
            protocolHandlers += pkg;
            System.setProperty("java.protocol.handler.pkgs", protocolHandlers);
        }
    }
}