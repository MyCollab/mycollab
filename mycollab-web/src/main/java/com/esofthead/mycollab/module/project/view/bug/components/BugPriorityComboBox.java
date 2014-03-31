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

import com.esofthead.mycollab.module.project.view.bug.BugPriorityStatusConstants;
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
		ic.addItem(BugPriorityStatusConstants.PRIORITY_BLOCKER);
		ic.addItem(BugPriorityStatusConstants.PRIORITY_CRITICAL);
		ic.addItem(BugPriorityStatusConstants.PRIORITY_MAJOR);
		ic.addItem(BugPriorityStatusConstants.PRIORITY_MINOR);
		ic.addItem(BugPriorityStatusConstants.PRIORITY_TRIVIAL);

		this.setContainerDataSource(ic);

		this.setItemIcon(
				BugPriorityStatusConstants.PRIORITY_BLOCKER,
				MyCollabResource
						.newResource(BugPriorityStatusConstants.PRIORITY_BLOCKER_IMG_12));
		this.setItemIcon(
				BugPriorityStatusConstants.PRIORITY_CRITICAL,
				MyCollabResource
						.newResource(BugPriorityStatusConstants.PRIORITY_CRITICAL_IMG_12));
		this.setItemIcon(
				BugPriorityStatusConstants.PRIORITY_MAJOR,
				MyCollabResource
						.newResource(BugPriorityStatusConstants.PRIORITY_MAJOR_IMG_12));
		this.setItemIcon(
				BugPriorityStatusConstants.PRIORITY_MINOR,
				MyCollabResource
						.newResource(BugPriorityStatusConstants.PRIORITY_MINOR_IMG_12));
		this.setItemIcon(
				BugPriorityStatusConstants.PRIORITY_TRIVIAL,
				MyCollabResource
						.newResource(BugPriorityStatusConstants.PRIORITY_TRIVIAL_IMG_12));
		this.setNullSelectionAllowed(false);
	}
}
