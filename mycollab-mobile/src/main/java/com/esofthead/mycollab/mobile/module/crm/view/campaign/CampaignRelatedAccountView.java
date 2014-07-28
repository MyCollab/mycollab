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
package com.esofthead.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountListDisplay;
import com.esofthead.mycollab.mobile.ui.MobileNavigationButton;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class CampaignRelatedAccountView extends
		AbstractRelatedListView<SimpleAccount, AccountSearchCriteria> {
	private static final long serialVersionUID = -6830593270870716952L;
	private SimpleCampaign campaign;

	public CampaignRelatedAccountView() {
		super();

		setCaption("Related Accounts");
		this.itemList = new AccountListDisplay();
		this.setContent(itemList);
	}

	public void displayAccounts(SimpleCampaign campaign) {
		this.campaign = campaign;
		loadAccounts();
	}

	private void loadAccounts() {
		AccountSearchCriteria searchCriteria = new AccountSearchCriteria();
		searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		searchCriteria.setCampaignId(new NumberSearchField(SearchField.AND,
				this.campaign.getId()));
		this.itemList.setSearchCriteria(searchCriteria);
	}

	@Override
	public void refresh() {
		loadAccounts();
	}

	@Override
	protected Component createRightComponent() {
		MobileNavigationButton addAccount = new MobileNavigationButton();
		addAccount.setTargetViewCaption(AppContext
				.getMessage(AccountI18nEnum.VIEW_NEW_TITLE));
		addAccount
				.addClickListener(new NavigationButton.NavigationButtonClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(
							NavigationButton.NavigationButtonClickEvent arg0) {
						fireNewRelatedItem("");
					}
				});
		addAccount.setStyleName("add-btn");
		return addAccount;
	}

}
