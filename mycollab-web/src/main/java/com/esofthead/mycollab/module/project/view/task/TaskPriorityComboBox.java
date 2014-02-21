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

public class TaskPriorityComboBox extends ComboBox {

	private static final long serialVersionUID = 1L;

	public static final String PRIORITY_HIGHT_IMG = "icons/12/priority_high.png";
	public static final String PRIORITY_LOW_IMG = "icons/12/priority_low.png";
	public static final String PRIORITY_MEDIUM_IMG = "icons/12/priority_medium.png";
	public static final String PRIORITY_NONE_IMG = "icons/12/priority_none.png";
	public static final String PRIORITY_URGENT_IMG = "icons/12/priority_urgent.png";

	public static final String PRIORITY_HIGHT = "High";
	public static final String PRIORITY_LOW = "Low";
	public static final String PRIORITY_MEDIUM = "Medium";
	public static final String PRIORITY_NONE = "None";
	public static final String PRIORITY_URGENT = "Urgent";

	public TaskPriorityComboBox() {
		this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

		IndexedContainer ic = new IndexedContainer();
		ic.addItem(PRIORITY_URGENT);
		ic.addItem(PRIORITY_HIGHT);
		ic.addItem(PRIORITY_MEDIUM);
		ic.addItem(PRIORITY_LOW);
		ic.addItem(PRIORITY_NONE);

		this.setContainerDataSource(ic);

		this.setItemIcon(PRIORITY_HIGHT,
				MyCollabResource.newResource(PRIORITY_HIGHT_IMG));
		this.setItemIcon(PRIORITY_LOW,
				MyCollabResource.newResource(PRIORITY_LOW_IMG));
		this.setItemIcon(PRIORITY_MEDIUM,
				MyCollabResource.newResource(PRIORITY_MEDIUM_IMG));
		this.setItemIcon(PRIORITY_NONE,
				MyCollabResource.newResource(PRIORITY_NONE_IMG));
		this.setItemIcon(PRIORITY_URGENT,
				MyCollabResource.newResource(PRIORITY_URGENT_IMG));

		this.setNullSelectionAllowed(false);
		this.setValue(this.getItemIds().iterator().next());
	}

	public static Resource getIconResourceByPriority(String priority) {
		Resource iconPriority = MyCollabResource
				.newResource(PRIORITY_MEDIUM_IMG);
		if (PRIORITY_HIGHT.equals(priority)) {
			iconPriority = MyCollabResource.newResource(PRIORITY_HIGHT_IMG);
		} else if (PRIORITY_LOW.equals(priority)) {
			iconPriority = MyCollabResource.newResource(PRIORITY_LOW_IMG);
		} else if (PRIORITY_MEDIUM.equals(priority)) {
			iconPriority = MyCollabResource.newResource(PRIORITY_MEDIUM_IMG);
		} else if (PRIORITY_NONE.equals(priority)) {
			iconPriority = MyCollabResource.newResource(PRIORITY_NONE_IMG);
		} else if (PRIORITY_URGENT.equals(priority)) {
			iconPriority = MyCollabResource.newResource(PRIORITY_URGENT_IMG);
		}
		return iconPriority;
	}
}
