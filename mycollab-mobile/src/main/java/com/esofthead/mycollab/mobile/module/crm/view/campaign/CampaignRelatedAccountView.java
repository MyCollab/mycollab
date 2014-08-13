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
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.crm.events.CrmEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountListDisplay;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

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

		setCaption(AppContext
				.getMessage(AccountI18nEnum.M_TITLE_RELATED_ACCOUNTS));
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
		final Popover controlBtns = new Popover();
		controlBtns.setClosable(true);
		controlBtns.setStyleName("controls-popover");

		VerticalLayout addButtons = new VerticalLayout();
		addButtons.setSpacing(true);
		addButtons.setWidth("100%");
		addButtons.setMargin(true);
		addButtons.addStyleName("edit-btn-layout");

		NavigationButton newAccount = new NavigationButton();
		newAccount.setTargetViewCaption(AppContext
				.getMessage(AccountI18nEnum.VIEW_NEW_TITLE));
		newAccount
				.addClickListener(new NavigationButton.NavigationButtonClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(
							NavigationButton.NavigationButtonClickEvent arg0) {
						controlBtns.close();
						fireNewRelatedItem("");
					}
				});
		addButtons.addComponent(newAccount);

		NavigationButton selectAccount = new NavigationButton();
		selectAccount.setTargetViewCaption("Select Accounts");
		selectAccount
				.addClickListener(new NavigationButton.NavigationButtonClickListener() {

					private static final long serialVersionUID = 270503987054356318L;

					@Override
					public void buttonClick(
							NavigationButton.NavigationButtonClickEvent event) {
						controlBtns.close();
						CampaignAccountSelectionView accountSelectionView = new CampaignAccountSelectionView(
								CampaignRelatedAccountView.this);
						AccountSearchCriteria criteria = new AccountSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						accountSelectionView.setSearchCriteria(criteria);
						EventBusFactory.getInstance().post(
								new CrmEvent.PushView(
										CampaignRelatedAccountView.this,
										accountSelectionView));
					}
				});
		addButtons.addComponent(selectAccount);

		controlBtns.setContent(addButtons);

		final Button addAccount = new Button();
		addAccount.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(Button.ClickEvent event) {
				if (!controlBtns.isAttached())
					controlBtns.showRelativeTo(addAccount);
				else
					controlBtns.close();

			}
		});
		addAccount.setStyleName("add-btn");

		return addAccount;
	}

}
