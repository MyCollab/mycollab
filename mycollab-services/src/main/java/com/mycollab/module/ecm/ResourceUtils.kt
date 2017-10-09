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
    fun getExternalResourceService(resourceType: ResourceType): ExternalResourceService {
        return when {
            ResourceType.Dropbox === resourceType -> AppContextUtil.getSpringBean(DropboxResourceService::class.java)
            else -> throw MyCollabException("Current support only dropbox resource service")
        }
    }

    /**
     * @param resource
     * @return
     */
    @JvmStatic
    fun getExternalDrive(resource: Resource): ExternalDrive? {
        when (resource) {
            is ExternalFolder -> return resource.externalDrive
            is ExternalContent -> return resource.externalDrive
            else -> return null
        }
    }

    /**
     * @param resource
     * @return
     */
    @JvmStatic
    fun getType(resource: Resource): ResourceType {
        return if (!resource.isExternalResource) {
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
}
