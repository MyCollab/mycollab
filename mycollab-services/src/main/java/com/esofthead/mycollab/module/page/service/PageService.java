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
package com.esofthead.mycollab.module.page.service;

import java.util.List;

import com.esofthead.mycollab.core.persistence.service.IService;
import com.esofthead.mycollab.module.page.domain.Folder;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.module.page.domain.PageVersion;
import com.esofthead.mycollab.module.page.domain.PageResource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public interface PageService extends IService {
	/**
	 * 
	 * @param page
	 * @param createdUser
	 */
	void savePage(Page page, String createdUser);

	/**
	 * 
	 * @param path
	 * @param requestedUser
	 * @return
	 */
	Page getPage(String path, String requestedUser);

	/**
	 * 
	 * @param path
	 * @return
	 */
	Folder getFolder(String path);

	/**
	 * 
	 * @param path
	 * @return
	 */
	List<PageVersion> getPageVersions(String path);

	/**
	 * 
	 * @param path
	 * @param versionName
	 * @return
	 */
	Page getPageByVersion(String path, String versionName);

	/**
	 * 
	 * @param path
	 * @param versionName
	 * @return the restore page
	 */
	Page restorePage(String path, String versionName);

	/**
	 * 
	 * @param folder
	 * @param createdUser
	 */
	void createFolder(Folder folder, String createdUser);

	/**
	 * 
	 * @param path
	 * @return
	 */
	List<Page> getPages(String path, String requestedUser);

	/**
	 * 
	 * @param path
	 * @param requestedUser
	 * @return
	 */
	List<PageResource> getResources(String path, String requestedUser);

	/**
	 * 
	 * @param path
	 */
	void removeResource(String path);
}
