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
            "$siteUrl${URL_PREFIX_PARAM}account/preview"

    @JvmStatic
    fun generateRoleLink(userRoleId: Int?): String =
            "${URL_PREFIX_PARAM}account/role/preview/${UrlEncodeDecoder.encode(userRoleId)}"

    @JvmStatic
    fun generatePreviewFullRoleLink(siteUrl: String, userRoleId: Int?): String =
            "$siteUrl${generateRoleLink(userRoleId)}"

    @JvmStatic
    fun generateUserLink(username: String?): String =
            "${URL_PREFIX_PARAM}account/user/preview/${GenericLinkUtils.encodeParam(username)}"

    @JvmStatic
    fun generatePreviewFullUserLink(siteUrl: String, username: String?): String =
            "${siteUrl}account/user/preview/${GenericLinkUtils.encodeParam(username)}"
}
