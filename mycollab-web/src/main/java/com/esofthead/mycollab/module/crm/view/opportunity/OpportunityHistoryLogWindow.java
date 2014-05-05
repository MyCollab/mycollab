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

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.module.crm.ui.components.HistoryLogWindow;
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

		this.generateFieldDisplayHandler("opportunityname", "Opportunity Name");
		this.generateFieldDisplayHandler("currencyid", "Currency",
				HistoryLogComponent.CURRENCY_FIELD);
		this.generateFieldDisplayHandler("amount", "Amount");
		this.generateFieldDisplayHandler("salesstage", "Sales Stage");
		this.generateFieldDisplayHandler("probability", "Probability (%)");
		this.generateFieldDisplayHandler("nextstep", "Next Step");
		this.generateFieldDisplayHandler("accountid", "Account Name");
		this.generateFieldDisplayHandler("expectedcloseddate",
				"Expected Close Date", HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("opportunitytype", "Type");
		this.generateFieldDisplayHandler("source", "Lead Source");
		this.generateFieldDisplayHandler("campaignid", "Campaign");
		this.generateFieldDisplayHandler("assignuser", AppContext
				.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD));
		this.generateFieldDisplayHandler("description", "Description");
	}
}
