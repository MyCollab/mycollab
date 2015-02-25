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

package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.utils.FieldGroupFormatter;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ProjectMemberHistoryLogWindow extends HistoryLogWindow {
	private static final long serialVersionUID = 1L;

	public static final FieldGroupFormatter projectMememberFormatter;

	static {
		projectMememberFormatter = new FieldGroupFormatter();

		projectMememberFormatter.generateFieldDisplayHandler("username",
				ProjectMemberI18nEnum.FORM_USER);

		// TODO: Display role name in history
		projectMememberFormatter.generateFieldDisplayHandler("projectroleid",
				ProjectMemberI18nEnum.FORM_ROLE);
	}

	public ProjectMemberHistoryLogWindow(String module, String type) {
		super(module, type);
	}

	@Override
	protected FieldGroupFormatter buildFormatter() {
		return projectMememberFormatter;
	}
}
