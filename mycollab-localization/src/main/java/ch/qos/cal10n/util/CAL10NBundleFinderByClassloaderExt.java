package ch.qos.cal10n.util;

import com.mycollab.core.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class CAL10NBundleFinderByClassloaderExt extends AbstractCAL10NBundleFinderExt {
    private static Logger LOG = LoggerFactory.getLogger(CAL10NBundleFinderByClassloaderExt.class);
    private final ClassLoader classLoader;

    public CAL10NBundleFinderByClassloaderExt(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    protected URL getResource(String resourceCandidate) {
        File i18nFolder = new File(FileUtils.getUserFolder(), "i18n");
        File i18nFile = FileUtils.getDesireFile(i18nFolder, resourceCandidate);
        if (i18nFile != null) {
            try {
                return i18nFile.toURI().toURL();
            } catch (MalformedURLException e) {
                LOG.error("Except to get resource file", e);
                return classLoader.getResource(resourceCandidate);
            }
        } else {
            return classLoader.getResource(resourceCandidate);
        }
    }
}
