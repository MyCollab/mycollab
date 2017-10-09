package com.mycollab.module.user.ui.format

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.utils.StringUtils.isBlank
import com.mycollab.html.FormatUtils
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.UserUIContext
import com.mycollab.vaadin.ui.formatter.HistoryFieldFormat

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
class UserHistoryFieldFormat : HistoryFieldFormat {

    override fun toString(value: String): String {
        return toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY))
    }

    override fun toString(value: String, displayAsHtml: Boolean?, msgIfBlank: String): String {
        if (isBlank(value)) {
            return msgIfBlank
        }

        val userService = AppContextUtil.getSpringBean(UserService::class.java)
        val user = userService.findUserByUserNameInAccount(value, AppUI.accountId)
        if (user != null) {
            return if (displayAsHtml!!) {
                val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)

                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(
                        MailUtils.getSiteUrl(AppUI.accountId), user.username)

                val link = FormatUtils.newA(userLink, user.displayName)
                FormatUtils.newLink(img, link).write()
            } else {
                user.displayName
            }
        }

        return value
    }
}
