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
package com.esofthead.mycollab.module.crm.view.opportunity;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
import com.esofthead.mycollab.module.user.ui.components.UserHistoryFieldFormat;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HistoryLogComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
class OpportunityHistoryLogWindow extends HistoryLogWindow {
	private static final long serialVersionUID = 1L;

	public OpportunityHistoryLogWindow(String module, String type) {
		super(module, type);

		this.generateFieldDisplayHandler("opportunityname",
				AppContext.getMessage(OpportunityI18nEnum.FORM_NAME));
		this.generateFieldDisplayHandler("currencyid",
				AppContext.getMessage(OpportunityI18nEnum.FORM_CURRENCY),
				HistoryLogComponent.CURRENCY_FIELD);
		this.generateFieldDisplayHandler("amount",
				AppContext.getMessage(OpportunityI18nEnum.FORM_AMOUNT));
		this.generateFieldDisplayHandler("salesstage",
				AppContext.getMessage(OpportunityI18nEnum.FORM_SALE_STAGE));
		this.generateFieldDisplayHandler("probability",
				AppContext.getMessage(OpportunityI18nEnum.FORM_SALE_STAGE));
		this.generateFieldDisplayHandler("nextstep",
				AppContext.getMessage(OpportunityI18nEnum.FORM_NEXT_STEP));
		this.generateFieldDisplayHandler("accountid",
				AppContext.getMessage(OpportunityI18nEnum.FORM_ACCOUNT_NAME));
		this.generateFieldDisplayHandler("expectedcloseddate", AppContext
				.getMessage(OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE),
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("opportunitytype",
				AppContext.getMessage(OpportunityI18nEnum.FORM_TYPE));
		this.generateFieldDisplayHandler("source",
				AppContext.getMessage(OpportunityI18nEnum.FORM_LEAD_SOURCE));
		this.generateFieldDisplayHandler("campaignid",
				AppContext.getMessage(OpportunityI18nEnum.FORM_CAMPAIGN_NAME));
		this.generateFieldDisplayHandler("assignuser",
				AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE),
				new UserHistoryFieldFormat());
		this.generateFieldDisplayHandler("description",
				AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION));
	}
}
