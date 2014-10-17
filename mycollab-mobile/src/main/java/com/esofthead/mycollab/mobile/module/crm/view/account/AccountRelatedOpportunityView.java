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
import com.esofthead.mycollab.mobile.module.crm.view.opportunity.OpportunityListDisplay;
import com.esofthead.mycollab.mobile.ui.AbstractRelatedListView;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class AccountRelatedOpportunityView extends
		AbstractRelatedListView<SimpleOpportunity, OpportunitySearchCriteria> {
	private static final long serialVersionUID = -5900127054425653263L;
	private Account account;

	public AccountRelatedOpportunityView() {
		initUI();
	}

	private void initUI() {
		this.setCaption(AppContext
				.getMessage(OpportunityI18nEnum.M_TITLE_RELATED_OPPORTUNITIES));
		itemList = new OpportunityListDisplay();
		this.setContent(itemList);
	}

	public void displayOpportunities(final Account account) {
		this.account = account;
		loadOpportunities();
	}

	private void loadOpportunities() {
		final OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setAccountId(new NumberSearchField(SearchField.AND, account
				.getId()));
		setSearchCriteria(criteria);
	}

	@Override
	public void refresh() {
		loadOpportunities();
	}

	@Override
	protected Component createRightComponent() {
		Button addOpportunity = new Button();
		addOpportunity.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7172838996944732255L;

			@Override
			public void buttonClick(Button.ClickEvent event) {
				fireNewRelatedItem("");
			}
		});
		addOpportunity.setStyleName("add-btn");
		return addOpportunity;
	}

}
