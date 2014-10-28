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
package com.esofthead.mycollab.module.ecm.service;

import java.io.InputStream;
import java.util.List;

import com.esofthead.mycollab.cache.IgnoreCacheClass;
import com.esofthead.mycollab.core.persistence.service.IService;
import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.domain.Resource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@IgnoreCacheClass
public interface ResourceService extends IService {

	Folder createNewFolder(String baseFolderPath, String folderName,
			String createdBy);

	List<Resource> getResources(String path);

	Resource getResource(String path);

	List<Content> getContents(String path);

	List<Folder> getSubFolders(String path);

	void saveContent(Content content, String createdUser,
			InputStream refStream, Integer sAccountId);

	void removeResource(String path, String userDelete, Integer sAccountId);

	InputStream getContentStream(String path);

	void rename(String oldPath, String newPath, String userUpdate);

	List<Resource> searchResourcesByName(String baseFolderPath,
			String resourceName);

	void moveResource(String oldPath, String newPath, String userMove);

	Folder getParentFolder(String path);
}
