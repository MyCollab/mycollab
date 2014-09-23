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

import com.esofthead.mycollab.vaadin.resources.VaadinResourceManager;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class UserAvatarControlFactory {
	public static Image createUserAvatarEmbeddedComponent(String avatarId,
			int size) {
		Image embedded = new Image(null, createAvatarResource(avatarId, size));
		return embedded;

	}

	public static Resource createAvatarResource(String avatarId, int size) {
		if (avatarId == null) {
			return MyCollabResource.newResource("icons/default_user_avatar_"
					+ size + ".png");
		}
		return VaadinResourceManager.getResourceManager().getAvatarResource(
				avatarId, size);
	}

	public static Button createUserAvatarButtonLink(String userAvatarId,
			String fullName) {
		Button button = new Button();
		button.setIcon(createAvatarResource(userAvatarId, 48));
		button.setStyleName("link");
		return button;
	}
}
