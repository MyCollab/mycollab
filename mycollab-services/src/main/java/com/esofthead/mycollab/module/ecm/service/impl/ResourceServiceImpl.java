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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.utils.MimeTypesUtil;
import com.esofthead.mycollab.esb.CamelProxyBuilderUtil;
import com.esofthead.mycollab.module.billing.service.BillingPlanCheckerService;
import com.esofthead.mycollab.module.ecm.dao.ContentJcrDao;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.domain.ContentActivityLogAction;
import com.esofthead.mycollab.module.ecm.domain.ContentActivityLogBuilder;
import com.esofthead.mycollab.module.ecm.domain.ContentActivityLogWithBLOBs;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.domain.Resource;
import com.esofthead.mycollab.module.ecm.esb.DeleteResourcesCommand;
import com.esofthead.mycollab.module.ecm.esb.EcmEndPoints;
import com.esofthead.mycollab.module.ecm.esb.SaveContentCommand;
import com.esofthead.mycollab.module.ecm.service.ContentActivityLogService;
import com.esofthead.mycollab.module.ecm.service.ResourceService;
import com.esofthead.mycollab.module.file.service.RawContentService;

@Service
public class ResourceServiceImpl implements ResourceService {
	private static Logger log = LoggerFactory
			.getLogger(ResourceServiceImpl.class);

	@Autowired
	private ContentJcrDao contentJcrDao;

	@Autowired
	private RawContentService rawContentService;

	@Autowired
	private ContentActivityLogService contentActivityLogService;

	@Autowired
	private BillingPlanCheckerService billingPlanCheckerService;

	@Override
	public List<Resource> getResources(String path) {
		List<Resource> resources = contentJcrDao.getResources(path);
		if (CollectionUtils.isNotEmpty(resources)) {
			Collections.sort(resources);
		}

		return resources;
	}

	@Override
	public List<Content> getContents(String path) {
		return contentJcrDao.getContents(path);
	}

	@Override
	public List<Folder> getSubFolders(String path) {
		return contentJcrDao.getSubFolders(path);
	}

	@Override
	public Folder createNewFolder(String baseFolderPath, String folderName,
			String createdBy) {
		String folderPath = baseFolderPath + "/" + folderName;
		Folder folder = new Folder(folderPath);
		folder.setName(folderName);
		folder.setCreatedBy(createdBy);
		folder.setCreated(new GregorianCalendar());
		contentJcrDao.createFolder(folder, createdBy);
		ContentActivityLogWithBLOBs activityLog = new ContentActivityLogWithBLOBs();
		ContentActivityLogAction createFolderAction = ContentActivityLogBuilder
				.makeCreateFolder(folderPath);
		activityLog.setCreateduser(createdBy);
		activityLog.setBasefolderpath(baseFolderPath);
		activityLog.setActiondesc(createFolderAction.toString());
		contentActivityLogService.saveWithSession(activityLog, "");
		return folder;
	}

	@Override
	public void saveContent(Content content, String createdUser,
			InputStream refStream, Integer sAccountId) {
		Integer fileSize = 0;
		try {
			fileSize = refStream.available();
			billingPlanCheckerService.validateAccountCanUploadMoreFiles(
					sAccountId, fileSize);
		} catch (IOException e) {
			log.error("Can not get available bytes", e);
		}

		// detect mimeType and set to content
		String mimeType = MimeTypesUtil.detectMimeType(content.getPath());
		content.setMimeType(mimeType);
		content.setSize(Long.valueOf(fileSize));

		contentJcrDao.saveContent(content, createdUser);

		String contentPath = content.getPath();
		rawContentService.saveContent(contentPath, refStream);

		ContentActivityLogWithBLOBs activityLog = new ContentActivityLogWithBLOBs();
		ContentActivityLogAction createContentAction = ContentActivityLogBuilder
				.makeCreateContent(contentPath);
		activityLog.setCreateduser(createdUser);
		activityLog.setActiondesc(createContentAction.toString());
		activityLog.setBasefolderpath(contentPath);
		contentActivityLogService.saveWithSession(activityLog, "");

		SaveContentCommand saveContentCommand = CamelProxyBuilderUtil.build(
				EcmEndPoints.SAVE_CONTENT_ENDPOINT, SaveContentCommand.class);
		saveContentCommand.saveContent(content, createdUser, sAccountId);
	}

