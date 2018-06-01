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
package com.mycollab.module.ecm.service.impl

import com.google.common.eventbus.AsyncEventBus
import com.mycollab.core.MyCollabException
import com.mycollab.core.UserInvalidInputException
import com.mycollab.core.utils.FileUtils
import com.mycollab.core.utils.ImageUtil
import com.mycollab.core.utils.MimeTypesUtil
import com.mycollab.core.utils.StringUtils
import com.mycollab.module.billing.service.BillingPlanCheckerService
import com.mycollab.module.ecm.domain.Content
import com.mycollab.module.ecm.domain.Folder
import com.mycollab.module.ecm.domain.Resource
import com.mycollab.module.ecm.esb.DeleteResourcesEvent
import com.mycollab.module.ecm.esb.SaveContentEvent
import com.mycollab.module.ecm.service.ContentJcrDao
import com.mycollab.module.ecm.service.ResourceService
import com.mycollab.module.file.service.RawContentService
import org.apache.commons.collections.CollectionUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.*
import java.util.*
import javax.imageio.ImageIO

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Service
open class ResourceServiceImpl(private val contentJcrDao: ContentJcrDao,
                          private val rawContentService: RawContentService,
                          private val billingPlanCheckerService: BillingPlanCheckerService,
                          private val asyncEventBus: AsyncEventBus) : ResourceService {

    override fun getResources(path: String): List<Resource> {
        val resources = contentJcrDao.getResources(path)
        if (CollectionUtils.isNotEmpty(resources)) {
            Collections.sort(resources)
            return resources
        }

        return listOf()
    }

    override fun getContents(path: String): List<Content> = contentJcrDao.getContents(path)

    override fun getSubFolders(path: String): List<Folder> = contentJcrDao.getSubFolders(path)

    override fun createNewFolder(baseFolderPath: String, folderName: String, description: String, createdBy: String): Folder {
        if (FileUtils.isValidFileName(folderName)) {
            val folderPath = "$baseFolderPath/$folderName"
            val folder = Folder(folderPath)
            folder.name = folderName
            folder.description = description
            folder.createdBy = createdBy
            folder.created = GregorianCalendar()
            contentJcrDao.createFolder(folder, createdBy)
            return folder
        }
        throw UserInvalidInputException("Invalid file name")
    }

    override fun saveContent(content: Content, createdUser: String, refStream: InputStream, sAccountId: Int?) {
        var fileSize: Int? = 0
        if (sAccountId != null) {
            try {
                fileSize = refStream.available()
                billingPlanCheckerService.validateAccountCanUploadMoreFiles(sAccountId, fileSize.toLong())
            } catch (e: IOException) {
                LOG.error("Can not get available bytes", e)
            }
        }

        // detect mimeType and set to content
        val mimeType = MimeTypesUtil.detectMimeType(content.path)
        content.mimeType = mimeType
        content.size = java.lang.Long.valueOf(fileSize!!.toLong())

        val contentPath = content.path
        rawContentService.saveContent(contentPath, refStream)

        if (MimeTypesUtil.isImage(mimeType)) {
            try {
                rawContentService.getContentStream(contentPath).use { newInputStream ->
                    val image = ImageUtil.generateImageThumbnail(newInputStream)
                    if (image != null) {
                        val thumbnailPath = ".thumbnail/$sAccountId/${StringUtils.generateSoftUniqueId()}.png"
                        val tmpFile = File.createTempFile("tmp", "png")
                        ImageIO.write(image, "png", FileOutputStream(tmpFile))
                        rawContentService.saveContent(thumbnailPath, FileInputStream(tmpFile))
                        content.thumbnail = thumbnailPath
                    }
                }
            } catch (e: IOException) {
                LOG.error("Error when generating thumbnail", e)
            }
        }

        contentJcrDao.saveContent(content, createdUser)

        val event = SaveContentEvent(content, createdUser, sAccountId)
        asyncEventBus.post(event)
    }

    override fun removeResource(path: String) {
        contentJcrDao.removeResource(path)
        rawContentService.removePath(path)
    }

    override fun removeResource(path: String, userDelete: String, isUpdateDriveInfo: Boolean?, sAccountId: Int?) {
        val res = contentJcrDao.getResource(path) ?: return

        val event = when (res) {
            is Folder -> DeleteResourcesEvent(arrayOf(path), userDelete, isUpdateDriveInfo!!, sAccountId)
            else -> DeleteResourcesEvent(arrayOf(path, (res as Content).thumbnail), userDelete, isUpdateDriveInfo!!, sAccountId)
        }
        asyncEventBus.post(event)
        contentJcrDao.removeResource(path)
    }

    override fun getContentStream(path: String): InputStream = rawContentService.getContentStream(path)

    override fun rename(oldPath: String, newPath: String, userUpdate: String) {
        contentJcrDao.rename(oldPath, newPath)
        rawContentService.renamePath(oldPath, newPath)
    }

    override fun searchResourcesByName(baseFolderPath: String, resourceName: String): List<Resource> =
            contentJcrDao.searchResourcesByName(baseFolderPath, resourceName)

    override fun moveResource(oldPath: String, newPath: String, userMove: String) {
        val oldResourceName = oldPath.substring(oldPath.lastIndexOf("/") + 1, oldPath.length)

        val oldResource = contentJcrDao.getResource(oldPath)

        if (oldResource is Folder && newPath.contains(oldPath)) {
            throw UserInvalidInputException("Can not move asset(s) to folder $newPath")
        } else {
            val destinationPath = newPath + "/" + oldResourceName
            contentJcrDao.moveResource(oldPath, destinationPath)
            rawContentService.movePath(oldPath, destinationPath)
        }
    }

    override fun getParentFolder(path: String): Folder? {
        try {
            val parentPath = path.substring(0, path.lastIndexOf("/"))
            val res = contentJcrDao.getResource(parentPath)
            return res as? Folder
        } catch (e: Exception) {
            throw MyCollabException(e)
        }

    }

    override fun getResource(path: String): Resource? = contentJcrDao.getResource(path)

    companion object {
        private val LOG = LoggerFactory.getLogger(ResourceServiceImpl::class.java)
    }
}
