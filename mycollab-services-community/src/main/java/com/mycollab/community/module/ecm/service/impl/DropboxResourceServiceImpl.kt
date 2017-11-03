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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.ecm.service.impl

import com.mycollab.core.UnsupportedFeatureException
import com.mycollab.module.ecm.service.DropboxResourceService
import com.mycollab.module.ecm.domain.*
import org.springframework.stereotype.Service

import java.io.InputStream

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@Service
class DropboxResourceServiceImpl : DropboxResourceService {

    override fun getResources(drive: ExternalDrive, path: String): List<Resource> {
        throw UnsupportedFeatureException("This feature is not supported except onsite mode")
    }

    override fun getSubFolders(drive: ExternalDrive, path: String): List<ExternalFolder> {
        throw UnsupportedFeatureException("This feature is not supported except onsite mode")
    }

    override fun getCurrentResourceByPath(drive: ExternalDrive, path: String): Resource {
        throw UnsupportedFeatureException("This feature is not supported except onsite mode")
    }

    override fun getParentResourceFolder(drive: ExternalDrive, childPath: String): Folder {
        throw UnsupportedFeatureException("This feature is not supported except onsite mode")
    }

    override fun createNewFolder(drive: ExternalDrive, path: String): Folder {
        throw UnsupportedFeatureException("This feature is not supported except onsite mode")
    }

    override fun saveContent(drive: ExternalDrive, content: Content, `in`: InputStream) {
        throw UnsupportedFeatureException("This feature is not supported except onsite mode")
    }

    override fun rename(drive: ExternalDrive, oldPath: String, newPath: String) {
        throw UnsupportedFeatureException("This feature is not supported except onsite mode")
    }

    override fun deleteResource(drive: ExternalDrive, path: String) {
        throw UnsupportedFeatureException("This feature is not supported except onsite mode")
    }

    override fun download(drive: ExternalDrive, path: String): InputStream {
        throw UnsupportedFeatureException("This feature is not supported except onsite mode")
    }

    override fun move(drive: ExternalDrive, fromPath: String, toPath: String) {
        throw UnsupportedFeatureException("This feature is not supported except onsite mode")
    }
}
