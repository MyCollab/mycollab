/**
 * mycollab-ui - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.resources;

import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.MyCollabException;
import com.mycollab.module.file.service.AbstractStorageService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.resources.file.VaadinFileResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
public class VaadinResourceFactory {
    private static final String S3_CLS = "com.mycollab.ondemand.vaadin.resources.s3.VaadinS3Resource";

    private static VaadinResourceFactory _instance = new VaadinResourceFactory();
    private VaadinResource vaadinResource;

    private VaadinResourceFactory() {
        if (SiteConfiguration.isDemandEdition()) {
            try {
                Class<VaadinResource> cls = (Class<VaadinResource>) Class.forName(S3_CLS);
                vaadinResource = cls.newInstance();
            } catch (Exception e) {
                throw new MyCollabException("Exception when load s3 resource file", e);
            }
        } else {
            vaadinResource = new VaadinFileResource();
        }
    }

    public static VaadinResource getInstance() {
        return _instance.vaadinResource;
    }

    public static Resource getResource(String documentPath) {
        return new ExternalResource(AppContextUtil.getSpringBean(AbstractStorageService.class)
                .getResourcePath(documentPath));
    }

    public static Resource getLogoResource(String logoId, int size) {
        return new ExternalResource(AppContextUtil.getSpringBean(AbstractStorageService.class)
                .getLogoPath(AppUI.getAccountId(), logoId, size));
    }

    public static Resource getAvatarResource(String avatarId, int size) {
        return new ExternalResource(AppContextUtil.getSpringBean(AbstractStorageService.class)
                .getAvatarPath(avatarId, size));
    }
}
