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
package com.esofthead.mycollab.vaadin.resources.file;

import com.esofthead.mycollab.configuration.FileStorage;
import com.esofthead.mycollab.vaadin.resources.VaadinResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author MyCollab Ltd.
 * @since 4.5.1
 */
public class VaadinFileResource extends VaadinResource {

    private static final Logger LOG = LoggerFactory.getLogger(VaadinFileResource.class);

    @Override
    public Resource getStreamResource(String documentPath) {
        return new FileStreamDownloadResource(documentPath);
    }

    static class FileStreamDownloadResource extends FileResource {
        private static final long serialVersionUID = 1L;

        FileStreamDownloadResource(String documentPath) {
            super(new File(FileStorage.baseContentFolder, documentPath));
        }

        @Override
        public DownloadStream getStream() {
            final String fileName = getFilename().replaceAll(" ", "_").replaceAll("-", "_");
            try {
                FileInputStream inStream = new FileInputStream(getSourceFile());
                DownloadStream ds = new DownloadStream(inStream, getMIMEType(), fileName);
                ds.setParameter("Content-Disposition", "attachment; filename=" + fileName);
                ds.setCacheTime(0);
                return ds;
            } catch (IOException e) {
                LOG.error("Error to create download stream", e);
                return null;
            }
        }

    }
}
