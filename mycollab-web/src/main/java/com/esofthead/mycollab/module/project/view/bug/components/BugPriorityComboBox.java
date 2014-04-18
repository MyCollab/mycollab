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
package com.esofthead.mycollab.module.project.view.bug.components;

import com.esofthead.mycollab.module.project.BugPriorityStatusConstants;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class BugPriorityComboBox extends ComboBox {

	private static final long serialVersionUID = 1L;

	public BugPriorityComboBox() {

		this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

		IndexedContainer ic = new IndexedContainer();
		ic.addItem(BugPriorityStatusConstants.BLOCKER);
		ic.addItem(BugPriorityStatusConstants.CRITICAL);
		ic.addItem(BugPriorityStatusConstants.MAJOR);
		ic.addItem(BugPriorityStatusConstants.MINOR);
		ic.addItem(BugPriorityStatusConstants.TRIVIAL);

		this.setContainerDataSource(ic);

		this.setItemIcon(BugPriorityStatusConstants.BLOCKER, MyCollabResource
				.newResource(ProjectResources.B_PRIORITY_BLOCKER_IMG_12));
		this.setItemIcon(BugPriorityStatusConstants.CRITICAL, MyCollabResource
				.newResource(ProjectResources.B_PRIORITY_CRITICAL_IMG_12));
		this.setItemIcon(BugPriorityStatusConstants.MAJOR, MyCollabResource
				.newResource(ProjectResources.B_PRIORITY_MAJOR_IMG_12));
		this.setItemIcon(BugPriorityStatusConstants.MINOR, MyCollabResource
				.newResource(ProjectResources.B_PRIORITY_MINOR_IMG_12));
		this.setItemIcon(BugPriorityStatusConstants.TRIVIAL, MyCollabResource
				.newResource(ProjectResources.B_PRIORITY_TRIVIAL_IMG_12));
		this.setNullSelectionAllowed(false);
	}
}
