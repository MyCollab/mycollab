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

import com.esofthead.mycollab.module.tracker.BugResolutionConstants;
import com.esofthead.mycollab.vaadin.ui.ValueComboBox;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugResolutionComboBox extends ValueComboBox {
	private static final long serialVersionUID = 1L;

	private BugResolutionComboBox(boolean nullIsAllowable, String... values) {
		super(nullIsAllowable, values);
	}

	public static BugResolutionComboBox getInstanceForWontFixWindow() {
		return new BugResolutionComboBox(false, new String[] {
				BugResolutionConstants.CAN_NOT_REPRODUCE,
				BugResolutionConstants.DUPLICATE,
				BugResolutionConstants.INCOMPLETE,
				BugResolutionConstants.WON_FIX });
	}

	public static BugResolutionComboBox getInstanceForValidBugWindow() {
		return new BugResolutionComboBox(false, new String[] {
				BugResolutionConstants.NEWISSUE, BugResolutionConstants.REOPEN,
				BugResolutionConstants.WAITFORVERIFICATION });
	}

	public static BugResolutionComboBox getInstanceForResolvedBugWindow() {
		return new BugResolutionComboBox(false,
				new String[] { BugResolutionConstants.FIXED });
	}
}
