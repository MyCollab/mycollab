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
package com.mycollab.mobile.module.crm.view;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.mobile.module.crm.view.account.AccountListPresenter;
import com.mycollab.mobile.module.crm.view.activity.ActivityListPresenter;
import com.mycollab.mobile.module.crm.view.campaign.CampaignListPresenter;
import com.mycollab.mobile.module.crm.view.cases.CaseListPresenter;
import com.mycollab.mobile.module.crm.view.contact.ContactListPresenter;
import com.mycollab.mobile.module.crm.view.lead.LeadListPresenter;
import com.mycollab.mobile.module.crm.view.opportunity.OpportunityListPresenter;
import com.mycollab.mobile.ui.AbstractMobileTabPageView;
import com.mycollab.mobile.ui.IconConstants;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.module.crm.domain.criteria.*;
import com.mycollab.module.crm.i18n.*;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
@ViewComponent
public class CrmContainerViewImpl extends AbstractMobileTabPageView implements CrmContainerView {
    private static final long serialVersionUID = 5251742381187041492L;

    private AccountListPresenter accountPresenter;
    private ContactListPresenter contactPresenter;
    private CampaignListPresenter campaignPresenter;
    private ActivityListPresenter activityPresenter;
    private LeadListPresenter leadPresenter;
    private OpportunityListPresenter opportunityPresenter;
    private CaseListPresenter casePresenter;

    public CrmContainerViewImpl() {
        buildComponents();
    }

    private void buildComponents() {
        this.addTab(getAccountTab(), "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\"" + IconConstants.CRM_ACCOUNT
                + "\"></span><div class=\"screen-reader-text\">"
                + AppContext.getMessage(AccountI18nEnum.LIST)
                + "</div>");
        this.addTab(getContactTab(), "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\"" + IconConstants.CRM_CONTACT
                + "\"></span><div class=\"screen-reader-text\">"
                + AppContext.getMessage(ContactI18nEnum.LIST)
                + "</div>");

        this.addTab(getCampaignTab(), "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_CAMPAIGN
                + "\"></span><div class=\"screen-reader-text\">"
                + AppContext.getMessage(CampaignI18nEnum.LIST)
                + "</div>");
        this.addTab(
                getLeadTab(),
                "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
                        + IconConstants.CRM_LEAD
                        + "\"></span><div class=\"screen-reader-text\">"
                        + AppContext
                        .getMessage(LeadI18nEnum.LIST)
                        + "</div>");
        this.addTab(
                getOpportunityTab(),
                "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
                        + IconConstants.CRM_OPPORTUNITY
                        + "\"></span><div class=\"screen-reader-text\">"
                        + AppContext
                        .getMessage(OpportunityI18nEnum.LIST)
                        + "</div>");
        this.addTab(
                getCaseTab(),
                "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
                        + IconConstants.CRM_CASE
                        + "\"></span><div class=\"screen-reader-text\">"
                        + AppContext
                        .getMessage(CaseI18nEnum.LIST)
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
                    criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
                    accountPresenter.getView().getPagedBeanTable().search(criteria);
                    AppContext.addFragment("crm/account/list", AppContext.getMessage(AccountI18nEnum.LIST));
                } else if (currentComponent == getContactTab()) {
                    ContactSearchCriteria criteria = new ContactSearchCriteria();
                    criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
                    contactPresenter.getView().getPagedBeanTable().search(criteria);
                    AppContext.addFragment("crm/contact/list", AppContext.getMessage(ContactI18nEnum.LIST));
                } else if (currentComponent == getCampaignTab()) {
                    CampaignSearchCriteria criteria = new CampaignSearchCriteria();
                    criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
                    campaignPresenter.getView().getPagedBeanTable().search(criteria);
                    AppContext.addFragment("crm/campaign/list", AppContext.getMessage(CampaignI18nEnum.LIST));
                } else if (currentComponent == getLeadTab()) {
                    LeadSearchCriteria criteria = new LeadSearchCriteria();
                    criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
                    leadPresenter.getView().getPagedBeanTable().search(criteria);
                    AppContext.addFragment("crm/lead/list", AppContext.getMessage(LeadI18nEnum.LIST));
                } else if (currentComponent == getOpportunityTab()) {
                    OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
                    criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
                    opportunityPresenter.getView().getPagedBeanTable().search(criteria);
                    AppContext.addFragment("crm/opportunity/list", AppContext.getMessage(OpportunityI18nEnum.LIST));
                } else if (currentComponent == getCaseTab()) {
                    CaseSearchCriteria criteria = new CaseSearchCriteria();
                    criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
                    casePresenter.getView().getPagedBeanTable().search(criteria);
                    AppContext.addFragment("crm/cases/list", AppContext.getMessage(CaseI18nEnum.LIST));
                } else if (currentComponent == getActivityTab()) {
                    ActivitySearchCriteria criteria = new ActivitySearchCriteria();
                    criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
                    activityPresenter.getView().getPagedBeanTable().search(criteria);
                    AppContext.addFragment("crm/activity/list", AppContext.getMessage(ActivityI18nEnum.M_VIEW_LIST_TITLE));
                }
            }
        });

    }

    private Component getActivityTab() {
        activityPresenter = PresenterResolver.getPresenter(ActivityListPresenter.class);
        return activityPresenter.getView();
    }

    private Component getCaseTab() {
        casePresenter = PresenterResolver.getPresenter(CaseListPresenter.class);
        return casePresenter.getView();
    }

    private Component getOpportunityTab() {
        opportunityPresenter = PresenterResolver.getPresenter(OpportunityListPresenter.class);
        return opportunityPresenter.getView();
    }

    private Component getLeadTab() {
        leadPresenter = PresenterResolver.getPresenter(LeadListPresenter.class);
        return leadPresenter.getView();
    }

    private Component getCampaignTab() {
        campaignPresenter = PresenterResolver.getPresenter(CampaignListPresenter.class);
        return campaignPresenter.getView();
    }

    private Component getContactTab() {
        contactPresenter = PresenterResolver.getPresenter(ContactListPresenter.class);
        return contactPresenter.getView();
    }

    private Component getAccountTab() {
        accountPresenter = PresenterResolver.getPresenter(AccountListPresenter.class);
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
