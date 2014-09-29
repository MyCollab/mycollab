/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.ecm.service.impl;

import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxEntry.File;
import com.dropbox.core.DbxEntry.WithChildren;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.module.ecm.StorageNames;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.domain.ExternalContent;
import com.esofthead.mycollab.module.ecm.domain.ExternalDrive;
import com.esofthead.mycollab.module.ecm.domain.ExternalFolder;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.domain.Resource;
import com.esofthead.mycollab.module.ecm.service.DropboxResourceService;

@Service
public class DropboxResourceServiceImpl implements DropboxResourceService {
	private static Logger log = LoggerFactory
			.getLogger(DropboxResourceServiceImpl.class);

	@Override
	public List<Resource> getResources(ExternalDrive drive, String path) {
		List<Resource> resources = new ArrayList<Resource>();
		try {
			DbxRequestConfig requestConfig = new DbxRequestConfig(
					"MyCollab/1.0", null);
			DbxClient client = new DbxClient(requestConfig,
					drive.getAccesstoken());
			WithChildren children = client.getMetadataWithChildren(path);
			if (CollectionUtils.isNotEmpty(children.children)) {
				for (DbxEntry entry : children.children) {
					if (entry.isFile()) {
						ExternalContent resource = new ExternalContent(
								entry.path);
						resource.setStorageName(StorageNames.DROPBOX);
						resource.setExternalDrive(drive);
						Date lastModifiedDate = ((File) entry).lastModified;
						Calendar createdDate = new GregorianCalendar();
						createdDate.setTime(lastModifiedDate);
						resource.setSize(((File) entry).numBytes);
						resource.setCreated(createdDate);
						resources.add(resource);
					} else if (entry.isFolder()) {
						ExternalFolder resource = new ExternalFolder(entry.path);
						resource.setStorageName(StorageNames.DROPBOX);
						resource.setExternalDrive(drive);
						resources.add(resource);
					} else {
						log.error("Do not support dropbox resource except file or folder");
					}
				}
			}
		} catch (Exception e) {
			log.error("Error when get dropbox resource", e);
			throw new UserInvalidInputException(
					"Error when retrieving dropbox files. The most possible issue is can not connect to dropbox server");
		}
		Collections.sort(resources);
		return resources;
	}

	@Override
	public List<ExternalFolder> getSubFolders(ExternalDrive drive, String path) {
		List<ExternalFolder> subFolders = new ArrayList<ExternalFolder>();

		try {
			DbxRequestConfig requestConfig = new DbxRequestConfig(
					"MyCollab/1.0", null);
			DbxClient client = new DbxClient(requestConfig,
					drive.getAccesstoken());
			WithChildren children = client.getMetadataWithChildren(path);
			if (CollectionUtils.isNotEmpty(children.children)) {
				for (DbxEntry entry : children.children) {
					if (entry.isFolder()) {
						ExternalFolder resource = new ExternalFolder(entry.path);
						resource.setStorageName(StorageNames.DROPBOX);
						resource.setExternalDrive(drive);
						subFolders.add(resource);
					}

				}
			}
		} catch (Exception e) {
			log.error("Error when get dropbox resource", e);
		}

		return subFolders;
	}

	@Override
	public Resource getCurrentResourceByPath(ExternalDrive drive, String path) {
		try {
			DbxRequestConfig requestConfig = new DbxRequestConfig(
					"MyCollab/1.0", null);
			DbxClient client = new DbxClient(requestConfig,
					drive.getAccesstoken());
			Resource res = null;

			DbxEntry entry = client.getMetadata(path);
			if (entry == null) {
				return null;
			}

			if (entry.isFile()) {
				ExternalContent resource = new ExternalContent(entry.path);
				resource.setStorageName(StorageNames.DROPBOX);
				resource.setExternalDrive(drive);
				Date lastModifiedDate = ((File) entry).lastModified;
				Calendar createdDate = new GregorianCalendar();
				createdDate.setTime(lastModifiedDate);
				resource.setSize(((File) entry).numBytes);
				resource.setCreated(createdDate);
				res = resource;
			} else if (entry.isFolder()) {
				ExternalFolder resource = new ExternalFolder(entry.path);
				resource.setStorageName(StorageNames.DROPBOX);
				resource.setExternalDrive(drive);
				res = resource;
			}
			return res;
		} catch (DbxException e) {
			log.error("Error when get dropbox resource", e);
			throw new UserInvalidInputException(e);
		}

	}

