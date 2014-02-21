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
package com.esofthead.mycollab.module.crm.view.account;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.form.view.DynaFormLayout;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.localization.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.ui.components.AbstractPreviewItemComp;
import com.esofthead.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator;
import com.esofthead.mycollab.module.crm.ui.components.NoteListItems;
import com.esofthead.mycollab.module.crm.view.CrmResources;
import com.esofthead.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.TabsheetLazyLoadComp;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
class AccountReadComp extends AbstractPreviewItemComp<SimpleAccount> {
	private static final long serialVersionUID = 1L;

	protected AccountContactListComp associateContactList;
	protected AccountOpportunityListComp associateOpportunityList;
	protected AccountLeadListComp associateLeadList;
	protected AccountCaseListComp associateCaseList;
	protected ActivityRelatedItemListComp associateActivityList;
	protected NoteListItems noteListItems;

	public AccountReadComp() {
		super(MyCollabResource.newResource("icons/22/crm/account.png"));
	}

	@Override
	protected ComponentContainer createBottomPanel() {
		final TabsheetLazyLoadComp tabContainer = new TabsheetLazyLoadComp();
		tabContainer.setWidth("100%");

		tabContainer.addTab(noteListItems, "Notes",
				MyCollabResource.newResource("icons/16/crm/note.png"));
		tabContainer.addTab(associateContactList, "Contacts",
				MyCollabResource.newResource("icons/16/crm/contact.png"));
		tabContainer.addTab(associateOpportunityList, "Opportunities",
				MyCollabResource.newResource("icons/16/crm/opportunity.png"));
		tabContainer.addTab(associateLeadList, "Leads",
				MyCollabResource.newResource("icons/16/crm/lead.png"));
		tabContainer.addTab(associateCaseList, "Cases",
				MyCollabResource.newResource("icons/16/crm/case.png"));
		tabContainer.addTab(associateActivityList, "Activities",
				MyCollabResource.newResource("icons/16/crm/calendar.png"));
		return tabContainer;
	}

	@Override
	protected AdvancedPreviewBeanForm<SimpleAccount> initPreviewForm() {
		return new AdvancedPreviewBeanForm<SimpleAccount>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void showHistory() {
				final AccountHistoryLogWindow historyLog = new AccountHistoryLogWindow(
						ModuleNameConstants.CRM, CrmTypeConstants.ACCOUNT,
						beanItem.getId());
				historyLog.loadHistory(beanItem.getId());
				UI.getCurrent().addWindow(historyLog);
			}
		};
	}

	@Override
	protected ComponentContainer createButtonControls() {
		return new CrmPreviewFormControlsGenerator<SimpleAccount>(previewForm)
				.createButtonControls(RolePermissionCollections.CRM_ACCOUNT);

	}

	protected void displayActivities() {
		final ActivitySearchCriteria criteria = new ActivitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setType(new StringSearchField(SearchField.AND,
				CrmTypeConstants.ACCOUNT));
		criteria.setTypeid(new NumberSearchField(beanItem.getId()));
		associateActivityList.setSearchCriteria(criteria);
	}

	protected void displayAssociateCaseList() {
		final CaseSearchCriteria criteria = new CaseSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setAccountId(new NumberSearchField(beanItem.getId()));
		associateCaseList.setSearchCriteria(criteria);
	}

	protected void displayAssociateLeadList() {
		associateLeadList.displayLeads(beanItem);
	}

	protected void displayAssociateOpportunityList() {
		final OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setAccountId(new NumberSearchField(SearchField.AND, beanItem
				.getId()));
		associateOpportunityList.setSearchCriteria(criteria);
	}

	protected void displayNotes() {
		noteListItems.showNotes(CrmTypeConstants.ACCOUNT, beanItem.getId());
	}

	public ActivityRelatedItemListComp getAssociateActivityList() {
		return associateActivityList;
	}

	public AccountCaseListComp getAssociateCaseList() {
		return associateCaseList;
	}

	public AccountContactListComp getAssociateContactList() {
		return associateContactList;
	}

	public AccountLeadListComp getAssociateLeadList() {
		return associateLeadList;
	}

	public AccountOpportunityListComp getAssociateOpportunityList() {
		return associateOpportunityList;
	}

	public AdvancedPreviewBeanForm<SimpleAccount> getPreviewForm() {
		return previewForm;
	}

	@Override
	protected String initFormTitle() {
		// check if there is converted lead associates with this account
		LeadService leadService = ApplicationContextUtil
				.getSpringBean(LeadService.class);
		SimpleLead lead = leadService.findConvertedLeadOfAccount(
				beanItem.getId(), AppContext.getAccountId());
		if (lead != null) {
			return "<h2>"
					+ beanItem.getAccountname()
					+ LocalizationHelper
							.getMessage(
									LeadI18nEnum.CONVERT_FROM_LEAD_TITLE,
									CrmResources
											.getResourceLink(CrmTypeConstants.LEAD),
									CrmLinkGenerator.generateCrmItemLink(
											CrmTypeConstants.LEAD, lead.getId()),
									lead.getLeadName()) + "</h2>";
		} else {
			return "<h2>" + beanItem.getAccountname() + "</h2>";
		}
	}

	protected final void initRelatedComponents() {
		associateContactList = new AccountContactListComp();
		associateActivityList = new ActivityRelatedItemListComp(true);
		associateOpportunityList = new AccountOpportunityListComp();
		associateLeadList = new AccountLeadListComp();
		associateCaseList = new AccountCaseListComp();
		noteListItems = new NoteListItems("Notes");
	}

	@Override
	protected IFormLayoutFactory initFormLayoutFactory() {
		return new DynaFormLayout(CrmTypeConstants.ACCOUNT,
				AccountDefaultDynaFormLayoutFactory.getForm());
	}

	@Override
	protected AbstractBeanFieldGroupViewFieldFactory<SimpleAccount> initBeanFormFieldFactory() {
		return new AccountReadFormFieldFactory(previewForm);
	}

	@Override
	protected void onPreviewItem() {
		displayNotes();
		displayActivities();
		associateContactList.displayContacts(beanItem);
		displayAssociateCaseList();
		displayAssociateOpportunityList();
		displayAssociateLeadList();
	}

}
