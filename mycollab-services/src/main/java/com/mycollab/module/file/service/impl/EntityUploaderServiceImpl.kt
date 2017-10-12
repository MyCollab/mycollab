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
package com.mycollab.module.file.service.impl

import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.ImageUtil
import com.mycollab.core.utils.StringUtils
import com.mycollab.module.ecm.domain.Content
import com.mycollab.module.ecm.service.ResourceService
import com.mycollab.module.file.service.EntityUploaderService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
@Service
class EntityUploaderServiceImpl(private val resourceService: ResourceService) : EntityUploaderService {

    override fun upload(image: BufferedImage, basePath: String, oldId: String?, uploadedUser: String,
                        sAccountId: Int?, preferSizes: Array<Int>): String {
        // Construct new logoid
        val newLogoId = GregorianCalendar().timeInMillis.toString() + UUID.randomUUID().toString()

        for (preferSize in preferSizes) {
            uploadLogoToStorage(uploadedUser, image, basePath, newLogoId, preferSize)
        }

        if (StringUtils.isNotBlank(oldId)) {
            preferSizes.forEach {
                try {
                    resourceService.removeResource("$basePath/${oldId}_$it.png",
                            uploadedUser, true, sAccountId)
                } catch (e: Exception) {
                    LOG.error("Error while delete old logo", e)
                }
            }
        }

        return newLogoId
    }

    private fun uploadLogoToStorage(uploadedUser: String, image: BufferedImage, basePath: String, logoId: String, width: Int) {
        val scaleImage = ImageUtil.scaleImage(image, width.toFloat() / image.width)
        val outStream = ByteArrayOutputStream()
        try {
            ImageIO.write(scaleImage, "png", outStream)
        } catch (e: IOException) {
            throw MyCollabException("Error while write image to stream", e)
        }

        val logoContent = Content()
        logoContent.path = "$basePath/${logoId}_$width.png"
        logoContent.name = "${logoId}_$width"
        resourceService.saveContent(logoContent, uploadedUser, ByteArrayInputStream(outStream.toByteArray()), null)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(EntityUploaderServiceImpl::class.java)
    }
}
