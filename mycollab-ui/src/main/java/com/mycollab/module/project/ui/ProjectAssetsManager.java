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
package com.mycollab.module.project.ui;

import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
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
        resources.put(ProjectTypeConstants.TICKET, FontAwesome.LIST);
        resources.put(ProjectTypeConstants.PAGE, FontAwesome.FILE);
        resources.put(ProjectTypeConstants.BUG, FontAwesome.BUG);
        resources.put(ProjectTypeConstants.BUG_COMPONENT, FontAwesome.CUBE);
        resources.put(ProjectTypeConstants.BUG_VERSION, FontAwesome.LEAF);
        resources.put(ProjectTypeConstants.FILE, FontAwesome.BRIEFCASE);
        resources.put(ProjectTypeConstants.RISK, FontAwesome.SHIELD);
        resources.put(ProjectTypeConstants.FINANCE, FontAwesome.MONEY);
        resources.put(ProjectTypeConstants.TIME, FontAwesome.CLOCK_O);
        resources.put(ProjectTypeConstants.INVOICE, FontAwesome.CREDIT_CARD);
        resources.put(ProjectTypeConstants.STANDUP, FontAwesome.CUBES);
        resources.put(ProjectTypeConstants.MEMBER, FontAwesome.USERS);
        resources.put(ProjectTypeConstants.PROJECT, FontAwesome.CALENDAR_O);
    }

    public static FontAwesome getAsset(String resId) {
        FontAwesome font = resources.get(resId);
        return (font != null) ? font : FontAwesome.DASHBOARD;
    }

    public static String toHexString(String resId) {
        return "&#x" + Integer.toHexString(resources.get(resId).getCodepoint());
    }

    public static FontAwesome getPriority(String priority) {
        if (Priority.Urgent.name().equals(priority) || Priority.High.name()
                .equals(priority) || Priority.Medium.name().equals(priority) || priority == null) {
            return FontAwesome.ARROW_UP;
        } else {
            return FontAwesome.ARROW_DOWN;
        }
    }

    public static FontAwesome getMilestoneStatus(String status) {
        if (MilestoneStatus.Closed.name().equals(status)) {
            return FontAwesome.MINUS_CIRCLE;
        } else if (MilestoneStatus.InProgress.name().equals(status)) {
            return FontAwesome.CLOCK_O;
        } else {
            return FontAwesome.SPINNER;
        }
    }

    public static String getTaskPriorityHtml(String taskPriority) {
        if (StringUtils.isBlank(taskPriority)) {
            taskPriority = Priority.Medium.name();
        }
        FontAwesome fontAwesome = getPriority(taskPriority);
        return String.format("<span class=\"task-%s v-icon\" style=\"font-family: FontAwesome;\">&#x%s;</span>",
                taskPriority.toLowerCase(), Integer.toHexString(fontAwesome.getCodepoint()));
    }
}
