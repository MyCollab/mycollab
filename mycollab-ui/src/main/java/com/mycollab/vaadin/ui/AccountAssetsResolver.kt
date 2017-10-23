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
import com.vaadin.ui.Button
import com.vaadin.ui.themes.BaseTheme
import org.vaadin.viritin.button.MButton

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
object AccountAssetsResolver {
    @JvmStatic
    fun createAccountLogoImageComponent(logoId: String, size: Int): Button = MButton().
            withStyleName(BaseTheme.BUTTON_LINK).withIcon(createLogoResource(logoId, size))

    @JvmStatic
    fun createLogoResource(logoId: String?, size: Int): Resource = if (logoId == null) {
        ExternalResource(AppContextUtil.getSpringBean(AbstractStorageService::class.java)
                .generateAssetRelativeLink("icons/logo.png"))
    } else VaadinResourceFactory.getLogoResource(logoId, size)
}
