package com.mycollab.module.file.service.impl

import com.mycollab.core.MyCollabException
import com.mycollab.core.UserInvalidInputException
import com.mycollab.core.utils.ImageUtil
import com.mycollab.module.ecm.domain.Content
import com.mycollab.module.ecm.service.ResourceService
import com.mycollab.module.file.PathUtils
import com.mycollab.module.file.service.AccountFavIconService
import com.mycollab.module.user.domain.BillingAccount
import com.mycollab.module.user.service.BillingAccountService
import net.sf.image4j.codec.ico.ICOEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.UUID

/**
 * @author MyCollab Ltd
 * @since 5.0.10
 */
@Service
class AccountFavIconServiceImpl(private val resourceService: ResourceService,
                                private val billingAccountService: BillingAccountService) : AccountFavIconService {

    override fun upload(uploadedUser: String, logo: BufferedImage, sAccountId: Int): String {
        var tempLogo = logo
        if (tempLogo.width != tempLogo.height) {
            val min = Math.min(tempLogo.width, tempLogo.height)
            tempLogo = tempLogo.getSubimage(0, 0, min, min)
        }
        val account = billingAccountService.getAccountById(sAccountId) ?: throw MyCollabException("There's no account associated with provided id $sAccountId")

        tempLogo = ImageUtil.scaleImage(tempLogo, 32, 32)
        // Construct new logoid
        val newLogoId = UUID.randomUUID().toString()
        val outStream = ByteArrayOutputStream()
        try {
            ICOEncoder.write(tempLogo, outStream)
        } catch (e: IOException) {
            throw UserInvalidInputException("Can not convert file to ico format", e)
        }

        val logoContent = Content()
        logoContent.path = PathUtils.buildFavIconPath(sAccountId, newLogoId)
        logoContent.name = newLogoId
        resourceService.saveContent(logoContent, uploadedUser, ByteArrayInputStream(outStream.toByteArray()), null)

        //remove the old favicon
        resourceService.removeResource(PathUtils.buildFavIconPath(sAccountId, account.faviconpath),
                uploadedUser, true, sAccountId)

        account.faviconpath = newLogoId
        billingAccountService.updateSelectiveWithSession(account, uploadedUser)

        return newLogoId
    }
}
