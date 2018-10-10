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
package com.mycollab.vaadin.resources

import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.MyCollabException
import com.mycollab.module.file.service.AbstractStorageService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.resources.file.VaadinFileResource
import com.vaadin.server.ExternalResource
import com.vaadin.server.Resource

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
class VaadinResourceFactory private constructor() {
    private var vaadinResource: VaadinResource? = null

    init {
        vaadinResource = if (SiteConfiguration.isDemandEdition()) {
            try {
                val cls = Class.forName(S3_CLS) as Class<VaadinResource>
                cls.newInstance()
            } catch (e: Exception) {
                throw MyCollabException("Exception when load s3 resource file", e)
            }

        } else {
            VaadinFileResource()
        }
    }

    companion object {
        private val S3_CLS = "com.mycollab.ondemand.vaadin.resources.s3.VaadinS3Resource"

        private val _instance = VaadinResourceFactory()

        @JvmStatic
        val instance: VaadinResource?
            get() = _instance.vaadinResource

        @JvmStatic
        fun getResource(documentPath: String) =
                ExternalResource(AppContextUtil.getSpringBean(AbstractStorageService::class.java)
                        .getResourcePath(documentPath))

        @JvmStatic
        fun getLogoResource(logoId: String, size: Int) =
                ExternalResource(AppContextUtil.getSpringBean(AbstractStorageService::class.java)
                        .getLogoPath(AppUI.accountId, logoId, size))

        @JvmStatic
        fun getAvatarResource(avatarId: String, size: Int) =
                ExternalResource(AppContextUtil.getSpringBean(AbstractStorageService::class.java)
                        .getAvatarPath(avatarId, size))
    }
}
