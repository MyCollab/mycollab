package com.mycollab.community.module.file.service.impl

import com.mycollab.core.Version
import com.mycollab.core.utils.FileUtils
import com.mycollab.module.file.service.AbstractStorageService
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.io.File

/**
 * @author MyCollab Ltd
 * @since 5.5.0
 */
@Service
open class FileStorageServiceImpl : AbstractStorageService(), InitializingBean {

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        val baseContentFolder = FileUtils.homeFolder
        val avatarFolder = File(baseContentFolder, "avatar")
        val logoFolder = File(baseContentFolder, "logo")
        FileUtils.mkdirs(avatarFolder)
        FileUtils.mkdirs(logoFolder)
    }

    override fun generateAssetRelativeLink(resourceId: String): String =
            "${deploymentMode!!.getCdnUrl()}$resourceId?v=${Version.getVersion()}"
}
