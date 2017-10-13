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
            "${deploymentMode.getCdnUrl()}$resourceId?v=${Version.getVersion()}"
}
