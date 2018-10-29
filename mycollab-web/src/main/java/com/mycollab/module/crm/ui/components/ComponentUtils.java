/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.ui.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
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
        return new MButton("").withIcon(VaadinIcons.ADJUST)
                .withStyleName(WebThemes.BUTTON_ACTION, WebThemes.BUTTON_SMALL_PADDING)
                .withDescription(UserUIContext.getMessage(GenericI18Enum.OPT_LAYOUT_OPTIONS));
    }
}
