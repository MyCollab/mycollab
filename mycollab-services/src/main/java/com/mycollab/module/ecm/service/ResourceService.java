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
package com.mycollab.module.ecm.service;

import com.mycollab.cache.IgnoreCacheClass;
import com.mycollab.db.persistence.service.IService;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.ecm.domain.Resource;

import java.io.InputStream;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
public interface ResourceService extends IService {

    /**
     * 
     * @param baseFolderPath
     * @param folderName
     * @param description
     * @param createdBy
     * @return
     */
    Folder createNewFolder(String baseFolderPath, String folderName, String description, String createdBy);

    /**
     * @param path
     * @return
     */
    List<Resource> getResources(String path);

    /**
     * @param path
     * @return
     */
    Resource getResource(String path);

    /**
     * @param path
     * @return
     */
    List<Content> getContents(String path);

    /**
     * @param path
     * @return
     */
    List<Folder> getSubFolders(String path);

    /**
     * @param content
     * @param createdUser
     * @param refStream
     * @param sAccountId
     */
    void saveContent(Content content, String createdUser, InputStream refStream, Integer sAccountId);

    /**
     *
     * @param path
     */
    void removeResource(String path);

    /**
     * @param path
     * @param userDelete
     * @param sAccountId
     */
    void removeResource(String path, String userDelete, Boolean isUpdateDriveInfo, Integer sAccountId);

    /**
     * @param path
     * @return
     */
    InputStream getContentStream(String path);

    /**
     * @param oldPath
     * @param newPath
     * @param userUpdate
     */
    void rename(String oldPath, String newPath, String userUpdate);

    /**
     * @param baseFolderPath
     * @param resourceName
     * @return
     */
    List<Resource> searchResourcesByName(String baseFolderPath, String resourceName);

    /**
     * @param oldPath
     * @param newPath
     * @param userMove
     */
    void moveResource(String oldPath, String newPath, String userMove);

    /**
     * @param path
     * @return
     */
    Folder getParentFolder(String path);
}
