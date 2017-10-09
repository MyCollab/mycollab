package com.mycollab.vaadin.ui;

import com.mycollab.module.file.service.AbstractStorageService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.resources.VaadinResourceFactory;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Image;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class UserAvatarControlFactory {
    public static Image createUserAvatarEmbeddedComponent(String avatarId, int size) {
        return new Image(null, createAvatarResource(avatarId, size));
    }

    public static Image createUserAvatarEmbeddedComponent(String avatarId, int size, String tooltip) {
        Image embedded = new Image(null, createAvatarResource(avatarId, size));
        embedded.setDescription(tooltip);
        return embedded;
    }

    public static Resource createAvatarResource(String avatarId, int size) {
        if (avatarId == null) {
            return new ExternalResource(AppContextUtil.getSpringBean(AbstractStorageService.class)
                    .generateAssetRelativeLink((String.format("icons/default_user_avatar_%d.png", size))));
        }
        return VaadinResourceFactory.getAvatarResource(avatarId, size);
    }
}
