package ch.qos.cal10n;

import ch.qos.cal10n.util.CAL10NBundleExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
public class ResourceLoader {
    private static Logger LOG = LoggerFactory.getLogger(ResourceLoader.class);
    private WatchService watchService;
    private List<String> cacheWatchFolders = new ArrayList<>();
    private Map<String, CAL10NBundleExt> bundleMap = new ConcurrentHashMap<>();

    ResourceLoader() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            LOG.error("Error", e);
            return;
        }
        new Thread(()-> {
            while (true) {
                // Obtaining watch keys
                WatchKey key = null;
                try {
                    key = watchService.take();
                } catch (InterruptedException e) {
                    break;
                }
                if (key != null) {
                    for (WatchEvent<?> watchEvent : key.pollEvents()) {
                        final WatchEvent.Kind<?> kind = watchEvent.kind();
                        // Overflow event
                        if (StandardWatchEventKinds.OVERFLOW == kind) {
                            continue; // loop
                        } else if (StandardWatchEventKinds.ENTRY_MODIFY == kind) {
                            // A new Path was created
                            Path newPath = ((WatchEvent<Path>) watchEvent).context();
                            File changeFile = newPath.toFile();
                            CAL10NBundleExt cal10NBundleExt = bundleMap.get(changeFile.getName());
                            if (cal10NBundleExt != null) {
                                cal10NBundleExt.setChanged(true);
                            }
                        }
                    }
                    if (!key.reset()) {
                        break; //loop
                    }
                }
            }
        }).start();
    }

    void registerBundleAndFile(CAL10NBundleExt cal10NBundleExt) {
        File hostFile = cal10NBundleExt.getHostFile();
        if (hostFile != null) {
            File parentFile = hostFile.getParentFile();
            bundleMap.put(hostFile.getName(), cal10NBundleExt);
            if (!cacheWatchFolders.contains(parentFile.getAbsolutePath())) {
                cacheWatchFolders.add(parentFile.getAbsolutePath());
                Path folder = Paths.get(parentFile.getAbsolutePath());
                // Register event
                try {
                    folder.register(watchService,
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_MODIFY);
                } catch (IOException e) {
                    LOG.error("Error", e);
                }
            }
        }
    }
}
