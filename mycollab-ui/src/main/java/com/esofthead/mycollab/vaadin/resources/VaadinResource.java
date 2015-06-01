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
package com.esofthead.mycollab.vaadin.resources;

import com.esofthead.mycollab.configuration.StorageConfiguration;
import com.esofthead.mycollab.configuration.StorageManager;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.1
 *
 */
public abstract class VaadinResource {

	public abstract Resource getStreamResource(String documentPath);

	public Resource getImagePreviewResource(String documentPath) {
		StorageConfiguration storageConfiguration = StorageManager.getConfiguration();
		return new ExternalResource(storageConfiguration.getResourcePath(documentPath));
	}

	public Resource getLogoResource(String logoId, int size) {
		return new ExternalResource(StorageManager.getConfiguration().getLogoPath(logoId, size));
	}

	public Resource getAvatarResource(String avatarId, int size) {
		return new ExternalResource(StorageManager.getConfiguration().getAvatarPath(avatarId, size));
	}
}
