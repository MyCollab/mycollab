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

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.db.persistence.service.IService
import com.mycollab.module.ecm.domain.*

import java.io.InputStream

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
interface ExternalResourceService : IService {
    /**
     * @param drive
     * @param path
     * @return
     */
    fun getResources(drive: ExternalDrive, path: String): List<Resource>

    /**
     * @param drive
     * @param path
     * @return
     */
    fun getSubFolders(drive: ExternalDrive, path: String): List<ExternalFolder>

    /**
     * @param drive
     * @param path
     * @return
     */
    fun getCurrentResourceByPath(drive: ExternalDrive, path: String): Resource

    /**
     * @param drive
     * @param childPath
     * @return
     */
    fun getParentResourceFolder(drive: ExternalDrive, childPath: String): Folder

    /**
     * @param drive
     * @param path
     * @return
     */
    fun createNewFolder(drive: ExternalDrive, path: String): Folder

    /**
     * @param drive
     * @param content
     * @param in
     */
    fun saveContent(drive: ExternalDrive, content: Content, `in`: InputStream)

    /**
     * @param drive
     * @param oldPath
     * @param newPath
     */
    fun rename(drive: ExternalDrive, oldPath: String, newPath: String)

    /**
     * @param drive
     * @param path
     */
    fun deleteResource(drive: ExternalDrive, path: String)

    /**
     * @param drive
     * @param path
     * @return
     */
    fun download(drive: ExternalDrive, path: String): InputStream

    /**
     * @param drive
     * @param fromPath
     * @param toPath
     */
    fun move(drive: ExternalDrive, fromPath: String, toPath: String)
}
