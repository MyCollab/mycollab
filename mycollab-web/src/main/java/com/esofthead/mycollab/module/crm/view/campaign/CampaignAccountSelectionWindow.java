package com.esofthead.mycollab.module.crm.view.campaign;

import java.util.Arrays;

import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.ui.components.RelatedItemSelectionWindow2;
import com.esofthead.mycollab.module.crm.view.account.AccountSimpleSearchPanel;
import com.esofthead.mycollab.module.crm.view.account.AccountTableDisplay;
import com.esofthead.mycollab.module.crm.view.account.AccountTableFieldDef;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Button;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignAccountSelectionWindow extends
RelatedItemSelectionWindow2<SimpleAccount, AccountSearchCriteria> {

	private static final long serialVersionUID = 1L;

	public CampaignAccountSelectionWindow(
			CampaignAccountListComp2 associateAccountList) {
		super("Select Accounts", associateAccountList);

		this.setWidth("900px");
	}

	@Override
	protected void initUI() {
		tableItem = new AccountTableDisplay(AccountTableFieldDef.selected,
				Arrays.asList(AccountTableFieldDef.accountname,
						AccountTableFieldDef.phoneoffice,
						AccountTableFieldDef.email, AccountTableFieldDef.city));

		Button selectBtn = new Button("Select", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event) {
				close();
			}
		});
		selectBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

		AccountSimpleSearchPanel accountSimpleSearchPanel = new AccountSimpleSearchPanel();
		accountSimpleSearchPanel
		.addSearchHandler(new SearchHandler<AccountSearchCriteria>() {

			@Override
			public void onSearch(AccountSearchCriteria criteria) {
				tableItem.setSearchCriteria(criteria);
			}

		});

		this.bodyContent.addComponent(accountSimpleSearchPanel);
		this.bodyContent.addComponent(selectBtn);
		this.bodyContent.addComponent(tableItem);
	}
}
