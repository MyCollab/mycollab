package com.mycollab.module.user

import com.mycollab.common.GenericLinkUtils
import com.mycollab.common.GenericLinkUtils.URL_PREFIX_PARAM
import com.mycollab.common.UrlEncodeDecoder

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object AccountLinkGenerator {

    @JvmStatic
    fun generateFullProfileLink(siteUrl: String): String =
            siteUrl + URL_PREFIX_PARAM + "account/preview"

    @JvmStatic
    fun generateRoleLink(userRoleId: Int?): String =
            "account/role/preview/" + UrlEncodeDecoder.encode(userRoleId)

    @JvmStatic
    fun generatePreviewFullRoleLink(siteUrl: String, userRoleId: Int?): String =
            siteUrl + URL_PREFIX_PARAM + generateRoleLink(userRoleId)

    @JvmStatic
    fun generateUserLink(username: String?): String =
            URL_PREFIX_PARAM + "account/user/preview/" + GenericLinkUtils.encodeParam(username)

    @JvmStatic
    fun generatePreviewFullUserLink(siteUrl: String, username: String?): String =
            siteUrl + URL_PREFIX_PARAM + "account/user/preview/" + GenericLinkUtils.encodeParam(username)
}
