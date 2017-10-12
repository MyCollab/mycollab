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
package com.mycollab.community.module.ecm.service.impl;

import com.mycollab.core.UnsupportedFeatureException;
import com.mycollab.module.ecm.service.DropboxResourceService;
import com.mycollab.module.ecm.domain.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@Service
public class DropboxResourceServiceImpl implements DropboxResourceService {

    @Override
    public List<Resource> getResources(ExternalDrive drive, String path) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");
    }

    @Override
    public List<ExternalFolder> getSubFolders(ExternalDrive drive, String path) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");
    }

    @Override
    public Resource getCurrentResourceByPath(ExternalDrive drive, String path) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");
    }

    @Override
    public Folder getParentResourceFolder(ExternalDrive drive, String childPath) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");
    }

    @Override
    public Folder createNewFolder(ExternalDrive drive, String path) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");
    }

    @Override
    public void saveContent(ExternalDrive drive, Content content, InputStream in) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");

    }

    @Override
    public void rename(ExternalDrive drive, String oldPath, String newPath) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");

    }

    @Override
    public void deleteResource(ExternalDrive drive, String path) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");

    }

    @Override
    public InputStream download(ExternalDrive drive, String path) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");
    }

    @Override
    public void move(ExternalDrive drive, String fromPath, String toPath) {
        throw new UnsupportedFeatureException(
                "This feature is not supported except onsite mode");

    }
}
