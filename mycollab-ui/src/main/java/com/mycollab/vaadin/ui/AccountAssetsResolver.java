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
package com.mycollab.vaadin.ui;

import com.mycollab.configuration.StorageFactory;
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
            return new ExternalResource(StorageFactory.generateAssetRelativeLink("icons/logo.png"));
        }

        return VaadinResourceFactory.getLogoResource(logoId, size);
    }
}
