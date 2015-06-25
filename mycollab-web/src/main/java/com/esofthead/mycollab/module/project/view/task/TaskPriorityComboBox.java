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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.esofthead.mycollab.vaadin.ui.AssetResource;
import com.esofthead.mycollab.vaadin.ui.I18nValueComboBox;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskPriorityComboBox extends I18nValueComboBox {
    private static final long serialVersionUID = 1L;

    public TaskPriorityComboBox() {
        this.setNullSelectionAllowed(false);

        this.loadData(Arrays.asList(TaskPriority.Urgent, TaskPriority.High,
                TaskPriority.Medium, TaskPriority.Low, TaskPriority.None));

        this.setItemIcon(TaskPriority.High.name(), new AssetResource(ProjectResources.T_PRIORITY_HIGHT_IMG));
        this.setItemIcon(TaskPriority.Low.name(), new AssetResource(ProjectResources.T_PRIORITY_LOW_IMG));
        this.setItemIcon(TaskPriority.Medium.name(), new AssetResource(ProjectResources.T_PRIORITY_MEDIUM_IMG));
        this.setItemIcon(TaskPriority.None.name(), new AssetResource(ProjectResources.T_PRIORITY_NONE_IMG));
        this.setItemIcon(TaskPriority.Urgent.name(), new AssetResource(ProjectResources.T_PRIORITY_URGENT_IMG));

        this.setValue(this.getItemIds().iterator().next());
    }
}
