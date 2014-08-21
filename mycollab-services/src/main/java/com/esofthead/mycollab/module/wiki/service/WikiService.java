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
package com.esofthead.mycollab.module.wiki.service;

import java.util.List;

import javax.jcr.version.Version;

import com.esofthead.mycollab.module.wiki.domain.Folder;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.module.wiki.domain.WikiResource;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
public interface WikiService {
	/**
	 * 
	 * @param page
	 * @param createdUser
	 */
	void savePage(Page page, String createdUser);

	/**
	 * 
	 * @param path
	 * @return
	 */
	Page getPage(String path);
	
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
	List<Version> getPageVersions(String path);

	Page getPageByVersion(String path, String versionName);

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
	List<Page> getPages(String path);

	List<WikiResource> getResources(String path);

	/**
	 * 
	 * @param path
	 */
	void removeResource(String path);
}
