/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.ui.components;

import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.vaadin.ui.HeaderWithIcon;

/**
 * @author MyCollab Ltd
 * @since 5.2.2
 */
public class ComponentUtils {
    public static HeaderWithIcon headerH2(String resId, String title) {
        return HeaderWithIcon.h2(ProjectAssetsManager.getAsset(resId), title);
    }

    public static HeaderWithIcon headerH3(String resId, String title) {
        return HeaderWithIcon.h3(ProjectAssetsManager.getAsset(resId), title);
    }
}
