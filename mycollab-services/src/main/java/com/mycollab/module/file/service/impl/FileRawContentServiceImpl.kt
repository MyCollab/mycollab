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
package com.mycollab.module.file.service.impl

import com.mycollab.core.MyCollabException
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.module.file.service.RawContentService
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory

import java.io.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class FileRawContentServiceImpl : RawContentService {

    private val baseFolder: File = com.mycollab.core.utils.FileUtils.homeFolder

    override fun saveContent(objectPath: String, stream: InputStream) {
        val startFileNameIndex = objectPath.lastIndexOf("/")
        if (startFileNameIndex > 0) {
            /*
             * make sure the directory exist
			 */
            val folderPath = objectPath.substring(0, startFileNameIndex)
            val file = File(baseFolder, folderPath)
            if (!file.exists() && !file.mkdirs()) {
                throw MyCollabException("Create directory failed")
            }
        }

        try {
            BufferedOutputStream(FileOutputStream(File(baseFolder, objectPath))).use { outStream ->
                val buffer = ByteArray(BUFFER_SIZE)
                var byteRead = stream.read(buffer)

                while (byteRead > 0) {
                    outStream.write(buffer, 0, byteRead)
                    byteRead = stream.read(buffer)
                }
            }
        } catch (e: IOException) {
            throw MyCollabException(e)
        }

    }

    override fun getContentStream(objectPath: String): InputStream {
        try {
            val file = File(baseFolder, objectPath)
            return FileInputStream(file)
        } catch (e: FileNotFoundException) {
            throw ResourceNotFoundException(e)
        }

    }

    override fun removePath(objectPath: String) {
        try {
            val file = File(baseFolder, objectPath)
            if (file.exists()) {
                if (file.isDirectory) {
                    FileUtils.deleteDirectory(file)
                } else {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            throw MyCollabException(e)
        }

    }

    override fun renamePath(oldPath: String, newPath: String) {
        val file = File(baseFolder, oldPath)
        if (file.exists()) {
            val result = file.renameTo(File("$baseFolder/$newPath"))
            if (!result) {
                LOG.error("Can not rename old path $oldPath to new path $newPath")
            }
        } else {
            LOG.error("Can not rename old path $oldPath to new path $newPath because file is not existed")
        }
    }

    override fun movePath(oldPath: String, destinationPath: String) {
        try {
            val src = File("$baseFolder/$oldPath")
            val dest = File("$baseFolder/$destinationPath")

            if (!src.exists()) {
                LOG.debug("Source: ${src.path} is not existed")
                return
            }

            if (dest.exists()) {
                FileUtils.deleteQuietly(dest)
            }

            if (src.isFile) {
                FileUtils.moveFile(src, dest)
            } else {
                FileUtils.moveDirectory(src, dest)
            }
        } catch (e: IOException) {
            throw MyCollabException(e)
        }

    }

    override fun getSize(objectPath: String): Long {
        val file = File("$baseFolder/$objectPath")
        return if (file.exists()) {
            when {
                file.isFile -> FileUtils.sizeOf(file)
                file.isDirectory -> FileUtils.sizeOfDirectory(file)
                else -> 0
            }
        } else 0

    }

    companion object {
        private val BUFFER_SIZE = 1024

        private val LOG = LoggerFactory.getLogger(FileRawContentServiceImpl::class.java)
    }
}
