/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.ecm.service

import com.mycollab.module.ecm.domain.Content
import com.mycollab.module.ecm.domain.Folder
import com.mycollab.module.ecm.domain.Resource

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ContentJcrDao {

    fun saveContent(content: Content, createdUser: String)

    fun createFolder(folder: Folder, createdUser: String)

    fun rename(oldPath: String, newPath: String)

    fun getResource(path: String): Resource?

    fun removeResource(path: String)

    fun getResources(path: String): List<Resource>

    fun getContents(path: String): List<Content>

    fun getSubFolders(path: String): List<Folder>

    fun searchResourcesByName(baseFolderPath: String, resourceName: String): List<Resource>

    fun moveResource(oldPath: String, destinationPath: String)
}
