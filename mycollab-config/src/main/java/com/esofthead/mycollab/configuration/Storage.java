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
package com.esofthead.mycollab.configuration;

import com.esofthead.mycollab.core.MyCollabException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File configuration for storage file in MyCollab. We support two kinds of file
 * system:
 * <ul>
 * <li>S3 mode: Files are stored in Amazon S3. This is used for MyCollab cloud
 * service.</li>
 * <li>File system: Files are stored in OS file storage. This is used for
 * installation mode of MyCollab</li>
 * </ul>
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class Storage {
    private static Logger LOG = LoggerFactory.getLogger(Storage.class);
    private static final String S3_CONF_CLS = "com.esofthead.mycollab.ondemand.configuration.S3Storage";

    public static final String FILE_STORAGE_SYSTEM = "file";

    public static final String S3_STORAGE_SYSTEM = "s3";

    private static Storage instance;

    static {
        // Load storage configuration
        String storageSystem = ApplicationProperties.getString(ApplicationProperties.STORAGE_SYSTEM,
                Storage.FILE_STORAGE_SYSTEM);
        if (Storage.FILE_STORAGE_SYSTEM.equals(storageSystem)) {
            instance = new FileStorage();
        } else if (Storage.S3_STORAGE_SYSTEM.equals(storageSystem)) {
            try {
                Class<Storage> s3Conf = (Class<Storage>) Class.forName(S3_CONF_CLS);
                instance = s3Conf.newInstance();
            } catch (Exception e) {
                LOG.error(String.format("Can not load s3 file system with class %s", S3_CONF_CLS), e);
                System.exit(-1);
            }
        } else {
            throw new MyCollabException(String.format("Can not load storage  %s", storageSystem));
        }
    }

    public static Storage getInstance() {
        return instance;
    }

    public static String getResourcePath(String documentPath) {
        return SiteConfiguration.getResourceDownloadUrl() + documentPath;
    }

    public static String getLogoPath(Integer accountId, String logoName, int size) {
        if (logoName == null || "".equals(logoName)) {
            return MyCollabAssets.newAssetLink("icons/logo.png");
        }
        return String.format("%s%d/.assets/%s_%d.png", SiteConfiguration.getResourceDownloadUrl(), accountId,
                logoName, size);
    }

    public static String getFavIconPath(Integer sAccountId, String favIconName) {
        if (favIconName == null || "".equals(favIconName)) {
            return MyCollabAssets.newAssetLink("favicon.ico");
        }
        return String.format("%s%d/.assets/%s.ico", SiteConfiguration.getResourceDownloadUrl(), sAccountId, favIconName);
    }

    public static String getAvatarPath(String userAvatarId, int size) {
        if (StringUtils.isBlank(userAvatarId)) {
            return MyCollabAssets.newAssetLink(String.format("icons/default_user_avatar_%d.png", size));
        } else {
            return String.format("%savatar/%s_%d.png", SiteConfiguration.getResourceDownloadUrl(), userAvatarId, size);
        }
    }

    public static boolean isFileStorage() {
        return (instance instanceof FileStorage);
    }

    public static boolean isS3Storage() {
        return !isFileStorage();
    }
}
