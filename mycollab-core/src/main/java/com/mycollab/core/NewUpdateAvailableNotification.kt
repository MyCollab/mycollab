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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.core

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
class NewUpdateAvailableNotification(val version: String, val autoDownloadLink: String?, val manualDownloadLink: String, val installerFile: String?) : AbstractNotification(AbstractNotification.NEWS) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NewUpdateAvailableNotification) return false

        val that = other as NewUpdateAvailableNotification?

        if (version != that!!.version) return false
        if (if (autoDownloadLink != null) autoDownloadLink != that.autoDownloadLink else that.autoDownloadLink != null)
            return false
        if (manualDownloadLink != that.manualDownloadLink) return false
        return if (installerFile != null) installerFile == that.installerFile else that.installerFile == null

    }

    override fun hashCode(): Int {
        var result = version.hashCode()
        result = 31 * result + (autoDownloadLink?.hashCode() ?: 0)
        result = 31 * result + manualDownloadLink.hashCode()
        result = 31 * result + (installerFile?.hashCode() ?: 0)
        return result
    }
}
