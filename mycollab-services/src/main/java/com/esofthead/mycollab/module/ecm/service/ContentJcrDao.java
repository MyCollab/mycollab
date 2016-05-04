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
package com.esofthead.mycollab.module.ecm.service;

import com.esofthead.mycollab.module.ecm.domain.Content;
import com.esofthead.mycollab.module.ecm.domain.Folder;
import com.esofthead.mycollab.module.ecm.domain.Resource;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ContentJcrDao {

    void saveContent(Content content, String createdUser);

    void createFolder(Folder folder, String createdUser);

    void rename(String oldPath, String newPath);

    Resource getResource(String path);

    void removeResource(String path);

    List<Resource> getResources(String path);

    List<Content> getContents(String path);

    List<Folder> getSubFolders(String path);

    List<Resource> searchResourcesByName(String baseFolderPath, String resourceName);

    void moveResource(String oldPath, String destinationPath);
}
