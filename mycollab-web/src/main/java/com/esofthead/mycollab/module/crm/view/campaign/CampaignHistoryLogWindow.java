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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.module.user.ui.components.UserHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFormatter;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignHistoryLogWindow extends HistoryLogWindow {
	private static final long serialVersionUID = 1L;

	public static final FieldGroupFormatter campaignFormatter;

	static {
		campaignFormatter = new FieldGroupFormatter();

		campaignFormatter.generateFieldDisplayHandler("campaignname",
				CampaignI18nEnum.FORM_CAMPAIGN_NAME);
		campaignFormatter
				.generateFieldDisplayHandler("startdate",
						CampaignI18nEnum.FORM_START_DATE,
						FieldGroupFormatter.PRETTY_DATE_FIELD);
		campaignFormatter.generateFieldDisplayHandler("enddate",
				CampaignI18nEnum.FORM_END_DATE, FieldGroupFormatter.PRETTY_DATE_FIELD);
		campaignFormatter.generateFieldDisplayHandler("status",
				CampaignI18nEnum.FORM_STATUS);
		campaignFormatter.generateFieldDisplayHandler("type",
				CampaignI18nEnum.FORM_TYPE);
		campaignFormatter.generateFieldDisplayHandler("currencyid",
				CampaignI18nEnum.FORM_CURRENCY,
				FieldGroupFormatter.CURRENCY_FIELD);
		campaignFormatter.generateFieldDisplayHandler("budget",
				CampaignI18nEnum.FORM_BUDGET);
		campaignFormatter.generateFieldDisplayHandler("expectedcost",
				CampaignI18nEnum.FORM_EXPECTED_COST);
		campaignFormatter.generateFieldDisplayHandler("actualcost",
				CampaignI18nEnum.FORM_ACTUAL_COST);
		campaignFormatter.generateFieldDisplayHandler("expectedrevenue",
				CampaignI18nEnum.FORM_EXPECTED_REVENUE);
		campaignFormatter.generateFieldDisplayHandler("assignuser",
				GenericI18Enum.FORM_ASSIGNEE, new UserHistoryFieldFormat());
		campaignFormatter.generateFieldDisplayHandler("description",
				GenericI18Enum.FORM_DESCRIPTION);
	}

	public CampaignHistoryLogWindow(String module, String type) {
		super(module, type);
	}

	@Override
	protected FieldGroupFormatter buildFormatter() {
		return campaignFormatter;
	}
}
