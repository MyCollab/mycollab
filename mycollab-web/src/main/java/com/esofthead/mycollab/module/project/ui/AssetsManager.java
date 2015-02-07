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
package com.esofthead.mycollab.module.project.ui;

import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class AssetsManager {
    private static final Map<String, Resource> resources;

    static {
        resources = new HashMap<>();
        resources.put(ProjectTypeConstants.DASHBOARD, FontAwesome.DASHBOARD);
        resources.put(ProjectTypeConstants.MESSAGE, FontAwesome.COMMENT);
        resources.put(ProjectTypeConstants.MILESTONE, FontAwesome.FLAG_CHECKERED);
        resources.put(ProjectTypeConstants.TASK, FontAwesome.TASKS);
        resources.put(ProjectTypeConstants.PAGE, FontAwesome.FILE);
        resources.put(ProjectTypeConstants.BUG, FontAwesome.BUG);
        resources.put(ProjectTypeConstants.FILE, FontAwesome.BRIEFCASE);
        resources.put(ProjectTypeConstants.RISK, FontAwesome.SHIELD);
        resources.put(ProjectTypeConstants.PROBLEM, FontAwesome.EXCLAMATION_TRIANGLE);
        resources.put(ProjectTypeConstants.TIME, FontAwesome.CLOCK_O);
        resources.put(ProjectTypeConstants.STANDUP, FontAwesome.CUBES);
        resources.put(ProjectTypeConstants.MEMBER, FontAwesome.USERS);
    }

    public static Resource getAsset(String resId) {
        return resources.get(resId);
    }
}