	@Override
	public Folder getParentResourceFolder(ExternalDrive drive, String childPath) {
		String folderPath = childPath.substring(0, childPath.lastIndexOf("/"));
		if (folderPath.length() == 0)
			folderPath = "/";
		return (Folder) this.getCurrentResourceByPath(drive, folderPath);
	}

	@Override
	public Folder createFolder(ExternalDrive drive, String path) {
		DbxRequestConfig requestConfig = new DbxRequestConfig("MyCollab/1.0",
				null);
		DbxClient client = new DbxClient(requestConfig, drive.getAccesstoken());
		try {
			client.createFolder(path);
			return (Folder) this.getCurrentResourceByPath(drive, path);
		} catch (DbxException e) {
			log.error("Error when createdFolder dropbox resource", e);
		}
		return null;
	}

	@Override
	public void saveContent(ExternalDrive drive, Content content, InputStream in) {
		DbxRequestConfig requestConfig = new DbxRequestConfig("MyCollab/1.0",
				null);
		DbxClient client = new DbxClient(requestConfig, drive.getAccesstoken());
		try {
			client.uploadFile(content.getPath(), DbxWriteMode.add(), -1, in);
		} catch (Exception e) {
			log.error("Error when upload file to Dropbox", e);
		}
	}

	@Override
	public void rename(ExternalDrive drive, String oldPath, String newPath) {
		DbxRequestConfig requestConfig = new DbxRequestConfig("MyCollab/1.0",
				null);
		DbxClient client = new DbxClient(requestConfig, drive.getAccesstoken());
		try {
			client.copy(oldPath, newPath);
			client.delete(oldPath);
		} catch (DbxException e) {
			log.error("Error when rename dropbox resource", e);
		}
	}

	@Override
	public void deleteResource(ExternalDrive drive, String path) {
		DbxRequestConfig requestConfig = new DbxRequestConfig("MyCollab/1.0",
				null);
		DbxClient client = new DbxClient(requestConfig, drive.getAccesstoken());
		try {
			client.delete(path);
		} catch (DbxException e) {
			log.error("Error when Delete dropbox resource", e);
		}
	}

	@Override
	public InputStream download(ExternalDrive drive, final String path) {
		DbxRequestConfig requestConfig = new DbxRequestConfig("MyCollab/1.0",
				null);
		final DbxClient client = new DbxClient(requestConfig,
				drive.getAccesstoken());
		PipedInputStream in = new PipedInputStream();
		try {
			final PipedOutputStream out = new PipedOutputStream(in);
			new Thread(new Runnable() {
				public void run() {
					try {
						client.getFile(path, null, out);
						out.close();
					} catch (Exception e) {
						log.error("Error when get File from dropbox", e);
					}
				}
			}).start();
		} catch (Exception e) {
			throw new MyCollabException(
					"Error when get inputStream from dropbox file", e);
		}
		return in;
	}

	/**
	 * @see only support move in Dropbox local, not implement move from Dropbox
	 *      to MyCollab or against. Must implement it later
	 */
	@Override
	public void move(ExternalDrive drive, String fromPath, String toPath) {
		DbxRequestConfig requestConfig = new DbxRequestConfig("MyCollab/1.0",
				null);
		DbxClient client = new DbxClient(requestConfig, drive.getAccesstoken());
		try {
			client.move(fromPath, toPath);
		} catch (DbxException e) {
			log.error("Error when move dropbox resource", e);
		}
	}
}
