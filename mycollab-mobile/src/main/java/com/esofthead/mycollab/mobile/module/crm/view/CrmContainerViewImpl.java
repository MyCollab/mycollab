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
package com.esofthead.mycollab.mobile.module.crm.view;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.activity.ActivityListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.campaign.CampaignListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.cases.CaseListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.opportunity.OpportunityListPresenter;
import com.esofthead.mycollab.mobile.ui.AbstractMobileTabPageView;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CaseI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */

@ViewComponent
public class CrmContainerViewImpl extends AbstractMobileTabPageView implements
		CrmContainerView {

	private static final long serialVersionUID = 5251742381187041492L;

	private AccountListPresenter accountPresenter;
	private ContactListPresenter contactPresenter;
	private CampaignListPresenter campaignPresenter;
	private ActivityListPresenter activityPresenter;
	private LeadListPresenter leadPresenter;
	private OpportunityListPresenter opportunityPresenter;
	private CaseListPresenter casePresenter;

	public CrmContainerViewImpl() {
		((MobileNavigationManager) UI.getCurrent().getContent())
				.setNavigationMenu(null);
		buildComponents();
	}

	private void buildComponents() {
		this.addTab(
				getAccountTab(),
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_ACCOUNT
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_ACCOUNTS_HEADER)
						+ "</div>");
		this.addTab(
				getContactTab(),
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_CONTACT
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_CONTACTS_HEADER)
						+ "</div>");

		this.addTab(
				getCampaignTab(),
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_CAMPAIGN
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_CAMPAIGNS_HEADER)
						+ "</div>");
		this.addTab(
				getLeadTab(),
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_LEAD
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_LEADS_HEADER)
						+ "</div>");
		this.addTab(
				getOpportunityTab(),
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_OPPORTUNITY
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_OPPORTUNTIES_HEADER)
						+ "</div>");
		this.addTab(
				getCaseTab(),
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_CASE
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_CASES_HEADER)
						+ "</div>");
		this.addTab(
				getActivityTab(),
				"<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
						+ IconConstants.CRM_ACTIVITY
						+ "\"></span><div class=\"screen-reader-text\">"
						+ AppContext
								.getMessage(CrmCommonI18nEnum.TOOLBAR_ACTIVITIES_HEADER)
						+ "</div>");

		this.addListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -2091640999903863902L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				Component currentComponent = getSelelectedTab().getComponent();
				if (currentComponent == getAccountTab()) {
					AccountSearchCriteria criteria = new AccountSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					accountPresenter.getView().getPagedBeanTable()
							.setSearchCriteria(criteria);
					AppContext.addFragment("crm/account/list", AppContext
							.getMessage(AccountI18nEnum.VIEW_LIST_TITLE));
				} else if (currentComponent == getContactTab()) {
					ContactSearchCriteria criteria = new ContactSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					contactPresenter.getView().getPagedBeanTable()
							.setSearchCriteria(criteria);
					AppContext.addFragment("crm/contact/list", AppContext
							.getMessage(ContactI18nEnum.VIEW_LIST_TITLE));
				} else if (currentComponent == getCampaignTab()) {
					CampaignSearchCriteria criteria = new CampaignSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					campaignPresenter.getView().getPagedBeanTable()
							.setSearchCriteria(criteria);
					AppContext.addFragment("crm/campaign/list", AppContext
							.getMessage(CampaignI18nEnum.VIEW_LIST_TITLE));
				} else if (currentComponent == getLeadTab()) {
					LeadSearchCriteria criteria = new LeadSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					leadPresenter.getView().getPagedBeanTable()
							.setSearchCriteria(criteria);
					AppContext.addFragment("crm/lead/list",
							AppContext.getMessage(LeadI18nEnum.VIEW_LIST_TITLE));
				} else if (currentComponent == getOpportunityTab()) {
					OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					opportunityPresenter.getView().getPagedBeanTable()
							.setSearchCriteria(criteria);
					AppContext.addFragment("crm/opportunity/list", AppContext
							.getMessage(OpportunityI18nEnum.VIEW_LIST_TITLE));
				} else if (currentComponent == getCaseTab()) {
					CaseSearchCriteria criteria = new CaseSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					casePresenter.getView().getPagedBeanTable()
							.setSearchCriteria(criteria);
					AppContext.addFragment("crm/cases/list",
							AppContext.getMessage(CaseI18nEnum.VIEW_LIST_TITLE));
				} else if (currentComponent == getActivityTab()) {
					ActivitySearchCriteria criteria = new ActivitySearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					activityPresenter.getView().getPagedBeanTable()
							.setSearchCriteria(criteria);
					AppContext.addFragment("crm/activity/list", AppContext
							.getMessage(ActivityI18nEnum.M_VIEW_LIST_TITLE));
				}
			}
		});

	}

	private Component getActivityTab() {
		activityPresenter = PresenterResolver
				.getPresenter(ActivityListPresenter.class);
		return activityPresenter.getView();
	}

	private Component getCaseTab() {
		casePresenter = PresenterResolver.getPresenter(CaseListPresenter.class);
		return casePresenter.getView();
	}

	private Component getOpportunityTab() {
		opportunityPresenter = PresenterResolver
				.getPresenter(OpportunityListPresenter.class);
		return opportunityPresenter.getView();
	}

	private Component getLeadTab() {
		leadPresenter = PresenterResolver.getPresenter(LeadListPresenter.class);
		return leadPresenter.getView();
	}

	private Component getCampaignTab() {
		campaignPresenter = PresenterResolver
				.getPresenter(CampaignListPresenter.class);
		return campaignPresenter.getView();
	}

	private Component getContactTab() {
		contactPresenter = PresenterResolver
				.getPresenter(ContactListPresenter.class);
		return contactPresenter.getView();
	}

	private Component getAccountTab() {
		accountPresenter = PresenterResolver
				.getPresenter(AccountListPresenter.class);
		return accountPresenter.getView();
	}

	@Override
	public void goToAccounts() {
		this.setSelectedTab(getAccountTab());
	}

	@Override
	public void goToContacts() {
		this.setSelectedTab(getContactTab());
	}

	@Override
	public void goToCampaigns() {
		this.setSelectedTab(getCampaignTab());
	}

	@Override
	public void goToCases() {
		this.setSelectedTab(getCaseTab());
	}

	@Override
	public void goToLeads() {
		this.setSelectedTab(getLeadTab());
	}

	@Override
	public void goToActivities() {
		this.setSelectedTab(getActivityTab());
	}

	@Override
	public void goToOpportunities() {
		this.setSelectedTab(getOpportunityTab());
	}

}
