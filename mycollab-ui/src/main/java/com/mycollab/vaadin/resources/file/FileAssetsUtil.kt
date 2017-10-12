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
import com.vaadin.server.FontAwesome

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object FileAssetsUtil {
    private val extraIconMap = mapOf("application/pdf" to FontAwesome.FILE_PDF_O,
            "application/vnd.ms-excel" to FontAwesome.FILE_EXCEL_O,
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" to FontAwesome.FILE_EXCEL_O,
            "application/msword" to FontAwesome.FILE_WORD_O,
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" to FontAwesome.FILE_WORD_O,
            "application/vnd.ms-powerpoint" to FontAwesome.FILE_POWERPOINT_O,
            "application/vnd.openxmlformats-officedocument.presentationml.presentation" to FontAwesome.FILE_POWERPOINT_O,
            "application/zip" to FontAwesome.FILE_ZIP_O,
            "application/x-rar-compressed" to FontAwesome.FILE_ARCHIVE_O,
            "application/x-7z-compressed" to FontAwesome.FILE_ARCHIVE_O,
            "application/java-archive" to FontAwesome.FILE_ARCHIVE_O,
            "application/x-tar" to FontAwesome.FILE_ARCHIVE_O)

    @JvmStatic fun getFileIconResource(fileName: String): FontAwesome {
        val mimeType: String = MimeTypesUtil.detectMimeType(fileName)
        return when {
            MimeTypesUtil.isImage(mimeType) -> FontAwesome.FILE_IMAGE_O
            MimeTypesUtil.isAudio(mimeType) -> FontAwesome.FILE_AUDIO_O
            MimeTypesUtil.isVideo(mimeType) -> FontAwesome.FILE_VIDEO_O
            MimeTypesUtil.isText(mimeType) -> FontAwesome.FILE_TEXT_O
            else -> extraIconMap[mimeType] ?: FontAwesome.FILE_O
        }
    }
}