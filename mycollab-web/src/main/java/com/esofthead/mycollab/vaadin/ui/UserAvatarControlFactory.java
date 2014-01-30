/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.ui;

import java.io.File;

import com.esofthead.mycollab.configuration.FileStorageConfiguration;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.web.MyCollabResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class UserAvatarControlFactory {
	public static Embedded createUserAvatarEmbeddedComponent(String avatarId,
			int size) {
		Embedded embedded = new Embedded(null);
		embedded.setSource(createAvatarResource(avatarId, size));
		return embedded;

	}

	public static String getAvatarLink(String userAvatarId, int size) {
		if (userAvatarId == null) {
			return "";
		}

		String link = SiteConfiguration.getStorageConfiguration()
				.generateAvatarPath(userAvatarId, size);

		return link;
	}

	public static Resource createAvatarResource(String avatarId, int size) {
		Resource avatarRes = null;

		if (avatarId == null) {
			return MyCollabResource.newResource("icons/default_user_avatar_"
					+ size + ".png");
		}

		if (SiteConfiguration.isSupportFileStorage()) {
			FileStorageConfiguration fileStorageConfiguration = (FileStorageConfiguration) SiteConfiguration
					.getStorageConfiguration();
			File avatarFile = fileStorageConfiguration.getAvatarFile(avatarId,
					size);
			if (avatarFile != null) {
				avatarRes = new FileResource(avatarFile);
			} else {
				avatarRes = MyCollabResource
						.newResource("icons/default_user_avatar_" + size
								+ ".png");
			}

		} else if (SiteConfiguration.isSupportS3Storage()) {
			avatarRes = new ExternalResource(SiteConfiguration
					.getStorageConfiguration().generateAvatarPath(avatarId,
							size));
		}

		return avatarRes;
	}

	public static Button createUserAvatarButtonLink(String userAvatarId,
			String fullName) {
		Button button = new Button();
		button.setIcon(createAvatarResource(userAvatarId, 48));
		button.setStyleName("link");
		return button;
	}
}
