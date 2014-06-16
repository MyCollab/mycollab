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

package com.esofthead.mycollab.module.crm.view.campaign;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.module.user.ui.components.UserHistoryFieldFormat;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HistoryLogComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
class CampaignHistoryLogWindow extends HistoryLogWindow {
	private static final long serialVersionUID = 1L;

	public CampaignHistoryLogWindow(String module, String type) {
		super(module, type);

		this.generateFieldDisplayHandler("campaignname",
				AppContext.getMessage(CampaignI18nEnum.FORM_CAMPAIGN_NAME));
		this.generateFieldDisplayHandler("startdate",
				AppContext.getMessage(CampaignI18nEnum.FORM_START_DATE),
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("enddate",
				AppContext.getMessage(CampaignI18nEnum.FORM_END_DATE),
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("status",
				AppContext.getMessage(CampaignI18nEnum.FORM_STATUS));
		this.generateFieldDisplayHandler("type",
				AppContext.getMessage(CampaignI18nEnum.FORM_TYPE));
		this.generateFieldDisplayHandler("currencyid",
				AppContext.getMessage(CampaignI18nEnum.FORM_CURRENCY),
				HistoryLogComponent.CURRENCY_FIELD);
		this.generateFieldDisplayHandler("budget",
				AppContext.getMessage(CampaignI18nEnum.FORM_BUDGET));
		this.generateFieldDisplayHandler("expectedcost",
				AppContext.getMessage(CampaignI18nEnum.FORM_EXPECTED_COST));
		this.generateFieldDisplayHandler("actualcost",
				AppContext.getMessage(CampaignI18nEnum.FORM_ACTUAL_COST));
		this.generateFieldDisplayHandler("expectedrevenue",
				AppContext.getMessage(CampaignI18nEnum.FORM_EXPECTED_REVENUE));
		this.generateFieldDisplayHandler("assignuser",
				AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD),
				new UserHistoryFieldFormat());
		this.generateFieldDisplayHandler("description",
				AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION));
	}
}
