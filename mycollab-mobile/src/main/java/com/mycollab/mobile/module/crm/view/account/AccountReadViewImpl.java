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
import com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.mycollab.mobile.module.crm.view.activity.ActivityRelatedItemView;
import com.mycollab.mobile.module.crm.view.cases.RelatedCaseNavigatorButton;
import com.mycollab.mobile.module.crm.view.contact.RelatedContactNavigatorButton;
import com.mycollab.mobile.module.crm.view.lead.RelatedLeadNavigatorButton;
import com.mycollab.mobile.module.crm.view.opportunity.RelatedOpportunityNavigationButton;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import static com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator.CLONE_BTN_PRESENTED;
import static com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator.DELETE_BTN_PRESENTED;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class AccountReadViewImpl extends AbstractPreviewItemComp<SimpleAccount> implements AccountReadView {
    private static final long serialVersionUID = -5987636662071328512L;

    private RelatedContactNavigatorButton associateContacts;
    private RelatedCaseNavigatorButton associateCases;
    private ActivityRelatedItemView associateActivities;
    private RelatedLeadNavigatorButton associateLeads;
    private RelatedOpportunityNavigationButton associateOpportunities;

    @Override
    protected void afterPreviewItem() {
        associateContacts.displayRelatedByAccount(beanItem.getId());
        associateCases.displayRelatedByAccount(beanItem.getId());
        associateActivities.displayActivity(beanItem.getId());
        associateLeads.displayRelatedByAccount(beanItem.getId());
        associateOpportunities.displayRelatedByAccount(beanItem.getId());
    }

    @Override
    protected void initRelatedComponents() {
        associateContacts = new RelatedContactNavigatorButton();
        associateCases = new RelatedCaseNavigatorButton();
        associateActivities = new ActivityRelatedItemView(CrmTypeConstants.ACCOUNT);
        associateLeads = new RelatedLeadNavigatorButton();
        associateOpportunities = new RelatedOpportunityNavigationButton();
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
        VerticalLayout buttonControls = new CrmPreviewFormControlsGenerator<>(previewForm)
                .createButtonControls(CLONE_BTN_PRESENTED | DELETE_BTN_PRESENTED,
                        RolePermissionCollections.CRM_ACCOUNT);
        MButton editBtn = new MButton("", clickEvent -> EventBusFactory.getInstance().post(new AccountEvent.GotoEdit(this, beanItem)))
                .withIcon(FontAwesome.EDIT).withStyleName(UIConstants.CIRCLE_BOX)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_ACCOUNT));
        return new MHorizontalLayout(editBtn, new NavigationBarQuickMenu(buttonControls));
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        MVerticalLayout toolbarLayout = new MVerticalLayout().withFullWidth().withSpacing(false).withMargin(false);
        Component contactSection = FormSectionBuilder.build(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT),
                associateContacts);
        toolbarLayout.addComponent(contactSection);

        Component leadSection = FormSectionBuilder.build(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD),
                associateLeads);
        toolbarLayout.addComponent(leadSection);

        Component opportunitySection = FormSectionBuilder.build(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY),
                associateOpportunities);
        toolbarLayout.addComponent(opportunitySection);

        Component caseSection = FormSectionBuilder.build(CrmAssetsManager.getAsset(CrmTypeConstants.CASE),
                associateCases);
        toolbarLayout.addComponent(caseSection);
        return toolbarLayout;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.ACCOUNT;
    }
}
