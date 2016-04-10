package ch.qos.cal10n.util;

import java.net.URL;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class CAL10NBundleFinderByClassloaderExt extends AbstractCAL10NBundleFinderExt {

    final ClassLoader classLoader;

    public CAL10NBundleFinderByClassloaderExt(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    protected URL getResource(String resourceCandidate) {
        return classLoader.getResource(resourceCandidate);
    }
}
