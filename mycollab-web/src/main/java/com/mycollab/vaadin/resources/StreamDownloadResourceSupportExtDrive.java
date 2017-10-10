/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.resources;

import com.mycollab.core.MyCollabThread;
import com.mycollab.module.ecm.ResourceType;
import com.mycollab.module.ecm.ResourceUtils;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.ecm.domain.Resource;
import com.mycollab.module.ecm.service.ExternalResourceService;
import com.mycollab.module.ecm.service.ResourceService;
import com.mycollab.spring.AppContextUtil;
import com.vaadin.server.StreamResource;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class StreamDownloadResourceSupportExtDrive implements StreamResource.StreamSource {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(StreamDownloadResourceSupportExtDrive.class);

    private Collection<Resource> resources;

    private ResourceService resourceService;

    public StreamDownloadResourceSupportExtDrive(Collection<Resource> resources) {
        this.resources = resources;
        resourceService = AppContextUtil.getSpringBean(ResourceService.class);
    }

    @Override
    public InputStream getStream() {
        if (resources.size() == 1) {
            Resource res = resources.iterator().next();
            if (!(res instanceof Folder)) {
                if (res.isExternalResource()) {
                    ExternalResourceService service = ResourceUtils.getExternalResourceService(ResourceUtils.getType(res));
                    return service.download(ResourceUtils.getExternalDrive(res), res.getPath());
                } else {
                    return resourceService.getContentStream(res.getPath());
                }
            }
        }

        final PipedInputStream inStream = new PipedInputStream();
        final PipedOutputStream outStream;

        try {
            outStream = new PipedOutputStream(inStream);
        } catch (IOException ex) {
            LOG.error("Can not create outstream file", ex);
            return null;
        }

        Thread threadExport = new MyCollabThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ZipOutputStream zipOutStream = new ZipOutputStream(outStream);
                    zipResource(zipOutStream, resources);
                    zipOutStream.close();
                    outStream.close();
                } catch (Exception e) {
                    LOG.error("Error while saving content stream", e);
                }
            }
        });

        threadExport.start();
        return inStream;
    }

    private void zipResource(ZipOutputStream zipOutputStream, Collection<Resource> lstRes) {
        try {
            List<Resource> recurrResources;
            for (Resource currentResource : lstRes) {
                if (currentResource instanceof Folder) {
                    if (!currentResource.isExternalResource()) {
                        recurrResources = resourceService.getResources(currentResource.getPath());
                    } else {
                        ExternalResourceService service = ResourceUtils
                                .getExternalResourceService(ResourceUtils.getType(currentResource));
                        recurrResources = service.getResources(ResourceUtils
                                        .getExternalDrive(currentResource), currentResource.getPath());
                    }

                    if (CollectionUtils.isEmpty(recurrResources)) {
                        zipOutputStream.putNextEntry(new ZipEntry(currentResource.getName() + "/"));
                    } else {
                        for (Resource res : recurrResources) {
                            if (res instanceof Content) {
                                addFileToZip(currentResource.getName(), (Content) res, zipOutputStream);
                            } else if (res instanceof Folder) {
                                addFolderToZip(currentResource.getName(), res, zipOutputStream);
                            }
                        }
                    }
                } else {
                    addFileToZip("", (Content) currentResource, zipOutputStream);
                }
            }
        } catch (Exception e) {
            LOG.error("Error while save content", e);
        }
    }

    private void addFileToZip(String path, Content res, ZipOutputStream zip) throws Exception {
        byte[] buf = new byte[1024];
        InputStream contentStream;
        if (!res.isExternalResource()) {
            contentStream = resourceService.getContentStream(res.getPath());
        } else {
            ExternalResourceService service = ResourceUtils.getExternalResourceService(ResourceUtils.getType(res));
            contentStream = service.download(ResourceUtils.getExternalDrive(res), res.getPath());
        }
        if (path.length() == 0)
            path = res.getName();
        else
            path += "/" + res.getName();
        zip.putNextEntry(new ZipEntry(path));
        int byteLength;
        while ((byteLength = contentStream.read(buf)) > 0) {
            zip.write(buf, 0, byteLength);
        }
    }

    private void addFolderToZip(String path, Resource res, ZipOutputStream zip)
            throws Exception {
        List<Resource> lstResource;
        if (ResourceUtils.getType(res) == ResourceType.MyCollab) {
            lstResource = resourceService.getResources(res.getPath());
        } else {
            ExternalResourceService service = ResourceUtils.getExternalResourceService(ResourceUtils.getType(res));
            lstResource = service.getResources(ResourceUtils.getExternalDrive(res), res.getPath());
        }
        if (res instanceof Folder && lstResource.size() == 0) { // emptyFolder
            zip.putNextEntry(new ZipEntry(path + "/" + res.getName() + "/"));
        } else {
            if (res instanceof Folder) {
                zip.putNextEntry(new ZipEntry(path + "/" + res.getName() + "/"));
            }
            for (Resource curRes : lstResource) {
                if (curRes instanceof Folder) {
                    addFolderToZip(path + "/" + res.getName(), curRes, zip);
                } else {
                    addFileToZip(path + "/" + res.getName(), (Content) curRes, zip);
                }
            }
        }
    }
}