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
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.esofthead.mycollab.module.crm.view.lead.LeadSimpleSearchPanel;
import com.esofthead.mycollab.module.crm.view.lead.LeadTableDisplay;
import com.esofthead.mycollab.module.crm.view.lead.LeadTableFieldDef;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Button;

import java.util.Arrays;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class OpportunityLeadSelectionWindow extends RelatedItemSelectionWindow<SimpleLead, LeadSearchCriteria> {

	public OpportunityLeadSelectionWindow(OpportunityLeadListComp associateLeadList) {
		super("Select Leads", associateLeadList);
		this.setWidth("900px");
	}

	@Override
	protected void initUI() {
		tableItem = new LeadTableDisplay(LeadTableFieldDef.selected(),
				Arrays.asList(LeadTableFieldDef.name(), LeadTableFieldDef.status(),
						LeadTableFieldDef.email(), LeadTableFieldDef.phoneoffice()));

		Button selectBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SELECT), new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				close();
			}
		});
		selectBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

		LeadSimpleSearchPanel leadSimpleSearchPanel = new LeadSimpleSearchPanel();
		leadSimpleSearchPanel.addSearchHandler(new SearchHandler<LeadSearchCriteria>() {
			@Override
			public void onSearch(LeadSearchCriteria criteria) {
				tableItem.setSearchCriteria(criteria);
			}
		});

		this.bodyContent.with(leadSimpleSearchPanel, selectBtn, tableItem);
	}
}
