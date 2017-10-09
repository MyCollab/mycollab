package com.mycollab.module.user

import com.mycollab.vaadin.AppUI

/**
 * @author MyCollab Ltd.
 * @since 4.1.2
 */
object AccountLinkBuilder {
    @JvmStatic
    fun generatePreviewFullUserLink(username: String): String =
            AccountLinkGenerator.generatePreviewFullUserLink(AppUI.siteUrl, username)

    @JvmStatic
    fun generatePreviewFullRoleLink(userRoleId: Int?): String =
            AccountLinkGenerator.generatePreviewFullRoleLink(AppUI.siteUrl, userRoleId)
}
