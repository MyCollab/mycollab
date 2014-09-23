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

import java.io.File;

/**
 * Configuration of file system storage mode
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public final class FileStorageConfiguration implements StorageConfiguration {

	public static File baseContentFolder;

	static {
		String userFolder = System.getProperty("user.home");
		baseContentFolder = new File(userFolder + "/.mycollab");
		baseContentFolder.mkdirs();
	}

	FileStorageConfiguration() {
		File avatarFolder = new File(baseContentFolder, "avatar");
		File logoFolder = new File(baseContentFolder, "logo");
		avatarFolder.mkdirs();
		logoFolder.mkdirs();
	}

	@Override
	public String getAvatarPath(String userAvatarId, int size) {
		if (userAvatarId == null || "".equals(userAvatarId)) {
			return SiteConfiguration.getSiteUrl("app") + "avatar/null/" + size;
		}
		return SiteConfiguration.getSiteUrl("app") + "avatar/" + userAvatarId
				+ "/" + size;
	}

	@Override
	public String getLogoPath(String accountLogoId, int size) {
		if (accountLogoId == null || "".equals(accountLogoId)) {
			return MyCollabAssets.newResourceLink("icons/logo.png");
		}
		return SiteConfiguration.getSiteUrl("app") + "logo/" + accountLogoId
				+ "/" + size;
	}

	@Override
	public String getResourcePath(String documentPath) {
		return baseContentFolder.getPath() + "/" + documentPath;
	}

	public File getAvatarFile(String username, int size) {
		File userAvatarFile = new File(baseContentFolder, "/avatar/" + username
				+ "_" + size + ".png");
		if (userAvatarFile.exists()) {
			return userAvatarFile;
		} else {
			return null;
		}
	}

	public File getLogoFile(String logoId, int size) {
		File logoFile = new File(baseContentFolder, "/logo/" + logoId + "_"
				+ size + ".png");
		if (logoFile.exists()) {
			return logoFile;
		} else {
			return null;
		}
	}

}
