package com.mycollab.module.project.ui.components;

import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public class ComponentUtils {
    public static HeaderWithFontAwesome headerH2(String resId, String title) {
        return HeaderWithFontAwesome.h2(ProjectAssetsManager.getAsset(resId), title);
    }

    public static HeaderWithFontAwesome headerH3(String resId, String title) {
        return HeaderWithFontAwesome.h3(ProjectAssetsManager.getAsset(resId), title);
    }
}
