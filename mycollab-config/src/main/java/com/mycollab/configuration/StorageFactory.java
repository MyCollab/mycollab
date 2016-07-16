/**
 * This file is part of mycollab-config.
 *
 * mycollab-config is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-config is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-config.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.configuration;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class StorageFactory {
    private static Logger LOG = LoggerFactory.getLogger(StorageFactory.class);

    private static final String S3_CONF_CLS = "com.mycollab.ondemand.configuration.S3Storage";

    public static final String FILE_STORAGE_SYSTEM = "file";
    public static final String S3_STORAGE_SYSTEM = "s3";

    private static StorageFactory _instance = new StorageFactory();
    private Storage storage;

    private StorageFactory() {
        // Load storage configuration
        String storageSystem = ApplicationProperties.getString(ApplicationProperties.STORAGE_SYSTEM, FILE_STORAGE_SYSTEM);
        if (FILE_STORAGE_SYSTEM.equals(storageSystem)) {
            storage = FileStorage.getInstance();
        } else if (S3_STORAGE_SYSTEM.equals(storageSystem)) {
            try {
                Class<Storage> s3Conf = (Class<Storage>) Class.forName(S3_CONF_CLS);
                storage = s3Conf.newInstance();
            } catch (Exception e) {
                LOG.error(String.format("Can not load s3 file system with class %s", S3_CONF_CLS), e);
                System.exit(-1);
            }
        } else {
            throw new MyCollabException(String.format("Can not load storage  %s", storageSystem));
        }
    }

    public static Storage getInstance() {
        return _instance.storage;
    }

    public static String getResourcePath(String documentPath) {
        return SiteConfiguration.getResourceDownloadUrl() + documentPath;
    }

    public static String getLogoPath(Integer accountId, String logoName, int size) {
        if (StringUtils.isBlank(logoName)) {
            return StorageFactory.generateAssetRelativeLink("icons/logo.png");
        }
        return String.format("%s%d/.assets/%s_%d.png", SiteConfiguration.getResourceDownloadUrl(), accountId,
                logoName, size);
    }

    public static String getEntityLogoPath(Integer accountId, String id, Integer size) {
        return String.format("%s%d/.assets/%s_%d.png", SiteConfiguration.getResourceDownloadUrl(), accountId,
                id, size);
    }

    public static String getFavIconPath(Integer sAccountId, String favIconName) {
        if (StringUtils.isBlank(favIconName)) {
            return StorageFactory.generateAssetRelativeLink("favicon.ico");
        }
        return String.format("%s%d/.assets/%s.ico", SiteConfiguration.getResourceDownloadUrl(), sAccountId, favIconName);
    }

    public static String getAvatarPath(String userAvatarId, int size) {
        if (StringUtils.isBlank(userAvatarId)) {
            return StorageFactory.generateAssetRelativeLink(String.format("icons/default_user_avatar_%d.png", size));
        } else {
            return String.format("%savatar/%s_%d.png", SiteConfiguration.getResourceDownloadUrl(), userAvatarId, size);
        }
    }

    public static String generateAssetRelativeLink(String resourceId) {
        return _instance.storage.generateAssetLink(resourceId);
    }
}
