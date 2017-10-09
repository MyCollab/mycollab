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
