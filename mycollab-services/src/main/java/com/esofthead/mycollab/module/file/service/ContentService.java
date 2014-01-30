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
package com.esofthead.mycollab.module.file.service;

import java.io.InputStream;

import com.esofthead.mycollab.core.persistence.service.IService;

/**
 * Abstract class keep all business services relate to MyCollab content
 * repository.
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public interface ContentService extends IService {
	/**
	 * Save content
	 * 
	 * @param accountId
	 *            account that current user belong to
	 * @param objectPath
	 *            path of content
	 * @param stream
	 *            input stream of content us saved
	 */
	void saveContent(Integer accountId, String objectPath, InputStream stream);

	/**
	 * Get content by path
	 * 
	 * @param accountId
	 *            account that current user belong to
	 * @param objectPath
	 *            path of content
	 * @return return input stream of content. If MyCollab can not find content,
	 *         return null
	 */
	InputStream getContent(Integer accountId, String objectPath);

	/**
	 * Remove content
	 * 
	 * @param accountId
	 *            account that current user belong to
	 * @param objectPath
	 *            path of content
	 */
	void removeContent(Integer accountId, String objectPath);
}
