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

import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.esofthead.mycollab.vaadin.ui.I18nValueComboBox;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugResolutionComboBox extends I18nValueComboBox {
	private static final long serialVersionUID = 1L;

	private BugResolutionComboBox(boolean nullIsAllowable, Enum<?>... values) {
		super(nullIsAllowable, values);
	}

	public static BugResolutionComboBox getInstanceForWontFixWindow() {
		return new BugResolutionComboBox(false, BugResolution.CannotReproduce,
				BugResolution.Duplicate, BugResolution.Incomplete,
				BugResolution.Won_Fix);
	}

	public static BugResolutionComboBox getInstanceForValidBugWindow() {
		return new BugResolutionComboBox(false, BugResolution.Newissue,
				BugResolution.ReOpen, BugResolution.WaitforVerification);
	}

	public static BugResolutionComboBox getInstanceForResolvedBugWindow() {
		return new BugResolutionComboBox(false, BugResolution.Fixed);
	}
}
