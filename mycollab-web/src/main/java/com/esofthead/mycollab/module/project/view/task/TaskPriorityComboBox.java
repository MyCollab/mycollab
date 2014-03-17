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

import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class TaskPriorityComboBox extends ComboBox {
	private static final long serialVersionUID = 1L;

	public TaskPriorityComboBox() {
		this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

		IndexedContainer ic = new IndexedContainer();
		ic.addItem(TaskPriorityStatusContants.PRIORITY_URGENT);
		ic.addItem(TaskPriorityStatusContants.PRIORITY_HIGHT);
		ic.addItem(TaskPriorityStatusContants.PRIORITY_MEDIUM);
		ic.addItem(TaskPriorityStatusContants.PRIORITY_LOW);
		ic.addItem(TaskPriorityStatusContants.PRIORITY_NONE);

		this.setContainerDataSource(ic);

		this.setItemIcon(
				TaskPriorityStatusContants.PRIORITY_HIGHT,
				MyCollabResource
						.newResource(TaskPriorityStatusContants.PRIORITY_HIGHT_IMG));
		this.setItemIcon(
				TaskPriorityStatusContants.PRIORITY_LOW,
				MyCollabResource
						.newResource(TaskPriorityStatusContants.PRIORITY_LOW_IMG));
		this.setItemIcon(
				TaskPriorityStatusContants.PRIORITY_MEDIUM,
				MyCollabResource
						.newResource(TaskPriorityStatusContants.PRIORITY_MEDIUM_IMG));
		this.setItemIcon(
				TaskPriorityStatusContants.PRIORITY_NONE,
				MyCollabResource
						.newResource(TaskPriorityStatusContants.PRIORITY_NONE_IMG));
		this.setItemIcon(
				TaskPriorityStatusContants.PRIORITY_URGENT,
				MyCollabResource
						.newResource(TaskPriorityStatusContants.PRIORITY_URGENT_IMG));

		this.setNullSelectionAllowed(false);
		this.setValue(this.getItemIds().iterator().next());
	}

	public static Resource getIconResourceByPriority(String priority) {
		Resource iconPriority = MyCollabResource
				.newResource(TaskPriorityStatusContants.PRIORITY_MEDIUM_IMG);
		if (TaskPriorityStatusContants.PRIORITY_HIGHT.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(TaskPriorityStatusContants.PRIORITY_HIGHT_IMG);
		} else if (TaskPriorityStatusContants.PRIORITY_LOW.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(TaskPriorityStatusContants.PRIORITY_LOW_IMG);
		} else if (TaskPriorityStatusContants.PRIORITY_MEDIUM.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(TaskPriorityStatusContants.PRIORITY_MEDIUM_IMG);
		} else if (TaskPriorityStatusContants.PRIORITY_NONE.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(TaskPriorityStatusContants.PRIORITY_NONE_IMG);
		} else if (TaskPriorityStatusContants.PRIORITY_URGENT.equals(priority)) {
			iconPriority = MyCollabResource
					.newResource(TaskPriorityStatusContants.PRIORITY_URGENT_IMG);
		}
		return iconPriority;
	}
}
