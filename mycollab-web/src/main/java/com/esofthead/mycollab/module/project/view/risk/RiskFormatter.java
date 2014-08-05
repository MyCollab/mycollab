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
package com.esofthead.mycollab.module.project.view.risk;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RiskI18nEnum;
import com.esofthead.mycollab.module.project.ui.format.ProjectMemberHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFomatter;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.3.3
 *
 */
public class RiskFormatter extends FieldGroupFomatter {
	public static final RiskFormatter instance;

	static {
		instance = new RiskFormatter();
	}

	public RiskFormatter() {
		super();

		this.generateFieldDisplayHandler("riskname", RiskI18nEnum.FORM_NAME);
		this.generateFieldDisplayHandler("description",
				RiskI18nEnum.FORM_DESCRIPTION);
		this.generateFieldDisplayHandler("raisedbyuser",
				RiskI18nEnum.FORM_RAISED_BY,
				new ProjectMemberHistoryFieldFormat());
		this.generateFieldDisplayHandler("assigntouser",
				GenericI18Enum.FORM_ASSIGNEE,
				new ProjectMemberHistoryFieldFormat());
		this.generateFieldDisplayHandler("consequence",
				RiskI18nEnum.FORM_CONSEQUENCE);

		this.generateFieldDisplayHandler("datedue", RiskI18nEnum.FORM_DATE_DUE,
				FieldGroupFomatter.DATE_FIELD);
		this.generateFieldDisplayHandler("probalitity",
				RiskI18nEnum.FORM_PROBABILITY);
		this.generateFieldDisplayHandler("status", RiskI18nEnum.FORM_STATUS,
				new I18nHistoryFieldFormat(StatusI18nEnum.class));
		this.generateFieldDisplayHandler("level", RiskI18nEnum.FORM_RATING);
		this.generateFieldDisplayHandler("response", RiskI18nEnum.FORM_RESPONSE);
	}
}
