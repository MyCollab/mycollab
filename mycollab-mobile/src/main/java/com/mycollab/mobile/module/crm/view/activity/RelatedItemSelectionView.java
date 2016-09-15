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
package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.module.crm.view.account.AccountSelectionView;
import com.mycollab.mobile.module.crm.view.campaign.CampaignSelectionView;
import com.mycollab.mobile.module.crm.view.cases.CaseSelectionView;
import com.mycollab.mobile.module.crm.view.contact.ContactSelectionView;
import com.mycollab.mobile.module.crm.view.lead.LeadSelectionView;
import com.mycollab.mobile.module.crm.view.opportunity.OpportunitySelectionView;
import com.mycollab.mobile.ui.AbstractMobileTabPageView;
import com.mycollab.mobile.ui.IconConstants;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.module.crm.i18n.*;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.3.1
 */
public class RelatedItemSelectionView extends AbstractMobileTabPageView {
    private static final long serialVersionUID = 2360108571912662272L;

    private AccountSelectionView accountList;
    private ContactSelectionView contactList;
    private CampaignSelectionView campaignList;
    private LeadSelectionView leadList;
    private OpportunitySelectionView opportunityList;
    private CaseSelectionView caseList;

    private FieldSelection selectionField;

    private Component previousView;

    public RelatedItemSelectionView(FieldSelection selectionField) {
        this.selectionField = selectionField;
        buildComponents();
    }

    @Override
    public void attach() {
        super.attach();
        Component parent = this.getParent();
        while (parent != null && !(parent instanceof NavigationManager)) {
            parent = parent.getParent();
        }
        if (parent != null) {
            this.previousView = ((NavigationManager) parent).getCurrentComponent();
        }

    }

    public void selectTab(String tabName) {
        if (tabName == null) {
            this.setSelectedTab(accountList);
            return;
        }
        switch (tabName) {
            case CrmTypeConstants.CAMPAIGN:
                this.setSelectedTab(campaignList);
                break;
            case CrmTypeConstants.CASE:
                this.setSelectedTab(caseList);
                break;
            case CrmTypeConstants.CONTACT:
                this.setSelectedTab(contactList);
                break;
            case CrmTypeConstants.LEAD:
                this.setSelectedTab(leadList);
                break;
            case CrmTypeConstants.OPPORTUNITY:
                this.setSelectedTab(opportunityList);
                break;
            default:
                this.setSelectedTab(accountList);
                break;
        }
    }

    private void buildComponents() {
        buildTabs();

        this.addTab(
                accountList,
                "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
                        + IconConstants.CRM_ACCOUNT
                        + "\"></span><div class=\"screen-reader-text\">"
                        + UserUIContext
                        .getMessage(AccountI18nEnum.LIST)
                        + "</div>");
        this.addTab(
                contactList,
                "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
                        + IconConstants.CRM_CONTACT
                        + "\"></span><div class=\"screen-reader-text\">"
                        + UserUIContext
                        .getMessage(ContactI18nEnum.LIST)
                        + "</div>");

        this.addTab(
                campaignList,
                "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
                        + IconConstants.CRM_CAMPAIGN
                        + "\"></span><div class=\"screen-reader-text\">"
                        + UserUIContext
                        .getMessage(CampaignI18nEnum.LIST)
                        + "</div>");
        this.addTab(
                leadList,
                "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
                        + IconConstants.CRM_LEAD
                        + "\"></span><div class=\"screen-reader-text\">"
                        + UserUIContext
                        .getMessage(LeadI18nEnum.LIST)
                        + "</div>");
        this.addTab(
                opportunityList,
                "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
                        + IconConstants.CRM_OPPORTUNITY
                        + "\"></span><div class=\"screen-reader-text\">"
                        + UserUIContext
                        .getMessage(OpportunityI18nEnum.LIST)
                        + "</div>");
        this.addTab(
                caseList,
                "<span class=\"nav-btn-icon\" aria-hidden=\"true\" data-icon=\""
                        + IconConstants.CRM_CASE
                        + "\"></span><div class=\"screen-reader-text\">"
                        + UserUIContext
                        .getMessage(CaseI18nEnum.LIST)
                        + "</div>");

        this.addListener(new SelectedTabChangeListener() {
            private static final long serialVersionUID = -2091640999903863902L;

            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                Component currentComponent = getSelelectedTab().getComponent();
                if (currentComponent == accountList) {
                    accountList.load();
                    accountList.setPreviousComponent(previousView);
                } else if (currentComponent == contactList) {
                    contactList.load();
                    contactList.setPreviousComponent(previousView);
                } else if (currentComponent == campaignList) {
                    campaignList.load();
                    campaignList.setPreviousComponent(previousView);
                } else if (currentComponent == leadList) {
                    leadList.load();
                    leadList.setPreviousComponent(previousView);
                } else if (currentComponent == opportunityList) {
                    opportunityList.load();
                    opportunityList.setPreviousComponent(previousView);
                } else if (currentComponent == caseList) {
                    caseList.load();
                    caseList.setPreviousComponent(previousView);
                }
            }
        });

    }

    private void buildTabs() {
        accountList = new AccountSelectionView();
        accountList.setSelectionField(selectionField);

        contactList = new ContactSelectionView();
        contactList.setSelectionField(selectionField);

        campaignList = new CampaignSelectionView();
        campaignList.setSelectionField(selectionField);

        caseList = new CaseSelectionView();
        caseList.setSelectionField(selectionField);

        leadList = new LeadSelectionView();
        leadList.setSelectionField(selectionField);

        opportunityList = new OpportunitySelectionView();
        opportunityList.setSelectionField(selectionField);
    }
}
