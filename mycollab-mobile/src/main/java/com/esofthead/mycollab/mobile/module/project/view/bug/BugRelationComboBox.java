/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.bug;

import com.esofthead.mycollab.module.tracker.BugRelationConstants;
import com.vaadin.ui.ListSelect;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class BugRelationComboBox extends ListSelect {

	private static final long serialVersionUID = 1L;

	public BugRelationComboBox() {
		this.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
		this.addItem(BugRelationConstants.PARENT);
		this.addItem(BugRelationConstants.CHILD);
		this.addItem(BugRelationConstants.RELATED);
		this.addItem(BugRelationConstants.DUPLICATED);
		this.addItem(BugRelationConstants.BEFORE);
		this.addItem(BugRelationConstants.AFTER);

		this.setNullSelectionAllowed(false);
		this.select(BugRelationConstants.PARENT);
	}

}
