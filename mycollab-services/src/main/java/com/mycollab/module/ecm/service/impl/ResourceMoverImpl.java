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
package com.mycollab.module.ecm.service.impl;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.ecm.ResourceType;
import com.mycollab.module.ecm.ResourceUtils;
import com.mycollab.module.ecm.domain.Content;
import com.mycollab.module.ecm.domain.Folder;
import com.mycollab.module.ecm.domain.Resource;
import com.mycollab.module.ecm.service.ExternalResourceService;
import com.mycollab.module.ecm.service.ResourceMover;
import com.mycollab.module.ecm.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class ResourceMoverImpl implements ResourceMover {

    @Autowired
    private ResourceService resourceService;

    private void moveResourceInDifferentStorage(Resource srcRes, Resource destRes, String userMove, Integer sAccountId) {
        String destMovePath = destRes.getPath() + "/" + srcRes.getName();

        if (srcRes instanceof Folder) {
            Folder createdFolder;
            List<Resource> lstRes;

            if (ResourceUtils.getType(destRes) != ResourceType.MyCollab) {
                ExternalResourceService destService = ResourceUtils.getExternalResourceService(ResourceUtils.getType(destRes));
                createdFolder = destService.createNewFolder(ResourceUtils.getExternalDrive(destRes), destMovePath);
            } else {
                createdFolder = resourceService.createNewFolder(destRes.getPath(), srcRes.getName(), "", userMove);
            }

            if (ResourceUtils.getType(srcRes) != ResourceType.MyCollab) {
                ExternalResourceService srcService = ResourceUtils.getExternalResourceService(ResourceUtils.getType(srcRes));
                lstRes = srcService.getResources(ResourceUtils.getExternalDrive(srcRes), srcRes.getPath());
            } else {
                lstRes = resourceService.getResources(srcRes.getPath());
            }

            for (Resource res : lstRes) {
                if (res instanceof Folder) {
                    moveResourceInDifferentStorage(res, createdFolder, userMove, sAccountId);
                } else {
                    copyFile((Content) res, createdFolder, userMove, sAccountId);
                }
            }
        } else {
            copyFile((Content) srcRes, destRes, userMove, sAccountId);
        }
    }

    private void copyFile(Content srcRes, Resource destRes, String userMove, Integer sAccountId) {
        // get input stream of download
        String destMovePath = destRes.getPath() + "/" + srcRes.getName();
        String srcPath = srcRes.getPath();

        InputStream in;
        ExternalResourceService srcService;
        if (ResourceUtils.getType(srcRes) != ResourceType.MyCollab) {
            srcService = ResourceUtils.getExternalResourceService(ResourceUtils.getType(srcRes));
            in = srcService.download(ResourceUtils.getExternalDrive(srcRes), srcRes.getPath());
        } else {
            in = resourceService.getContentStream(srcPath);
        }

        // update path of srcRes -------------------------
        srcRes.setPath(destMovePath);

        // ------------------------------------------------
        if (ResourceUtils.getType(destRes) != ResourceType.MyCollab) {
            ExternalResourceService destService = ResourceUtils.getExternalResourceService(ResourceUtils.getType(destRes));
            destService.saveContent(ResourceUtils.getExternalDrive(destRes), srcRes, in);
        } else {
            resourceService.saveContent(srcRes, userMove, in, sAccountId);
        }
    }

    private boolean checkIsTheSameAccountInStorage(Resource srcRes, Resource destRes) {
        return ResourceUtils.getType(srcRes) == ResourceUtils.getType(destRes) &&
                ResourceUtils.getExternalDrive(srcRes).getAccesstoken().equals(ResourceUtils.getExternalDrive(destRes).getAccesstoken());
    }

    private boolean isDuplicateFileName(Resource srcRes, Resource destRes) {
        if (ResourceUtils.getType(destRes) == ResourceType.MyCollab) {
            List<Resource> lstRes = resourceService.getResources(destRes.getPath());
            for (Resource res : lstRes) {
                if (srcRes.getName().equals(res.getName()))
                    return true;
            }
        } else {
            ExternalResourceService service = ResourceUtils.getExternalResourceService(ResourceUtils.getType(destRes));
            List<Resource> lstRes = service.getResources(ResourceUtils.getExternalDrive(destRes), destRes.getPath());
            for (Resource res : lstRes) {
                if (srcRes.getName().equals(res.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * destRes must be instanceof Folder
     */
    @Override
    public void moveResource(Resource srcRes, Resource destRes, String userMove, Integer sAccountId) {
        ResourceType srcType = ResourceUtils.getType(srcRes);
        ResourceType destType = ResourceUtils.getType(destRes);

        if (destRes instanceof Content)
            throw new MyCollabException("You cant move somethings to content path.That is impossible.");
        if (isDuplicateFileName(srcRes, destRes)) {
            throw new MyCollabException("Please check duplicate file, before move");
        }

        if (srcType == ResourceType.MyCollab && destType == ResourceType.MyCollab) {
            resourceService.moveResource(srcRes.getPath(), destRes.getPath(), userMove);
        } else if (srcType == destType && srcType != ResourceType.MyCollab) {
            if (checkIsTheSameAccountInStorage(srcRes, destRes)) {
                ExternalResourceService service = ResourceUtils.getExternalResourceService(ResourceUtils.getType(srcRes));
                service.move(ResourceUtils.getExternalDrive(srcRes), srcRes.getPath(),
                        destRes.getPath() + "/" + srcRes.getName());
            } else {
                moveResourceInDifferentStorage(srcRes, destRes, userMove, sAccountId);
                // delete src resource
                ExternalResourceService srcService = ResourceUtils.getExternalResourceService(ResourceUtils.getType(srcRes));
                srcService.deleteResource(ResourceUtils.getExternalDrive(srcRes), srcRes.getPath());
            }
        } else {
            moveResourceInDifferentStorage(srcRes, destRes, userMove, sAccountId);

            if (ResourceUtils.getType(srcRes) != ResourceType.MyCollab) {
                ExternalResourceService srcService = ResourceUtils.getExternalResourceService(ResourceUtils.getType(srcRes));
                srcService.deleteResource(ResourceUtils.getExternalDrive(srcRes), srcRes.getPath());
            } else {
                resourceService.removeResource(srcRes.getPath(), userMove, true, sAccountId);
            }
        }
    }
}
