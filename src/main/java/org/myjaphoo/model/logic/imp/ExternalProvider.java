package org.myjaphoo.model.logic.imp;

/**
 * a provider for something, that is usually done by an external tool.
 */
public interface ExternalProvider {
    /**
     * checks, if this method is available. This depends on if the external program is properly configured
     * or could be automatically recognized and also if it is available on the appropriate platform.
     */
    boolean isAvailable();

    /**
     * returns a small descr what thumb nail provider this is.
     *
     * @return the description
     */
    String getDescr();
}
