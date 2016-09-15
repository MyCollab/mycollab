/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.ui.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.server.FontAwesome;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class ComponentUtils {
    public static final HeaderWithFontAwesome header(String resId, String title) {
        return HeaderWithFontAwesome.h2(CrmAssetsManager.getAsset(resId), title);
    }

    public static final MButton createCustomizeViewButton() {
        MButton customizeViewBtn = new MButton("").withIcon(FontAwesome.ADJUST)
                .withStyleName(WebUIConstants.BUTTON_ACTION, WebUIConstants.BUTTON_SMALL_PADDING);
        customizeViewBtn.setDescription(UserUIContext.getMessage(GenericI18Enum.OPT_LAYOUT_OPTIONS));
        return customizeViewBtn;
    }

    public static final MButton createImportEntitiesButton() {
        MButton importBtn = new MButton("").withIcon(FontAwesome.CLOUD_UPLOAD)
                .withStyleName(WebUIConstants.BUTTON_ACTION, WebUIConstants.BUTTON_SMALL_PADDING);
        importBtn.setDescription("Import");
        return importBtn;
    }
}
