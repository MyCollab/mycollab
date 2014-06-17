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
package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.ui.format.ProjectMemberHistoryFieldFormat;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HistoryLogComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
class MilestoneHistoryLogList extends HistoryLogComponent {
	private static final long serialVersionUID = 1L;

	public MilestoneHistoryLogList(String module, String type) {
		super(module, type);

		this.generateFieldDisplayHandler("name",
				AppContext.getMessage(MilestoneI18nEnum.FORM_NAME_FIELD));
		this.generateFieldDisplayHandler("status",
				AppContext.getMessage(MilestoneI18nEnum.FORM_STATUS_FIELD));
		this.generateFieldDisplayHandler("owner",
				AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD),
				new ProjectMemberHistoryFieldFormat());
		this.generateFieldDisplayHandler("startdate",
				AppContext.getMessage(MilestoneI18nEnum.FORM_START_DATE_FIELD),
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("enddate",
				AppContext.getMessage(MilestoneI18nEnum.FORM_END_DATE_FIELD),
				HistoryLogComponent.DATE_FIELD);
	}

}
