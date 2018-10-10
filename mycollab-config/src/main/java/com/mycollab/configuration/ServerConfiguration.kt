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
package com.mycollab.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 5.5.0
 */
@Component
@ConfigurationProperties(prefix = "server")
class ServerConfiguration(var storageSystem: String = STORAGE_FILE, var port: Int,
                          var address: String?,
                          var apiUrl: String, var pullMethod: String?, var siteUrl: String,
                          var resourceDownloadUrl: String, var cdnUrl: String) {

    constructor() : this("", 8080, "", "", "", "", "", "")

    fun getApiUrl(path: String) = "$apiUrl$path"

    val isPush: Boolean
        get() = !"pull".equals(pullMethod ?: "", ignoreCase = true)

    companion object {

        const val STORAGE_FILE = "file"

        const val STORAGE_S3 = "s3"
    }
}
