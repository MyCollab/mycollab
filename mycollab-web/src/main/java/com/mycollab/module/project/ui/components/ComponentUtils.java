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
