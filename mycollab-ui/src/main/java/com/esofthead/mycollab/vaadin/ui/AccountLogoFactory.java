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
package com.esofthead.mycollab.vaadin.ui;

import java.io.File;

import com.esofthead.mycollab.configuration.FileStorageConfiguration;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Image;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class AccountLogoFactory {
	public static Image createAccountLogoImageComponent(String logoId, int size) {
		Image logo = new Image(null, createLogoResource(logoId, size));
		return logo;

	}

	public static String getLogoLink(String logoId, int size) {
		return SiteConfiguration.getStorageConfiguration().generateAvatarPath(
				logoId, size);
	}

	public static Resource createLogoResource(String logoId, int size) {
		Resource logoRes = null;

		if (logoId == null) {
			return MyCollabResource.newResource("icons/logo.png");
		}

		if (SiteConfiguration.isSupportFileStorage()) {
			FileStorageConfiguration fileStorageConfiguration = (FileStorageConfiguration) SiteConfiguration
					.getStorageConfiguration();
			File logoFile = fileStorageConfiguration.getLogoFile(logoId, size);
			if (logoFile != null) {
				logoRes = new FileResource(logoFile);
			} else {
				logoRes = MyCollabResource.newResource("icons/logo.png");
			}

		} else if (SiteConfiguration.isSupportS3Storage()) {
			logoRes = new ExternalResource(SiteConfiguration
					.getStorageConfiguration().generateLogoPath(logoId, size));
		}

		return logoRes;
	}
}
