package com.mycollab.module.crm.view.account;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.*;
import com.mycollab.module.crm.service.LeadService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.*;
import com.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.vaadin.ui.ComponentContainer;

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
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
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
        SimpleLead lead = leadService.findConvertedLeadOfAccount(beanItem.getId(), AppUI.getAccountId());
        if (lead != null) {
            return beanItem.getAccountname() + UserUIContext.getMessage(
                    LeadI18nEnum.CONVERT_FROM_LEAD_TITLE, CrmAssetsManager.getAsset(CrmTypeConstants.LEAD).getHtml(),
                    CrmLinkGenerator.generateCrmItemLink(CrmTypeConstants.LEAD, lead.getId()), lead.getLeadName());
        } else {
            return beanItem.getAccountname();
        }
    }

    @Override
    protected void updateHeader(String title) {
        headerTitle.removeAllComponents();
        ELabel formTitle = ELabel.h2(title);
        headerTitle.with(CrmAssetsUtil.editableAccountLogoComp(beanItem, 32), formTitle).expand(formTitle);
    }

    @Override
    protected final void initRelatedComponents() {
        associateContactList = new AccountContactListComp();
        associateActivityList = new ActivityRelatedItemListComp(true);
        associateOpportunityList = new AccountOpportunityListComp();
        associateLeadList = new AccountLeadListComp();
        associateCaseList = new AccountCaseListComp();
        activityComponent = new CrmActivityComponent(CrmTypeConstants.ACCOUNT);

        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        compFollowers = new CrmFollowersComp<>(CrmTypeConstants.ACCOUNT, RolePermissionCollections.CRM_ACCOUNT);
        addToSideBar(dateInfoComp, peopleInfoComp, compFollowers);

        tabSheet.addTab(previewLayout, CrmTypeConstants.DETAIL, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT),
                CrmAssetsManager.getAsset(CrmTypeConstants.DETAIL));
        tabSheet.addTab(associateContactList, CrmTypeConstants.CONTACT, UserUIContext.getMessage(ContactI18nEnum.LIST),
                CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
        tabSheet.addTab(associateLeadList, CrmTypeConstants.LEAD, UserUIContext.getMessage(LeadI18nEnum.LIST),
                CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
        tabSheet.addTab(associateOpportunityList, CrmTypeConstants.OPPORTUNITY,
                UserUIContext.getMessage(OpportunityI18nEnum.LIST), CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));
        tabSheet.addTab(associateCaseList, CrmTypeConstants.CASE, UserUIContext.getMessage(CaseI18nEnum.LIST),
                CrmAssetsManager.getAsset(CrmTypeConstants.CASE));
        tabSheet.addTab(associateActivityList, CrmTypeConstants.ACTIVITY, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY),
                CrmAssetsManager.getAsset(CrmTypeConstants.ACTIVITY));
        tabSheet.selectTab(CrmTypeConstants.DETAIL);
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(CrmTypeConstants.ACCOUNT, AccountDefaultDynaFormLayoutFactory.getForm());
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

        tabSheet.selectTab(CrmTypeConstants.DETAIL);
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

    @Override
    protected String getType() {
        return CrmTypeConstants.ACCOUNT;
    }
}
