/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.MyCollabThread;
import com.esofthead.mycollab.module.ecm.ResourceType;
import com.esofthead.mycollab.module.ecm.ResourceUtils;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.domain.Resource;
import com.esofthead.mycollab.module.ecm.service.ExternalResourceService;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.vaadin.server.StreamResource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class StreamDownloadResourceSupportExtDrive implements
		StreamResource.StreamSource {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(StreamDownloadResourceSupportExtDrive.class);

	private List<Resource> lstResource;

	private ResourceService resourceService;

	private boolean isSearchAction;

	public StreamDownloadResourceSupportExtDrive(List<Resource> lstRes,
			boolean isSearchAction) {
		this.lstResource = lstRes;
		resourceService = ApplicationContextUtil
				.getSpringBean(ResourceService.class);
		this.isSearchAction = isSearchAction;
	}

	@Override
	public InputStream getStream() {
		if (lstResource != null && lstResource.size() == 1
				&& lstResource.get(0) instanceof Content) {
			Resource res = lstResource.get(0);
			if (ResourceUtils.getType(res) == ResourceType.MyCollab) {
				return resourceService.getContentStream(res.getPath());
			} else {
				ExternalResourceService service = ResourceUtils
						.getExternalResourceService(ResourceUtils.getType(res));
				return service.download(ResourceUtils.getExternalDrive(res),
						res.getPath());
			}
		}

		final PipedInputStream inStream = new PipedInputStream();
		final PipedOutputStream outStream;

		try {
			outStream = new PipedOutputStream(inStream);
		} catch (IOException ex) {
			log.error("Can not create outstream file", ex);
			return null;
		}

		Thread threadExport = new MyCollabThread(new Runnable() {

			@Override
			public void run() {
				try {
					ZipOutputStream zipOutStream = new ZipOutputStream(
							outStream);
					if (isSearchAction) {
						zipResourceWithSearchAction(zipOutStream, lstResource);
					} else {
						zipResource(zipOutStream, lstResource);
					}
					zipOutStream.close();
					outStream.close();
				} catch (Exception e) {
					log.error("Error while saving content stream", e);
				}
			}
		});

		threadExport.start();

		return inStream;
	}

	private void zipResourceWithSearchAction(ZipOutputStream zip,
			List<Resource> lstResource) {
		try {
			for (Resource res : lstResource) {
				// because only support search for MyCollab drive. must fix it
				// when support all drive
				Folder resParentFolder = resourceService.getParentFolder(res
						.getPath());
				String currentResourcePath = "";
				try {
					currentResourcePath = resParentFolder.getPath().substring(
							resParentFolder.getPath().indexOf("/", 2) + 1);
				} catch (Exception e) {
					currentResourcePath = "";
				}
				if (currentResourcePath.indexOf("/") != -1) {
					currentResourcePath = currentResourcePath
							.substring(currentResourcePath.indexOf("/") + 1);
				}
				currentResourcePath = currentResourcePath.replace("/", "");
				byte[] buf = new byte[1024];
				int len = -1;
				InputStream contentStream = resourceService
						.getContentStream(res.getPath());
				zip.putNextEntry(new ZipEntry(currentResourcePath
						+ res.getName()));
				while ((len = contentStream.read(buf)) > 0) {
					zip.write(buf, 0, len);
				}
			}
		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}

	private void zipResource(ZipOutputStream zipOutputStream,
			List<Resource> lstRes) {
		try {
			List<Resource> lstResource = new ArrayList<Resource>();
			for (Resource currentResource : lstRes) {
				if (currentResource instanceof Folder) {
					if (ResourceUtils.getType(currentResource) == ResourceType.MyCollab) {
						lstResource = resourceService
								.getResources(currentResource.getPath());
					} else {
						ExternalResourceService service = ResourceUtils
								.getExternalResourceService(ResourceUtils
										.getType(currentResource));
						lstResource = service
								.getResources(ResourceUtils
										.getExternalDrive(currentResource),
										currentResource.getPath());
					}
					if (lstResource.size() == 0) {
						zipOutputStream.putNextEntry(new ZipEntry(
								currentResource.getName() + "/"));
					} else {
						for (Resource res : lstResource) {
							if (res instanceof Content) {
								addFileToZip(currentResource.getName(),
										(Content) res, zipOutputStream);
							} else if (res instanceof Folder) {
								addFolderToZip(currentResource.getName(), res,
										zipOutputStream);
							}
						}
					}
				} else {
					addFileToZip("", (Content) currentResource, zipOutputStream);
				}
			}
		} catch (Exception e) {
			log.error("Error while save content", e);
		}
	}

	private void addFileToZip(String path, Content res, ZipOutputStream zip)
			throws Exception {
		byte[] buf = new byte[1024];
		int len = -1;
		InputStream contentStream = null;
		if (ResourceUtils.getType(res) == ResourceType.MyCollab) {
			contentStream = resourceService.getContentStream(res.getPath());
		} else {
			ExternalResourceService service = ResourceUtils
					.getExternalResourceService(ResourceUtils.getType(res));
			contentStream = service.download(
					ResourceUtils.getExternalDrive(res), res.getPath());
		}
		if (path.length() == 0)
			path = res.getName();
		else
			path += "/" + res.getName();
		zip.putNextEntry(new ZipEntry(path));
		while ((len = contentStream.read(buf)) > 0) {
			zip.write(buf, 0, len);
		}
	}

	private void addFolderToZip(String path, Resource res, ZipOutputStream zip)
			throws Exception {
		List<Resource> lstResource = null;
		if (ResourceUtils.getType(res) == ResourceType.MyCollab) {
			lstResource = resourceService.getResources(res.getPath());
		} else {
			ExternalResourceService service = ResourceUtils
					.getExternalResourceService(ResourceUtils.getType(res));
			lstResource = service.getResources(
					ResourceUtils.getExternalDrive(res), res.getPath());
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
					addFileToZip(path + "/" + res.getName(), (Content) curRes,
							zip);
				}
			}
		}
	}

}
