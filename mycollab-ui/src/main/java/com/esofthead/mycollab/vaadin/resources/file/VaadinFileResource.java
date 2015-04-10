/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.resources.file;

import com.esofthead.mycollab.configuration.FileStorageConfiguration;
import com.esofthead.mycollab.configuration.StorageConfiguration;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.vaadin.resources.VaadinResource;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;

import java.io.File;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.1
 *
 */
public class VaadinFileResource implements VaadinResource {

	@Override
	public Resource getStreamResource(String documentPath) {
		return new FileStreamDownloadResource(documentPath);
	}

	@Override
	public Resource getImagePreviewResource(String documentPath, Resource failOverSource) {
		StorageConfiguration storageConfiguration = StorageManager.getConfiguration();
		File docFile = new File(storageConfiguration.getResourcePath(documentPath));
		return (docFile.exists()) ? new FileResource(docFile) : failOverSource;
	}

	@Override
	public Resource getLogoResource(String logoId, int size) {
		FileStorageConfiguration fileStorageConfiguration = (FileStorageConfiguration) StorageManager
				.getConfiguration();
		File logoFile = fileStorageConfiguration.getLogoFile(logoId, size);
		return (logoFile != null) ? new FileResource(logoFile)
				: MyCollabResource.newResource("icons/logo.png");
	}

	@Override
	public Resource getAvatarResource(String avatarId, int size) {
		FileStorageConfiguration fileStorageConfiguration = (FileStorageConfiguration) StorageManager
				.getConfiguration();
		File avatarFile = fileStorageConfiguration.getAvatarFile(avatarId, size);
		return (avatarFile != null) ? new FileResource(avatarFile)
				: MyCollabResource.newResource("icons/default_user_avatar_"
						+ size + ".png");
	}
}
