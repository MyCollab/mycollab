/**
 * mycollab-services - Parent pom providing dependency and plugin management for applications
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
package com.mycollab.module.page.service

import com.mycollab.db.persistence.service.IService
import com.mycollab.module.page.domain.Folder
import com.mycollab.module.page.domain.Page
import com.mycollab.module.page.domain.PageResource
import com.mycollab.module.page.domain.PageVersion

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
interface PageService : IService {
    /**
     * @param page
     * @param createdUser
     */
    fun savePage(page: Page, createdUser: String)

    /**
     * @param path
     * @param requestedUser
     * @return
     */
    fun getPage(path: String, requestedUser: String): Page

    /**
     * @param path
     * @return
     */
    fun getFolder(path: String): Folder?

    /**
     * @param path
     * @return
     */
    fun getPageVersions(path: String): List<PageVersion>

    /**
     * @param path
     * @param versionName
     * @return
     */
    fun getPageByVersion(path: String, versionName: String): Page

    /**
     * @param path
     * @param versionName
     * @return the restore page
     */
    fun restorePage(path: String, versionName: String): Page

    /**
     * @param folder
     * @param createdUser
     */
    fun createFolder(folder: Folder, createdUser: String)

    /**
     * @param path
     * @return
     */
    fun getPages(path: String, requestedUser: String): List<Page>

    /**
     * @param path
     * @param requestedUser
     * @return
     */
    fun getResources(path: String, requestedUser: String): List<PageResource>

    /**
     * @param path
     */
    fun removeResource(path: String)
}
