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
    open protected var deploymentMode: IDeploymentMode? = null

    open fun getResourcePath(documentPath: String): String =
            deploymentMode!!.getResourceDownloadUrl() + documentPath

    open fun getLogoPath(accountId: Int, logoName: String?, size: Int): String =
            when {
                StringUtils.isBlank(logoName) -> generateAssetRelativeLink("icons/logo.png")
                else -> "${deploymentMode!!.getResourceDownloadUrl()}$accountId/.assets/${logoName}_$size.png"
            }

    open fun getEntityLogoPath(accountId: Int, id: String, size: Int): String =
            "${deploymentMode!!.getResourceDownloadUrl()}$accountId/.assets/${id}_$size.png"

    open fun getFavIconPath(sAccountId: Int, favIconName: String?): String =
            when {
                StringUtils.isBlank(favIconName) -> generateAssetRelativeLink("favicon.ico")
                else -> "${deploymentMode!!.getResourceDownloadUrl()}$sAccountId/.assets/$favIconName.ico"
            }

    open fun getAvatarPath(userAvatarId: String?, size: Int): String =
            when {
                StringUtils.isBlank(userAvatarId) -> generateAssetRelativeLink(String.format("icons/default_user_avatar_%d.png", size))
                else -> "${deploymentMode!!.getResourceDownloadUrl()}avatar/${userAvatarId}_$size.png"
            }

    abstract fun generateAssetRelativeLink(resourceId: String): String
}
