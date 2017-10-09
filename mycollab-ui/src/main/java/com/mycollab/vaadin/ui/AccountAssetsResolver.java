package com.mycollab.vaadin.ui;

import com.mycollab.module.file.service.AbstractStorageService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.resources.VaadinResourceFactory;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.BaseTheme;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class AccountAssetsResolver {
    public static Button createAccountLogoImageComponent(String logoId, int size) {
        Button logo = new Button();
        logo.setStyleName(BaseTheme.BUTTON_LINK);
        logo.setIcon(createLogoResource(logoId, size));
        return logo;
    }

    public static Resource createLogoResource(String logoId, int size) {
        if (logoId == null) {
            return new ExternalResource(AppContextUtil.getSpringBean(AbstractStorageService.class)
                    .generateAssetRelativeLink("icons/logo.png"));
        }

        return VaadinResourceFactory.getLogoResource(logoId, size);
    }
}
