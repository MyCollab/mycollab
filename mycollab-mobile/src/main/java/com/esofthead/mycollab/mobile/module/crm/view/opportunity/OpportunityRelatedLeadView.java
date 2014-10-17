/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view.opportunity;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadListDisplay;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.ui.AbstractRelatedListView;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class OpportunityRelatedLeadView extends
		AbstractRelatedListView<SimpleLead, LeadSearchCriteria> {
	private static final long serialVersionUID = -7499398284109642141L;
	private SimpleOpportunity opportunity;

	public OpportunityRelatedLeadView() {
		super();

		setCaption(AppContext.getMessage(LeadI18nEnum.M_TITLE_RELATED_LEADS));
		this.itemList = new LeadListDisplay();
		this.setContent(this.itemList);
	}

	private void loadLeads() {
		final LeadSearchCriteria searchCriteria = new LeadSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		searchCriteria.setOpportunityId(new NumberSearchField(SearchField.AND,
				opportunity.getId()));
		this.itemList.setSearchCriteria(searchCriteria);
	}

	public void displayLeads(SimpleOpportunity opportunity) {
		this.opportunity = opportunity;
		loadLeads();
	}

	@Override
	public void refresh() {
		loadLeads();
	}

	@Override
	protected Component createRightComponent() {
		NavigationBarQuickMenu addLead = new NavigationBarQuickMenu();
		addLead.setStyleName("add-btn");

		VerticalLayout addBtns = new VerticalLayout();
		addBtns.setStyleName("edit-btn-layout");
		addBtns.setSpacing(true);
		addBtns.setMargin(true);
		addBtns.setWidth("100%");

		Button newLead = new Button(
				AppContext.getMessage(LeadI18nEnum.VIEW_NEW_TITLE));
		newLead.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent arg0) {
				fireNewRelatedItem("");
			}
		});
		addBtns.addComponent(newLead);

		Button selectLead = new Button(
				AppContext.getMessage(LeadI18nEnum.M_TITLE_SELECT_LEADS));
		selectLead.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -1004824769733665112L;

			@Override
			public void buttonClick(Button.ClickEvent event) {
				final OpportunityLeadSelectionView leadSelectionView = new OpportunityLeadSelectionView(
						OpportunityRelatedLeadView.this);
				LeadSearchCriteria criteria = new LeadSearchCriteria();
				criteria.setSaccountid(new NumberSearchField(AppContext
						.getAccountId()));
				leadSelectionView.setSearchCriteria(criteria);
				EventBusFactory.getInstance().post(
						new ShellEvent.PushView(
								OpportunityRelatedLeadView.this,
								leadSelectionView));
			}
		});
		addBtns.addComponent(selectLead);

		addLead.setContent(addBtns);

		return addLead;
	}

}
