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

package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.ui.format.MilestoneHistoryFieldFormat;
import com.esofthead.mycollab.module.project.ui.format.ProjectMemberHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFormatter;
import com.esofthead.mycollab.utils.FieldGroupFormatter.I18nHistoryFieldFormat;
import com.esofthead.mycollab.vaadin.ui.HistoryLogComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class BugHistoryList extends HistoryLogComponent {

	public static final FieldGroupFormatter bugFomatter;

	static {
		bugFomatter = new FieldGroupFormatter();

		bugFomatter.generateFieldDisplayHandler("description",
				GenericI18Enum.FORM_DESCRIPTION);
		bugFomatter.generateFieldDisplayHandler("environment",
				BugI18nEnum.FORM_ENVIRONMENT);
		bugFomatter.generateFieldDisplayHandler("summary",
				BugI18nEnum.FORM_SUMMARY);
		bugFomatter.generateFieldDisplayHandler("status",
				BugI18nEnum.FORM_STATUS, new I18nHistoryFieldFormat(
						BugStatus.class));
		bugFomatter.generateFieldDisplayHandler("priority",
				BugI18nEnum.FORM_PRIORITY, new I18nHistoryFieldFormat(
						BugPriority.class));
		bugFomatter.generateFieldDisplayHandler("severity",
				BugI18nEnum.FORM_SEVERITY, new I18nHistoryFieldFormat(
						BugSeverity.class));
		bugFomatter.generateFieldDisplayHandler("resolution",
				BugI18nEnum.FORM_RESOLUTION, new I18nHistoryFieldFormat(
						BugResolution.class));
		bugFomatter.generateFieldDisplayHandler("estimateremaintime",
				BugI18nEnum.FORM_REMAIN_ESTIMATE);
		bugFomatter.generateFieldDisplayHandler("duedate",
				BugI18nEnum.FORM_DUE_DATE, FieldGroupFormatter.DATE_FIELD);
		bugFomatter.generateFieldDisplayHandler("createdTime",
				BugI18nEnum.FORM_CREATED_TIME, FieldGroupFormatter.DATE_FIELD);
		bugFomatter.generateFieldDisplayHandler("loguserFullName",
				BugI18nEnum.FORM_LOG_BY, new ProjectMemberHistoryFieldFormat());
		bugFomatter.generateFieldDisplayHandler("assignuser",
				GenericI18Enum.FORM_ASSIGNEE,
				new ProjectMemberHistoryFieldFormat());
		bugFomatter.generateFieldDisplayHandler("milestoneid",
				TaskI18nEnum.FORM_PHASE, new MilestoneHistoryFieldFormat());
	}

	public BugHistoryList() {
		super(ModuleNameConstants.PRJ, ProjectTypeConstants.BUG);
		this.setMargin(true);
	}

	@Override
	protected FieldGroupFormatter buildFormatter() {
		return bugFomatter;
	}
}
