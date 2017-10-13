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
package com.mycollab.module.ecm

import com.mycollab.core.MyCollabException
import com.mycollab.module.ecm.domain.ExternalContent
import com.mycollab.module.ecm.domain.ExternalDrive
import com.mycollab.module.ecm.domain.ExternalFolder
import com.mycollab.module.ecm.domain.Resource
import com.mycollab.module.ecm.service.DropboxResourceService
import com.mycollab.module.ecm.service.ExternalResourceService
import com.mycollab.spring.AppContextUtil
import org.apache.commons.beanutils.PropertyUtils

/**
 * Utility class of processing MyCollab resources.
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
object ResourceUtils {

    /**
     * @param resourceType
     * @return
     */
    @JvmStatic
    fun getExternalResourceService(resourceType: ResourceType): ExternalResourceService =
            when {
                ResourceType.Dropbox === resourceType -> AppContextUtil.getSpringBean(DropboxResourceService::class.java)
                else -> throw MyCollabException("Current support only dropbox resource service")
            }

    /**
     * @param resource
     * @return
     */
    @JvmStatic
    fun getExternalDrive(resource: Resource): ExternalDrive? =
            when (resource) {
                is ExternalFolder -> resource.externalDrive
                is ExternalContent -> resource.externalDrive
                else -> null
            }

    /**
     * @param resource
     * @return
     */
    @JvmStatic
    fun getType(resource: Resource): ResourceType =
            if (!resource.isExternalResource) {
                ResourceType.MyCollab
            } else {
                try {
                    val storageName = PropertyUtils.getProperty(resource, "storageName") as String
                    when (storageName) {
                        StorageNames.DROPBOX -> ResourceType.Dropbox
                        else -> throw Exception("Current support only dropbox resource service")
                    }
                } catch (e: Exception) {
                    throw MyCollabException("Can not define storage name of bean $resource")
                }
            }
}
