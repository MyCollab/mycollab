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

    override fun toString(value: String): String =
            toString(value, true, UserUIContext.getMessage(GenericI18Enum.FORM_EMPTY))

    override fun toString(value: String, displayAsHtml: Boolean, msgIfBlank: String): String {
        if (isBlank(value)) {
            return msgIfBlank
        }

        val userService = AppContextUtil.getSpringBean(UserService::class.java)
        val user = userService.findUserByUserNameInAccount(value, AppUI.accountId)
        if (user != null) {
            return if (displayAsHtml) {
                val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)

                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(
                        MailUtils.getSiteUrl(AppUI.accountId), user.username)

                val link = FormatUtils.newA(userLink, user.displayName!!)
                FormatUtils.newLink(img, link).write()
            } else user.displayName!!
        }

        return value
    }
}
