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
package com.mycollab.module.file

import com.mycollab.module.file.service.AbstractStorageService
import com.mycollab.spring.AppContextUtil

/**
 * @author MyCollab Ltd
 * @since 5.5.0
 */
object StorageUtils {
    @JvmStatic
    fun getResourcePath(documentPath: String): String =
            AppContextUtil.getSpringBean(AbstractStorageService::class.java).getResourcePath(documentPath)

    @JvmStatic
    fun getLogoPath(accountId: Int, logoName: String, size: Int): String =
            AppContextUtil.getSpringBean(AbstractStorageService::class.java).getLogoPath(accountId, logoName, size)

    @JvmStatic
    fun getEntityLogoPath(accountId: Int, id: String, size: Int): String =
            AppContextUtil.getSpringBean(AbstractStorageService::class.java).getEntityLogoPath(accountId, id, size)

    @JvmStatic
    fun getFavIconPath(sAccountId: Int, favIconName: String?): String =
            AppContextUtil.getSpringBean(AbstractStorageService::class.java).getFavIconPath(sAccountId, favIconName)

    @JvmStatic
    fun getAvatarPath(userAvatarId: String?, size: Int): String =
            AppContextUtil.getSpringBean(AbstractStorageService::class.java).getAvatarPath(userAvatarId, size)

    @JvmStatic
    fun generateAssetRelativeLink(resourceId: String): String =
            AppContextUtil.getSpringBean(AbstractStorageService::class.java).generateAssetRelativeLink(resourceId)
}
