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
package com.esofthead.mycollab.module.user.ui;

import com.vaadin.server.FontAwesome;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class SettingAssetsManager {
    private static final Map<String, FontAwesome> resources;

    static {
        resources = new HashMap<>();
        resources.put(SettingUIConstants.PROFILE, FontAwesome.BOOK);
        resources.put(SettingUIConstants.BILLING, FontAwesome.CREDIT_CARD);
        resources.put(SettingUIConstants.USERS, FontAwesome.USERS);
        resources.put(SettingUIConstants.CUSTOMIZATION, FontAwesome.MAGIC);
    }

    public static FontAwesome getAsset(String resId) {
        return resources.get(resId);
    }
}
