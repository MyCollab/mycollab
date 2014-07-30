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
package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.CrmEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadListDisplay;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class AccountRelatedLeadView extends
		AbstractRelatedListView<SimpleLead, LeadSearchCriteria> {
	private static final long serialVersionUID = -6563776375301107391L;
	private Account account;

	public AccountRelatedLeadView() {
		initUI();
	}

	private void initUI() {
		this.setCaption("Related Leads");
		this.itemList = new LeadListDisplay();
		this.setContent(itemList);
	}

	public void displayLeads(final Account account) {
		this.account = account;
		loadLeads();
	}

	private void loadLeads() {
		final LeadSearchCriteria criteria = new LeadSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setAccountId(new NumberSearchField(SearchField.AND, account
				.getId()));
		setSearchCriteria(criteria);
	}

	@Override
	public void refresh() {
		loadLeads();
	}

	@Override
	protected Component createRightComponent() {
		final Popover controlBtns = new Popover();
		controlBtns.setClosable(true);
		controlBtns.setStyleName("controls-popover");

		VerticalLayout addButtons = new VerticalLayout();
		addButtons.setSpacing(true);
		addButtons.setWidth("100%");
		addButtons.setMargin(true);
		addButtons.addStyleName("edit-btn-layout");

		NavigationButton newLead = new NavigationButton();
		newLead.setTargetViewCaption(AppContext
				.getMessage(LeadI18nEnum.VIEW_NEW_TITLE));
		newLead.addClickListener(new NavigationButton.NavigationButtonClickListener() {

			private static final long serialVersionUID = 8228954365650824438L;

			@Override
			public void buttonClick(
					NavigationButton.NavigationButtonClickEvent arg0) {
				controlBtns.close();
				fireNewRelatedItem("");
			}
		});
		addButtons.addComponent(newLead);

		NavigationButton selectLead = new NavigationButton();
		selectLead.setTargetViewCaption("Select Leads");
		selectLead
				.addClickListener(new NavigationButton.NavigationButtonClickListener() {

					private static final long serialVersionUID = 9076596614526838523L;

					@Override
					public void buttonClick(
							NavigationButton.NavigationButtonClickEvent event) {
						controlBtns.close();
						AccountLeadSelectionView leadSelectionView = new AccountLeadSelectionView(
								AccountRelatedLeadView.this);
						final LeadSearchCriteria criteria = new LeadSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						leadSelectionView.setSearchCriteria(criteria);
						EventBusFactory.getInstance().post(
								new CrmEvent.PushView(
										AccountRelatedLeadView.this,
										leadSelectionView));

					}
				});
		addButtons.addComponent(selectLead);

		controlBtns.setContent(addButtons);

		final Button addLead = new Button();
		addLead.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 8646239746274492634L;

			@Override
			public void buttonClick(ClickEvent evt) {
				if (!controlBtns.isAttached())
					controlBtns.showRelativeTo(addLead);
				else
					controlBtns.close();
			}
		});
		addLead.setStyleName("add-btn");
		return addLead;
	}

}
