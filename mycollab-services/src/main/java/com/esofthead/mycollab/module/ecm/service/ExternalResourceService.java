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
import com.esofthead.mycollab.module.ecm.domain.ExternalDrive;
import com.esofthead.mycollab.module.ecm.domain.ExternalFolder;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.domain.Resource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@IgnoreCacheClass
public interface ExternalResourceService extends IService {
	/**
	 * 
	 * @param drive
	 * @param path
	 * @return
	 */
	List<Resource> getResources(ExternalDrive drive, String path);

	/**
	 * 
	 * @param drive
	 * @param path
	 * @return
	 */
	List<ExternalFolder> getSubFolders(ExternalDrive drive, String path);

	/**
	 * 
	 * @param drive
	 * @param path
	 * @return
	 */
	Resource getCurrentResourceByPath(ExternalDrive drive, String path);

	/**
	 * 
	 * @param drive
	 * @param childPath
	 * @return
	 */
	Folder getParentResourceFolder(ExternalDrive drive, String childPath);

	/**
	 * 
	 * @param drive
	 * @param path
	 * @return
	 */
	Folder createFolder(ExternalDrive drive, String path);

	/**
	 * 
	 * @param drive
	 * @param content
	 * @param in
	 */
	void saveContent(ExternalDrive drive, Content content, InputStream in);

	/**
	 * 
	 * @param drive
	 * @param oldPath
	 * @param newPath
	 */
	void rename(ExternalDrive drive, String oldPath, String newPath);

	/**
	 * 
	 * @param drive
	 * @param path
	 */
	void deleteResource(ExternalDrive drive, String path);

	/**
	 * 
	 * @param drive
	 * @param path
	 * @return
	 */
	InputStream download(ExternalDrive drive, String path);

	/**
	 * 
	 * @param drive
	 * @param fromPath
	 * @param toPath
	 */
	void move(ExternalDrive drive, String fromPath, String toPath);
}
