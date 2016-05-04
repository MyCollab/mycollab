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

import com.esofthead.mycollab.core.persistence.service.IService;

import java.io.InputStream;

/**
 * MyCollab content repository has two parts: we use jackrabbit to keep content
 * meta data (such as file name, path, tag and etc more in future). The service
 * to get content data (input stream) are handled by
 * <code>RawContentService</code>. In practice, you should not work in low-level
 * API as <code>RawContentService</code> but <code>ContentService</code>
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface RawContentService extends IService {
    /**
     * Save content
     *
     * @param objectPath path of content
     * @param stream     input stream
     */
    void saveContent(String objectPath, InputStream stream);

    /**
     * Get content stream
     *
     * @param objectPath path of content
     * @return stream of content has path <code>objectPath</code>, otherwise
     * return null
     */
    InputStream getContentStream(String objectPath);

    /**
     * Remove content
     *
     * @param objectPath path of content
     */
    void removePath(String objectPath);

    /**
     * Rename content
     *
     * @param oldPath old path of content
     * @param newPath new path of content
     */
    void renamePath(String oldPath, String newPath);

    /**
     * Move content
     *
     * @param oldPath         old path of content
     * @param destinationPath new path of content
     */
    void movePath(String oldPath, String destinationPath);

    /**
     * Get size of content
     *
     * @param objectPath path of content
     * @return return size of content has path is <code>path</code>, return 0 if
     * content is not existed
     */
    long getSize(String objectPath);

}