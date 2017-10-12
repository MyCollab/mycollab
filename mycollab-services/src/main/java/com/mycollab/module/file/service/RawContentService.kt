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
package com.mycollab.module.file.service

import com.mycollab.db.persistence.service.IService

import java.io.InputStream

/**
 * MyCollab content repository has two parts: we use jackrabbit to keep content
 * meta data (such as file name, path, tag and etc more in future). The service
 * to get content data (input stream) are handled by
 * `RawContentService`. In practice, you should not work in low-level
 * API as `RawContentService` but `ContentService`
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface RawContentService : IService {
    /**
     * Save content
     *
     * @param objectPath path of content
     * @param stream     input stream
     */
    fun saveContent(objectPath: String, stream: InputStream)

    /**
     * Get content stream
     *
     * @param objectPath path of content
     * @return stream of content has path `objectPath`, otherwise
     * return null
     */
    fun getContentStream(objectPath: String): InputStream

    /**
     * Remove content
     *
     * @param objectPath path of content
     */
    fun removePath(objectPath: String)

    /**
     * Rename content
     *
     * @param oldPath old path of content
     * @param newPath new path of content
     */
    fun renamePath(oldPath: String, newPath: String)

    /**
     * Move content
     *
     * @param oldPath         old path of content
     * @param destinationPath new path of content
     */
    fun movePath(oldPath: String, destinationPath: String)

    /**
     * Get size of content
     *
     * @param objectPath path of content
     * @return return size of content has path is `path`, return 0 if
     * content is not existed
     */
    fun getSize(objectPath: String): Long

}