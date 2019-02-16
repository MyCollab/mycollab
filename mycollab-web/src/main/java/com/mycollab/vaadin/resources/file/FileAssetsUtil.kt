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
package com.mycollab.vaadin.resources.file

import com.mycollab.core.utils.MimeTypesUtil
import com.vaadin.icons.VaadinIcons

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object FileAssetsUtil {
    private val extraIconMap = mapOf("application/pdf" to VaadinIcons.FILE_TEXT,
            "application/vnd.ms-excel" to VaadinIcons.FILE_CODE,
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" to VaadinIcons.FILE_CODE,
            "application/msword" to VaadinIcons.FILE_CODE,
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" to VaadinIcons.FILE_CODE,
            "application/vnd.ms-powerpoint" to VaadinIcons.FILE_PRESENTATION,
            "application/vnd.openxmlformats-officedocument.presentationml.presentation" to VaadinIcons.FILE_PRESENTATION,
            "application/zip" to VaadinIcons.FILE_ZIP,
            "application/x-rar-compressed" to VaadinIcons.FILE_ZIP,
            "application/x-7z-compressed" to VaadinIcons.FILE_ZIP,
            "application/java-archive" to VaadinIcons.FILE_ZIP,
            "application/x-tar" to VaadinIcons.FILE_ZIP)

    @JvmStatic fun getFileIconResource(fileName: String): VaadinIcons {
        val mimeType: String = MimeTypesUtil.detectMimeType(fileName)
        return when {
            MimeTypesUtil.isImage(mimeType) -> VaadinIcons.FILE_PICTURE
            MimeTypesUtil.isAudio(mimeType) -> VaadinIcons.FILE
            MimeTypesUtil.isVideo(mimeType) -> VaadinIcons.FILE_MOVIE
            MimeTypesUtil.isText(mimeType) -> VaadinIcons.FILE_TEXT_O
            else -> extraIconMap[mimeType] ?: VaadinIcons.FILE_O
        }
    }
}