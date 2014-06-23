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
import com.esofthead.mycollab.mobile.ui.AbstractTabPageView;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.IModule;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.2
 * 
 */

@ViewComponent
public class CrmModule extends AbstractTabPageView implements IModule {
	private static final long serialVersionUID = 1741055981807436733L;

	private AccountListPresenter accountPresenter;
	private ContactListPresenter contactPresenter;
	private CampaignListPresenter campaignPresenter;
	private ActivityListPresenter activityPresenter;
	private LeadListPresenter leadPresenter;
	private OpportunityListPresenter opportunityPresenter;
	private CaseListPresenter casePresenter;

	public CrmModule() {
		ControllerRegistry.addController(new CrmModuleController(
				(NavigationManager) UI.getCurrent().getContent()));
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
		// this.addTab(
		// getCaseTab(),
		// "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
		// + IconConstants.CRM_CASE
		// + "\"></span><div class=\"screen-reader-text\">"
		// + AppContext
		// .getMessage(CrmCommonI18nEnum.TOOLBAR_CASES_HEADER)
		// + "</div>");
		// this.addTab(
		// getActivityTab(),
		// "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
		// + IconConstants.CRM_ACTIVITY
		// + "\"></span><div class=\"screen-reader-text\">"
		// + AppContext
		// .getMessage(CrmCommonI18nEnum.TOOLBAR_ACTIVITIES_HEADER)
		// + "</div>");

		this.addListener(new SelectedTabChangeListener() {
			private static final long serialVersionUID = -2091640999903863902L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				Component currentComponent = getSelelectedTab().getComponent();
				if (currentComponent == getAccountTab()) {
					AccountSearchCriteria criteria = new AccountSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					accountPresenter.go(CrmModule.this,
							new ScreenData.Search<AccountSearchCriteria>(
									criteria));
				} else if (currentComponent == getContactTab()) {
					ContactSearchCriteria criteria = new ContactSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					contactPresenter.go(CrmModule.this,
							new ScreenData.Search<ContactSearchCriteria>(
									criteria));
				} else if (currentComponent == getCampaignTab()) {
					CampaignSearchCriteria criteria = new CampaignSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					campaignPresenter.go(CrmModule.this,
							new ScreenData.Search<CampaignSearchCriteria>(
									criteria));
				} else if (currentComponent == getLeadTab()) {
					LeadSearchCriteria criteria = new LeadSearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					leadPresenter
							.go(CrmModule.this,
									new ScreenData.Search<LeadSearchCriteria>(
											criteria));
				} else if (currentComponent == getOpportunityTab()) {
					OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
					criteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
					opportunityPresenter.go(CrmModule.this,
							new ScreenData.Search<OpportunitySearchCriteria>(
									criteria));
				}
			}
		});

	}

	private Component getActivityTab() {
		activityPresenter = PresenterResolver
				.getPresenter(ActivityListPresenter.class);
		return activityPresenter.initView();
	}

	private Component getCaseTab() {
		casePresenter = PresenterResolver.getPresenter(CaseListPresenter.class);
		return casePresenter.initView();
	}

	private Component getOpportunityTab() {
		opportunityPresenter = PresenterResolver
				.getPresenter(OpportunityListPresenter.class);
		return opportunityPresenter.initView();
	}

	private Component getLeadTab() {
		leadPresenter = PresenterResolver.getPresenter(LeadListPresenter.class);
		return leadPresenter.initView();
	}

	private Component getCampaignTab() {
		campaignPresenter = PresenterResolver
				.getPresenter(CampaignListPresenter.class);
		return campaignPresenter.initView();
	}

	private Component getContactTab() {
		contactPresenter = PresenterResolver
				.getPresenter(ContactListPresenter.class);
		return contactPresenter.initView();
	}

	private Component getAccountTab() {
		accountPresenter = PresenterResolver
				.getPresenter(AccountListPresenter.class);
		return accountPresenter.initView();
	}
}
