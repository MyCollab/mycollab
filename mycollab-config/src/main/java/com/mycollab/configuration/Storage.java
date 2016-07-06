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

import com.mycollab.core.utils.StringUtils;

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

    public String getResourcePath(String documentPath) {
        return SiteConfiguration.getResourceDownloadUrl() + documentPath;
    }

    public String getLogoPath(Integer accountId, String logoName, int size) {
        if (logoName == null || "".equals(logoName)) {
            return MyCollabAssets.newAssetLink("icons/logo.png");
        }
        return String.format("%s%d/.assets/%s_%d.png", SiteConfiguration.getResourceDownloadUrl(), accountId,
                logoName, size);
    }

    public static String getEntityLogoPath(Integer accountId, String id, Integer size) {
        return String.format("%s%d/.assets/%s_%d.png", SiteConfiguration.getResourceDownloadUrl(), accountId,
                id, size);
    }

    public String getFavIconPath(Integer sAccountId, String favIconName) {
        if (favIconName == null || "".equals(favIconName)) {
            return MyCollabAssets.newAssetLink("favicon.ico");
        }
        return String.format("%s%d/.assets/%s.ico", SiteConfiguration.getResourceDownloadUrl(), sAccountId, favIconName);
    }

    public String getAvatarPath(String userAvatarId, int size) {
        if (StringUtils.isBlank(userAvatarId)) {
            return MyCollabAssets.newAssetLink(String.format("icons/default_user_avatar_%d.png", size));
        } else {
            return String.format("%savatar/%s_%d.png", SiteConfiguration.getResourceDownloadUrl(), userAvatarId, size);
        }
    }

    public boolean isFileStorage() {
        return (this instanceof FileStorage);
    }

    public boolean isS3Storage() {
        return !isFileStorage();
    }
}
