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
package com.mycollab.mobile.module.crm.view.account;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.form.view.DynaFormLayout;
import com.mycollab.mobile.module.crm.events.AccountEvent;
import com.mycollab.mobile.module.crm.ui.CrmAbstractPreviewItemComp;
import com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.mycollab.mobile.module.crm.ui.CrmRelatedItemsScreenData;
import com.mycollab.mobile.module.crm.view.activity.ActivityRelatedItemView;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.mobile.ui.IconConstants;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class AccountReadViewImpl extends CrmAbstractPreviewItemComp<SimpleAccount> implements AccountReadView {
    private static final long serialVersionUID = -5987636662071328512L;

    private AccountRelatedContactView associateContacts;
    private AccountRelatedCaseView associateCases;
    private ActivityRelatedItemView associateActivities;
    private AccountRelatedLeadView associateLeads;
    private AccountRelatedOpportunityView associateOpportunities;

    @Override
    protected void afterPreviewItem() {
        associateContacts.displayContacts(beanItem);
        associateCases.displayCases(beanItem);
        associateActivities.displayActivity(beanItem.getId());
        associateLeads.displayLeads(beanItem);
        associateOpportunities.displayOpportunities(beanItem);
    }

    @Override
    protected void initRelatedComponents() {
        associateContacts = new AccountRelatedContactView();
        associateCases = new AccountRelatedCaseView();
        associateActivities = new ActivityRelatedItemView(CrmTypeConstants.ACCOUNT);
        associateLeads = new AccountRelatedLeadView();
        associateOpportunities = new AccountRelatedOpportunityView();
    }

    @Override
    protected String initFormHeader() {
        return beanItem.getAccountname();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleAccount> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.ACCOUNT, AccountDefaultDynaFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleAccount> initBeanFormFieldFactory() {
        return new AccountReadFormFieldFactory(previewForm);
    }

    @Override
    public HasPreviewFormHandlers<SimpleAccount> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(previewForm).createButtonControls(RolePermissionCollections.CRM_ACCOUNT);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        HorizontalLayout toolbarLayout = new HorizontalLayout();
        toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        toolbarLayout.setSpacing(true);

        Button relatedContacts = new Button();
        relatedContacts.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_CONTACT
                + "\"></span><div class=\"screen-reader-text\">"
                + UserUIContext.getMessage(ContactI18nEnum.LIST)
                + "</div>");
        relatedContacts.setHtmlContentAllowed(true);
        relatedContacts.addClickListener(clickEvent -> EventBusFactory.getInstance().post(new AccountEvent.GoToRelatedItems(
                this, new CrmRelatedItemsScreenData(associateContacts))));
        toolbarLayout.addComponent(relatedContacts);

        Button relatedOpportunities = new Button();
        relatedOpportunities
                .setCaption("<span aria-hidden=\"true\" data-icon=\""
                        + IconConstants.CRM_OPPORTUNITY
                        + "\"></span><div class=\"screen-reader-text\">"
                        + UserUIContext.getMessage(OpportunityI18nEnum.LIST)
                        + "</div>");
        relatedOpportunities.setHtmlContentAllowed(true);
        relatedOpportunities.addClickListener(clickEvent -> EventBusFactory.getInstance().post(new AccountEvent.GoToRelatedItems(this,
                new CrmRelatedItemsScreenData(associateOpportunities))));
        toolbarLayout.addComponent(relatedOpportunities);

        Button relatedLeads = new Button();
        relatedLeads.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_LEAD
                + "\"></span><div class=\"screen-reader-text\">"
                + UserUIContext.getMessage(LeadI18nEnum.LIST) + "</div>");
        relatedLeads.setHtmlContentAllowed(true);
        relatedLeads.addClickListener(clickEvent -> EventBusFactory.getInstance().post(new AccountEvent.GoToRelatedItems(this,
                new CrmRelatedItemsScreenData(associateLeads))));
        toolbarLayout.addComponent(relatedLeads);

        Button relatedActivities = new Button();
        relatedActivities.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_ACTIVITY
                + "\"></span><div class=\"screen-reader-text\">"
                + UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY)
                + "</div>");
        relatedActivities.setHtmlContentAllowed(true);
        relatedActivities.addClickListener(clickEvent -> EventBusFactory.getInstance().post(new AccountEvent.GoToRelatedItems(this,
                new CrmRelatedItemsScreenData(associateActivities))));
        toolbarLayout.addComponent(relatedActivities);

        return toolbarLayout;
    }

    @Override
    public IRelatedListHandlers<SimpleContact> getRelatedContactHandlers() {
        return associateContacts;
    }

    @Override
    public IRelatedListHandlers<SimpleOpportunity> getRelatedOpportunityHandlers() {
        return associateOpportunities;
    }

    @Override
    public IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers() {
        return associateLeads;
    }

    @Override
    public IRelatedListHandlers<SimpleCase> getRelatedCaseHandlers() {
        return associateCases;
    }

    @Override
    public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
        return associateActivities;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.ACCOUNT;
    }
}
