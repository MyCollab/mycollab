package com.mycollab.module.crm.ui.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class ComponentUtils {
    public static HeaderWithFontAwesome header(String resId, String title) {
        return HeaderWithFontAwesome.h2(CrmAssetsManager.getAsset(resId), title);
    }

    public static MButton createCustomizeViewButton() {
        return new MButton("").withIcon(FontAwesome.ADJUST)
                .withStyleName(WebThemes.BUTTON_ACTION, WebThemes.BUTTON_SMALL_PADDING)
                .withDescription(UserUIContext.getMessage(GenericI18Enum.OPT_LAYOUT_OPTIONS));
    }
}
