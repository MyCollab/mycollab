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

import com.mycollab.core.utils.FileUtils
import com.mycollab.vaadin.resources.VaadinResource
import com.vaadin.server.DownloadStream
import com.vaadin.server.FileResource
import com.vaadin.server.Resource
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class VaadinFileResource : VaadinResource {
    companion object {
        val LOG = LoggerFactory.getLogger(VaadinFileResource::class.java)
    }


    override fun getStreamResource(documentPath: String): Resource = FileStreamDownloadResource(documentPath)

    class FileStreamDownloadResource(documentPath: String) : FileResource(File(FileUtils.homeFolder, documentPath)) {
        override fun getStream(): DownloadStream? {
            val fileName = filename.replace(" ", "_").replace("-", "_")
            try {
                val inStream = FileInputStream(sourceFile)
                val ds = DownloadStream(inStream, mimeType, fileName)
                ds.setParameter("Content-Disposition", "attachment; filename=" + fileName)
                ds.cacheTime = 0
                return ds
            } catch (e: IOException) {
                LOG.error("Error to create download stream", e)
                return null
            }
        }
    }
}