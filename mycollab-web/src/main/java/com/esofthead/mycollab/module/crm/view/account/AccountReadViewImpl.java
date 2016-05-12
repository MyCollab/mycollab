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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.*;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.*;
import com.esofthead.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;
import com.esofthead.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.DynaFormLayout;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class AccountReadViewImpl extends AbstractPreviewItemComp<SimpleAccount> implements AccountReadView {
    private static final long serialVersionUID = 1L;

    private AccountContactListComp associateContactList;
    private AccountOpportunityListComp associateOpportunityList;
    private AccountLeadListComp associateLeadList;
    private AccountCaseListComp associateCaseList;
    private ActivityRelatedItemListComp associateActivityList;
    private CrmActivityComponent activityComponent;

    private DateInfoComp dateInfoComp;
    private PeopleInfoComp peopleInfoComp;
    private CrmFollowersComp<SimpleAccount> compFollowers;

    public AccountReadViewImpl() {
        super(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleAccount> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(previewForm).createButtonControls(RolePermissionCollections.CRM_ACCOUNT);
    }

    private void displayActivities() {
        ActivitySearchCriteria criteria = new ActivitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        criteria.setType(StringSearchField.and(CrmTypeConstants.ACCOUNT));
        criteria.setTypeid(new NumberSearchField(beanItem.getId()));
        associateActivityList.setSearchCriteria(criteria);
    }

    private void displayAssociateCaseList() {
        associateCaseList.displayCases(beanItem);
    }

    private void displayAssociateLeadList() {
        associateLeadList.displayLeads(beanItem);
    }

    private void displayAssociateOpportunityList() {
        associateOpportunityList.displayOpportunities(beanItem);
    }


    @Override
    public AdvancedPreviewBeanForm<SimpleAccount> getPreviewForm() {
        return previewForm;
    }

    @Override
    protected String initFormTitle() {
        LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
        SimpleLead lead = leadService.findConvertedLeadOfAccount(beanItem.getId(), AppContext.getAccountId());
        if (lead != null) {
            return beanItem.getAccountname() + AppContext.getMessage(
                    LeadI18nEnum.CONVERT_FROM_LEAD_TITLE, CrmAssetsManager.getAsset(CrmTypeConstants.LEAD).getHtml(),
                    CrmLinkGenerator.generateCrmItemLink(CrmTypeConstants.LEAD, lead.getId()), lead.getLeadName());
        } else {
            return beanItem.getAccountname();
        }
    }

    @Override
    protected final void initRelatedComponents() {
        associateContactList = new AccountContactListComp();
        associateActivityList = new ActivityRelatedItemListComp(true);
        associateOpportunityList = new AccountOpportunityListComp();
        associateLeadList = new AccountLeadListComp();
        associateCaseList = new AccountCaseListComp();
        activityComponent = new CrmActivityComponent(CrmTypeConstants.ACCOUNT);

        CssLayout navigatorWrapper = previewItemContainer.getNavigatorWrapper();
        MVerticalLayout basicInfo = new MVerticalLayout().withWidth("100%").withStyleName("basic-info");

        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        compFollowers = new CrmFollowersComp<>(CrmTypeConstants.ACCOUNT, RolePermissionCollections.CRM_ACCOUNT);

        basicInfo.with(dateInfoComp, peopleInfoComp, compFollowers);
        navigatorWrapper.addComponentAsFirst(basicInfo);

        previewItemContainer.addTab(previewContent, CrmTypeConstants.DETAIL, AppContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT));
        previewItemContainer.addTab(associateContactList, CrmTypeConstants.CONTACT, AppContext.getMessage(ContactI18nEnum.LIST));
        previewItemContainer.addTab(associateLeadList, CrmTypeConstants.LEAD, AppContext.getMessage(LeadI18nEnum.LIST));
        previewItemContainer.addTab(associateOpportunityList, CrmTypeConstants.OPPORTUNITY,
                AppContext.getMessage(OpportunityI18nEnum.LIST.LIST));
        previewItemContainer.addTab(associateCaseList, CrmTypeConstants.CASE, AppContext.getMessage(CaseI18nEnum.LIST));
        previewItemContainer.addTab(associateActivityList, CrmTypeConstants.ACTIVITY, AppContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY));
        previewItemContainer.selectTab(CrmTypeConstants.DETAIL);
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
    protected void onPreviewItem() {
        activityComponent.loadActivities("" + beanItem.getId());
        displayActivities();
        associateContactList.displayContacts(beanItem);
        displayAssociateCaseList();
        displayAssociateOpportunityList();
        displayAssociateLeadList();

        peopleInfoComp.displayEntryPeople(beanItem);
        dateInfoComp.displayEntryDateTime(beanItem);
        compFollowers.displayFollowers(beanItem);

        previewItemContainer.selectTab(CrmTypeConstants.DETAIL);
    }

    @Override
    public SimpleAccount getItem() {
        return beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleAccount> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    public IRelatedListHandlers<SimpleContact> getRelatedContactHandlers() {
        return associateContactList;
    }

    @Override
    public IRelatedListHandlers<SimpleOpportunity> getRelatedOpportunityHandlers() {
        return associateOpportunityList;
    }

    @Override
    public IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers() {
        return associateLeadList;
    }

    @Override
    public IRelatedListHandlers<SimpleCase> getRelatedCaseHandlers() {
        return associateCaseList;
    }

    @Override
    public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
        return associateActivityList;
    }
}
