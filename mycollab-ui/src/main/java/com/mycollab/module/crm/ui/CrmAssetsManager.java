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
package com.mycollab.module.crm.ui;

import com.mycollab.module.crm.CrmTypeConstants;
import com.vaadin.server.FontAwesome;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class CrmAssetsManager {
    private static final Map<String, FontAwesome> resources;

    static {
        resources = new HashMap<>();
        resources.put(CrmTypeConstants.ACCOUNT, FontAwesome.INSTITUTION);
        resources.put(CrmTypeConstants.CONTACT, FontAwesome.USER);
        resources.put(CrmTypeConstants.OPPORTUNITY, FontAwesome.MONEY);
        resources.put(CrmTypeConstants.CASE, FontAwesome.BUG);
        resources.put(CrmTypeConstants.LEAD, FontAwesome.BUILDING);
        resources.put(CrmTypeConstants.ACTIVITY, FontAwesome.CALENDAR);
        resources.put(CrmTypeConstants.TASK, FontAwesome.LIST_ALT);
        resources.put(CrmTypeConstants.CALL, FontAwesome.PHONE);
        resources.put(CrmTypeConstants.MEETING, FontAwesome.PLANE);
        resources.put(CrmTypeConstants.CAMPAIGN, FontAwesome.TROPHY);
        resources.put(CrmTypeConstants.DETAIL, FontAwesome.LIST);
        resources.put(CrmTypeConstants.NOTE, FontAwesome.PENCIL);
    }

    public static FontAwesome getAsset(String resId) {
        return resources.get(resId);
    }
}
