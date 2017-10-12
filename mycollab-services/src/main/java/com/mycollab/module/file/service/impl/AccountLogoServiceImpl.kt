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
import com.mycollab.module.ecm.domain.Content
import com.mycollab.module.ecm.service.ResourceService
import com.mycollab.module.file.PathUtils
import com.mycollab.module.file.service.AccountLogoService
import com.mycollab.module.user.service.BillingAccountService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@Service
class AccountLogoServiceImpl(private val resourceService: ResourceService,
                             private val billingAccountService: BillingAccountService) : AccountLogoService {

    override fun upload(uploadedUser: String, logo: BufferedImage, sAccountId: Int): String {
        val account = billingAccountService.getAccountById(sAccountId) ?: throw MyCollabException("There's no account associated with provided id $sAccountId")

        // Construct new logoid
        val newLogoId = UUID.randomUUID().toString()

        SUPPORT_SIZES.forEach { uploadLogoToStorage(uploadedUser, logo, newLogoId, it, sAccountId) }

        // account old logo
        if (account.logopath != null) {
            SUPPORT_SIZES.forEach {
                try {
                    resourceService.removeResource(PathUtils.buildLogoPath(sAccountId, account.logopath, it),
                            uploadedUser, true, sAccountId)
                } catch (e: Exception) {
                    LOG.error("Error while delete old logo", e)
                }
            }
        }

        // save logo id
        account.logopath = newLogoId
        billingAccountService.updateSelectiveWithSession(account, uploadedUser)

        return newLogoId
    }

    private fun uploadLogoToStorage(uploadedUser: String, image: BufferedImage, logoId: String, width: Int, sAccountId: Int?) {
        val scaleImage = ImageUtil.scaleImage(image, width.toFloat() / image.width)
        val outStream = ByteArrayOutputStream()
        try {
            ImageIO.write(scaleImage, "png", outStream)
        } catch (e: IOException) {
            throw MyCollabException("Error while write image to stream", e)
        }

        val logoContent = Content()
        logoContent.path = PathUtils.buildLogoPath(sAccountId, logoId, width)
        logoContent.name = "${logoId}_$width"
        resourceService.saveContent(logoContent, uploadedUser, ByteArrayInputStream(outStream.toByteArray()), null)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(AccountLogoServiceImpl::class.java)
        private val SUPPORT_SIZES = intArrayOf(150, 100, 64)
    }
}