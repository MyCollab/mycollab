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
package com.mycollab.core.utils

import org.apache.tika.Tika

import java.io.IOException
import java.io.InputStream

/**
 * Utility class mainly used to detect mimetype of file upload to MyCollab
 *
 * @author MyCollab Ltd.
 * @since 2.0
 */
object MimeTypesUtil {
    @JvmField
    val BINARY_MIME_TYPE = "application/octet-stream"

    private val tika = Tika()

    /**
     * Detect mimetype of `inStream`
     *
     * @param inStream
     * @return mimetype of `inStream`. Return BINARY mimetype if it
     * can not detect
     */
    @JvmStatic
    fun detectMimeType(inStream: InputStream): String = try {
        tika.detect(inStream)
    } catch (e: IOException) {
        BINARY_MIME_TYPE
    }

    /**
     * Detect mimetype of content `contentName`
     *
     * @param contentName
     * @return
     */
    @JvmStatic
    fun detectMimeType(contentName: String): String = tika.detect(contentName.trim { it <= ' ' })

    @JvmStatic
    fun isImageType(contentName: String): Boolean = tika.detect(contentName).startsWith("image/")

    @JvmStatic
    fun isImage(mimeType: String): Boolean = mimeType.startsWith("image/")

    @JvmStatic
    fun isText(mimeType: String): Boolean = mimeType.startsWith("text/")

    @JvmStatic
    fun isAudio(mimeType: String): Boolean = mimeType.startsWith("audio/")

    @JvmStatic
    fun isVideo(mimeType: String): Boolean = mimeType.startsWith("video/")
}
