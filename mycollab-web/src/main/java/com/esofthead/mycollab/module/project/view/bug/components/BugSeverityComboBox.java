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

import com.esofthead.mycollab.module.project.BugSeverityConstants;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.ComboBox;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugSeverityComboBox extends ComboBox {

	private static final long serialVersionUID = 1L;

	public BugSeverityComboBox() {
		this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
		IndexedContainer ic = new IndexedContainer();
		ic.addItem(BugSeverityConstants.CRITICAL);
		ic.addItem(BugSeverityConstants.MAJOR);
		ic.addItem(BugSeverityConstants.MINOR);
		ic.addItem(BugSeverityConstants.TRIVIAL);

		this.setContainerDataSource(ic);

		this.setItemIcon(BugSeverityConstants.CRITICAL, MyCollabResource
				.newResource(BugSeverityConstants.CRITICAL_IMG_12));
		this.setItemIcon(BugSeverityConstants.MAJOR,
				MyCollabResource.newResource(BugSeverityConstants.MAJOR_IMG_12));
		this.setItemIcon(BugSeverityConstants.MINOR,
				MyCollabResource.newResource(BugSeverityConstants.MINOR_IMG_12));
		this.setItemIcon(BugSeverityConstants.TRIVIAL, MyCollabResource
				.newResource(BugSeverityConstants.TRIVIAL_IMG_12));
		this.setNullSelectionAllowed(false);
	}
}
