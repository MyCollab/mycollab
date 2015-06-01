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
 * 
 */
public abstract class StorageConfiguration {
	public static final String FILE_STORAGE_SYSTEM = "file";

    public static final String S3_STORAGE_SYSTEM = "s3";

    public String getAvatarPath(String userAvatarId, int size) {
        String resourceDownloadPath = SiteConfiguration.getResourceDownloadUrl();
        if (StringUtils.isBlank(userAvatarId)) {
            return MyCollabAssets.newResourceLink(String.format("icons/default_user_avatar_%d.png", size));
        } else {
            return String.format("%savatar/%s_%d.png", resourceDownloadPath, userAvatarId, size);
        }
    }

	public String getResourcePath(String documentPath) {
        return SiteConfiguration.getResourceDownloadUrl() + documentPath;
	}

	public String getLogoPath(String accountLogoId, int size) {
		if (accountLogoId == null || "".equals(accountLogoId)) {
			return MyCollabAssets.newResourceLink("icons/logo.png");
		}
        return String.format("%slogo/%s_%d.png", SiteConfiguration.getResourceDownloadUrl(), accountLogoId, size);
	}
}
