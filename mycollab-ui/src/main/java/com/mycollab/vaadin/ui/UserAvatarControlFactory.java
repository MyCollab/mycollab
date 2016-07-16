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
package com.mycollab.vaadin.ui;

import com.mycollab.configuration.StorageFactory;
import com.mycollab.vaadin.resources.VaadinResourceFactory;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Image;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UserAvatarControlFactory {
    public static Image createUserAvatarEmbeddedComponent(String avatarId, int size) {
        return new Image(null, createAvatarResource(avatarId, size));
    }

    public static Image createUserAvatarEmbeddedComponent(String avatarId, int size, String tooltip) {
        Image embedded = new Image(null, createAvatarResource(avatarId, size));
        embedded.setDescription(tooltip);
        return embedded;
    }

    public static Resource createAvatarResource(String avatarId, int size) {
        if (avatarId == null) {
            return new ExternalResource(StorageFactory.generateAssetRelativeLink((String.format("icons/default_user_avatar_%d.png", size))));
        }
        return VaadinResourceFactory.getAvatarResource(avatarId, size);
    }
}
