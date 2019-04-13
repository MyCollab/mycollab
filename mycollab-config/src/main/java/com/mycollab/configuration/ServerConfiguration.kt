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

import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.FileUtils
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

/**
 * @author MyCollab Ltd
 * @since 5.5.0
 */
@Component
@ConfigurationProperties(prefix = "server")
class ServerConfiguration(var storageSystem: String = STORAGE_FILE, var port: Int = 8080,
                          var address: String = "",
                          var apiUrl: String = "", var pullMethod: String = "", var siteUrl: String = "",
                          var resourceDownloadUrl: String = "", var cdnUrl: String = "",
                          var dataDir: String = USER_DIR) {

    private var _cacheHomeDir:File? = null

    fun getApiUrl(path: String) = "$apiUrl$path"

    fun getHomeDir(): File {
        if (_cacheHomeDir == null) {
            val userFolder = when (dataDir) {
                USER_DIR -> System.getProperty("user.home")
                APP_DIR -> System.getProperty("user.dir")
                else -> throw MyCollabException("Not support data dir value $dataDir")
            }
            _cacheHomeDir = File("$userFolder/.mycollab")
            val userHomeDir = File(System.getProperty("user.dir") + "/.mycollab")
            if (userHomeDir.exists() && _cacheHomeDir!!.toPath() != userHomeDir.toPath()) {
                try {
                    Files.move(userHomeDir.toPath(), _cacheHomeDir!!.toPath(), StandardCopyOption.REPLACE_EXISTING)
                } catch (e: Exception) {
                    userHomeDir
                }

            } else {
                FileUtils.mkdirs(_cacheHomeDir!!)
            }
        }
        return _cacheHomeDir!!
    }

    val isPush: Boolean
        get() = !"pull".equals(pullMethod, ignoreCase = true)

    companion object {

        const val STORAGE_FILE = "file"

        const val STORAGE_S3 = "s3"

        const val USER_DIR = "userdir"

        const val APP_DIR = "appdir"
    }
}
