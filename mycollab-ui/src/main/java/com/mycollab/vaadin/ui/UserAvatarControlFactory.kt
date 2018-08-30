/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.ui

import com.mycollab.module.file.service.AbstractStorageService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.resources.VaadinResourceFactory
import com.vaadin.server.ExternalResource
import com.vaadin.server.Resource
import com.vaadin.ui.Image

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object UserAvatarControlFactory {
    @JvmStatic
    fun createUserAvatarEmbeddedComponent(avatarId: String?, size: Int) =
            Image(null, createAvatarResource(avatarId, size))

    @JvmStatic
    fun createUserAvatarEmbeddedComponent(avatarId: String?, size: Int, tooltip: String): Image {
        val embedded = Image(null, createAvatarResource(avatarId, size))
        embedded.description = tooltip
        return embedded
    }

    @JvmStatic
    fun createAvatarResource(avatarId: String?, size: Int): Resource = if (avatarId == null) {
        ExternalResource(AppContextUtil.getSpringBean(AbstractStorageService::class.java)
                .generateAssetRelativeLink(String.format("icons/default_user_avatar_%d.png", size)))
    } else VaadinResourceFactory.getAvatarResource(avatarId, size)
}