	@Override
	public void removeResource(String path, String deleteUser,
			Integer sAccountId) {
		Resource res = contentJcrDao.getResource(path);
		ContentActivityLogAction deleteResourceAction;

		if (res instanceof Folder) {
			deleteResourceAction = ContentActivityLogBuilder
					.makeDeleteFolder(path);
		} else {
			deleteResourceAction = ContentActivityLogBuilder
					.makeDeleteContent(path);
		}

		contentJcrDao.removeResource(path);

		ContentActivityLogWithBLOBs activityLog = new ContentActivityLogWithBLOBs();
		activityLog.setCreateduser(deleteUser);
		activityLog.setActiondesc(deleteResourceAction.toString());
		activityLog.setBasefolderpath(path);
		contentActivityLogService.saveWithSession(activityLog, "");

		DeleteResourcesCommand deleteResourcesCommand = CamelProxyBuilderUtil
				.build(EcmEndPoints.DELETE_RESOURCES_ENDPOINT,
						DeleteResourcesCommand.class);
		deleteResourcesCommand.removeResource(path, deleteUser, sAccountId);
	}

	@Override
	public InputStream getContentStream(String path) {
		return rawContentService.getContentStream(path);
	}

	@Override
	public void rename(String oldPath, String newPath, String userUpdate) {
		Resource res = contentJcrDao.getResource(oldPath);
		ContentActivityLogAction renameAction;
		if (res instanceof Folder) {
			renameAction = ContentActivityLogBuilder.makeRenameFolder(oldPath,
					newPath);
		} else {
			renameAction = ContentActivityLogBuilder.makeRenameContent(oldPath,
					newPath);
		}

		contentJcrDao.rename(oldPath, newPath);
		rawContentService.renamePath(oldPath, newPath);

		ContentActivityLogWithBLOBs activityLog = new ContentActivityLogWithBLOBs();
		activityLog.setCreateduser(userUpdate);
		activityLog.setActiondesc(renameAction.toString());
		activityLog.setBasefolderpath(newPath);
		contentActivityLogService.saveWithSession(activityLog, "");
	}

	@Override
	public List<Resource> searchResourcesByName(String baseFolderPath,
			String resourceName) {
		return contentJcrDao
				.searchResourcesByName(baseFolderPath, resourceName);
	}

	@Override
	public void moveResource(String oldPath, String destinationFolderPath,
			String userMove) {
		String oldResourceName = oldPath.substring(
				oldPath.lastIndexOf("/") + 1, oldPath.length());

		Resource oldResource = contentJcrDao.getResource(oldPath);
		ContentActivityLogAction moveResoureAction;
		if (oldResource instanceof Folder) {
			moveResoureAction = ContentActivityLogBuilder.makeMoveFolder(
					oldPath, destinationFolderPath);
		} else {
			moveResoureAction = ContentActivityLogBuilder.makeMoveContent(
					oldPath, destinationFolderPath);
		}

		if ((oldResource instanceof Folder)
				&& destinationFolderPath.contains(oldPath)) {
			throw new UserInvalidInputException(
					"Can not move asset(s) to folder " + destinationFolderPath);
		} else {
			String destinationPath = destinationFolderPath + "/"
					+ oldResourceName;
			contentJcrDao.moveResource(oldPath, destinationPath);
			rawContentService.movePath(oldPath, destinationPath);

			ContentActivityLogWithBLOBs activityLog = new ContentActivityLogWithBLOBs();
			activityLog.setCreateduser(userMove);
			activityLog.setActiondesc(moveResoureAction.toString());
			activityLog.setBasefolderpath(destinationPath);
			contentActivityLogService.saveWithSession(activityLog, "");
		}
	}

	@Override
	public Folder getParentFolder(String path) {
		try {
			String parentPath = path.substring(0, path.lastIndexOf("/"));
			Resource res = contentJcrDao.getResource(parentPath);
			if (res instanceof Folder)
				return (Folder) res;
			return null;
		} catch (Exception e) {
			throw new MyCollabException(e);
		}
	}

	@Override
	public Resource getResource(String path) {
		return contentJcrDao.getResource(path);
	}
}
