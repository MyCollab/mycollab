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
package com.esofthead.mycollab.module.project.ui;

import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.vaadin.server.FontAwesome;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
public class ProjectAssetsManager {
    private static final Map<String, FontAwesome> resources;

    static {
        resources = new HashMap<>();
        resources.put(ProjectTypeConstants.DASHBOARD, FontAwesome.DASHBOARD);
        resources.put(ProjectTypeConstants.MESSAGE, FontAwesome.COMMENT);
        resources.put(ProjectTypeConstants.MILESTONE, FontAwesome.FLAG_CHECKERED);
        resources.put(ProjectTypeConstants.TASK, FontAwesome.TASKS);
        resources.put(ProjectTypeConstants.TASK_LIST, FontAwesome.BUILDING_O);
        resources.put(ProjectTypeConstants.PAGE, FontAwesome.FILE);
        resources.put(ProjectTypeConstants.BUG, FontAwesome.BUG);
        resources.put(ProjectTypeConstants.BUG_COMPONENT, FontAwesome.CUBE);
        resources.put(ProjectTypeConstants.BUG_VERSION, FontAwesome.LEAF);
        resources.put(ProjectTypeConstants.FILE, FontAwesome.BRIEFCASE);
        resources.put(ProjectTypeConstants.RISK, FontAwesome.SHIELD);
        resources.put(ProjectTypeConstants.PROBLEM, FontAwesome.EXCLAMATION_TRIANGLE);
        resources.put(ProjectTypeConstants.TIME, FontAwesome.CLOCK_O);
        resources.put(ProjectTypeConstants.STANDUP, FontAwesome.CUBES);
        resources.put(ProjectTypeConstants.MEMBER, FontAwesome.USERS);
        resources.put(ProjectTypeConstants.PROJECT, FontAwesome.CALENDAR_O);
    }

    public static FontAwesome getAsset(String resId) {
        return resources.get(resId);
    }
}
