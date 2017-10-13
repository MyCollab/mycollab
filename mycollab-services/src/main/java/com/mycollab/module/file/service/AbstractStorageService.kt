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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.file.service

import com.mycollab.configuration.IDeploymentMode
import com.mycollab.core.utils.StringUtils
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author MyCollab Ltd
 * @since 5.5.0
 */
abstract class AbstractStorageService {

    @Autowired
    open protected lateinit var deploymentMode: IDeploymentMode

    open fun getResourcePath(documentPath: String): String =
            deploymentMode.getResourceDownloadUrl() + documentPath

    open fun getLogoPath(accountId: Int, logoName: String?, size: Int): String =
            when {
                StringUtils.isBlank(logoName) -> generateAssetRelativeLink("icons/logo.png")
                else -> "${deploymentMode.getResourceDownloadUrl()}$accountId/.assets/${logoName}_$size.png"
            }

    open fun getEntityLogoPath(accountId: Int, id: String, size: Int): String =
            "${deploymentMode.getResourceDownloadUrl()}$accountId/.assets/${id}_$size.png"

    open fun getFavIconPath(sAccountId: Int, favIconName: String?): String =
            when {
                StringUtils.isBlank(favIconName) -> generateAssetRelativeLink("favicon.ico")
                else -> "${deploymentMode.getResourceDownloadUrl()}$sAccountId/.assets/$favIconName.ico"
            }

    open fun getAvatarPath(userAvatarId: String?, size: Int): String =
            when {
                StringUtils.isBlank(userAvatarId) -> generateAssetRelativeLink("icons/default_user_avatar_$size.png")
                else -> "${deploymentMode.getResourceDownloadUrl()}avatar/${userAvatarId}_$size.png"
            }

    abstract fun generateAssetRelativeLink(resourceId: String): String
}
