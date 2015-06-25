/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.resources;

import com.esofthead.mycollab.configuration.Storage;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.resources.file.VaadinFileResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;

/**
 * @author MyCollab Ltd.
 * @since 4.5.1
 */
public abstract class VaadinResource {
    private static final String S3_CLS = "com.esofthead.mycollab.vaadin.resources.s3.VaadinS3Resource";

    private static VaadinResource instance;

    static {
        if (Storage.isFileStorage()) {
            instance = new VaadinFileResource();
        } else if (Storage.isS3Storage()) {
            try {
                Class<VaadinResource> cls = (Class<VaadinResource>) Class.forName(S3_CLS);
                instance = cls.newInstance();
            } catch (Exception e) {
                throw new MyCollabException("Exception when load s3 resource file", e);
            }
        } else {
            throw new MyCollabException("Do not support storage system setting. Accept file or s3 only");
        }
    }

    public static VaadinResource getInstance() {
        return instance;
    }

    public abstract Resource getStreamResource(String documentPath);

    public Resource getResource(String documentPath) {
        return new ExternalResource(Storage.getResourcePath(documentPath));
    }

    public Resource getLogoResource(String logoId, int size) {
        return new ExternalResource(Storage.getLogoPath(AppContext.getAccountId(), logoId, size));
    }

    public Resource getAvatarResource(String avatarId, int size) {
        return new ExternalResource(Storage.getAvatarPath(avatarId, size));
    }
}
