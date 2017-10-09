package com.mycollab.module.mail

import com.mycollab.configuration.IDeploymentMode
import com.mycollab.module.file.service.AbstractStorageService
import com.mycollab.module.user.service.BillingAccountService
import com.mycollab.spring.AppContextUtil

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object MailUtils {
    @JvmStatic
    fun getSiteUrl(sAccountId: Int): String {
        var siteUrl = ""
        val mode = AppContextUtil.getSpringBean(IDeploymentMode::class.java)
        if (mode.isDemandEdition) {
            val billingAccountService = AppContextUtil.getSpringBean(BillingAccountService::class.java)
            val account = billingAccountService.getAccountById(sAccountId)
            if (account != null) siteUrl = mode.getSiteUrl(account.subdomain)
        } else siteUrl = mode.getSiteUrl("")
        return siteUrl
    }

    @JvmStatic
    fun getAvatarLink(userAvatarId: String, size: Int): String = AppContextUtil.getSpringBean(AbstractStorageService::class.java).getAvatarPath(userAvatarId, size)
}