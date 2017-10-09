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
