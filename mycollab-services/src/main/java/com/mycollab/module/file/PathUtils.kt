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
package com.mycollab.module.file

/**
 * @author MyCollab Ltd
 * @since 5.0.10
 */
object PathUtils {
    @JvmStatic
    fun buildPath(sAccountId: Int?, objectPath: String): String {
        return (if (sAccountId == null) "" else sAccountId.toString() + "/") + objectPath
    }

    @JvmStatic
    fun getProjectLogoPath(accountId: Int?, projectId: Int?): String =
            "$accountId/project/$projectId/.attachments"

    @JvmStatic
    fun getEntityLogoPath(accountId: Int?): String = "$accountId/.assets"

    @JvmStatic
    fun getProjectDocumentPath(accountId: Int?, projectId: Int?): String =
            "$accountId/project/$projectId/.page"

    @JvmStatic
    fun buildLogoPath(sAccountId: Int?, logoFileName: String, logoSize: Int?): String =
            "$sAccountId/.assets/${logoFileName}_$logoSize.png"

    @JvmStatic
    fun buildFavIconPath(sAccountId: Int?, favIconFileName: String): String =
            "$sAccountId/.assets/$favIconFileName.ico"
}
