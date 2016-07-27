/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.resources.file

import java.io.{File, FileInputStream, IOException}

import com.mycollab.configuration.FileStorage
import com.mycollab.vaadin.resources.VaadinResource
import com.vaadin.server.{DownloadStream, FileResource, Resource}
import org.slf4j.LoggerFactory

/**
  * @author MyCollab Ltd
  * @since 5.3.5
  */
class VaadinFileResource extends VaadinResource {
  val LOG = LoggerFactory.getLogger(classOf[VaadinFileResource])
  
  override def getStreamResource(documentPath: String): Resource = new FileStreamDownloadResource(documentPath)
  
  @SerialVersionUID(1L)
  class FileStreamDownloadResource(val documentPath: String) extends FileResource(new File(FileStorage.getInstance.getBaseContentFolder, documentPath)) {
    override def getStream: DownloadStream = {
      val fileName = getFilename.replace(" ", "_").replace("-", "_")
      try {
        val inStream = new FileInputStream(getSourceFile)
        val ds = new DownloadStream(inStream, getMIMEType, fileName)
        ds.setParameter("Content-Disposition", "attachment; filename=" + fileName)
        ds.setCacheTime(0)
        ds
      }
      catch {
        case e: IOException =>
          LOG.error("Error to create download stream", e)
          null
      }
    }
  }
  
}
