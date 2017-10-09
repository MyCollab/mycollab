package com.mycollab.module.crm.view.opportunity;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.SimpleContactOpportunityRel;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
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
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class OpportunityReadViewImpl extends AbstractPreviewItemComp<SimpleOpportunity> implements OpportunityReadView {
    private static final long serialVersionUID = 1L;

    private OpportunityContactListComp associateContactList;
    private OpportunityLeadListComp associateLeadList;
    private ActivityRelatedItemListComp associateActivityList;
    private CrmActivityComponent activityComponent;

    private PeopleInfoComp peopleInfoComp;
    private DateInfoComp dateInfoComp;
    private CrmFollowersComp<SimpleOpportunity> followersComp;

    public OpportunityReadViewImpl() {
        super(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleOpportunity> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(previewForm).createButtonControls(RolePermissionCollections.CRM_OPPORTUNITY);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    protected void onPreviewItem() {
        activityComponent.loadActivities("" + beanItem.getId());

        displayActivities();
        displayContacts();
        displayLeads();

        dateInfoComp.displayEntryDateTime(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);
        followersComp.displayFollowers(beanItem);

        tabSheet.selectTab(CrmTypeConstants.DETAIL);

        if (beanItem.isOverdue()) {
            previewLayout.addTitleStyleName(WebThemes.LABEL_OVERDUE);
        }
    }

    @Override
    protected String initFormTitle() {
        // check if there is converted lead associates with this account
        LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
        SimpleLead lead = leadService.findConvertedLeadOfOpportunity(beanItem.getId(), AppUI.getAccountId());
        if (lead != null) {
            return String.format("<h2>%s%s</h2>", beanItem.getOpportunityname(), UserUIContext
                    .getMessage(LeadI18nEnum.CONVERT_FROM_LEAD_TITLE, CrmAssetsManager.getAsset(CrmTypeConstants.LEAD),
                            CrmLinkGenerator.generateCrmItemLink(CrmTypeConstants.LEAD, lead.getId()), lead.getLeadName()));
        } else {
            return beanItem.getOpportunityname();
        }
    }

    @Override
    protected void initRelatedComponents() {
        associateContactList = new OpportunityContactListComp();
        associateLeadList = new OpportunityLeadListComp();
        associateActivityList = new ActivityRelatedItemListComp(true);

        activityComponent = new CrmActivityComponent(CrmTypeConstants.OPPORTUNITY);

        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        followersComp = new CrmFollowersComp<>(CrmTypeConstants.OPPORTUNITY, RolePermissionCollections.CRM_OPPORTUNITY);
        addToSideBar(dateInfoComp, peopleInfoComp, followersComp);

        tabSheet.addTab(previewLayout, CrmTypeConstants.DETAIL, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT),
                CrmAssetsManager.getAsset(CrmTypeConstants.DETAIL));
        tabSheet.addTab(associateContactList, CrmTypeConstants.CONTACT, UserUIContext.getMessage(ContactI18nEnum.LIST),
                CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
        tabSheet.addTab(associateLeadList, CrmTypeConstants.LEAD, UserUIContext.getMessage(LeadI18nEnum.LIST),
                CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
        tabSheet.addTab(associateActivityList, CrmTypeConstants.ACTIVITY, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY),
                CrmAssetsManager.getAsset(CrmTypeConstants.ACTIVITY));
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(CrmTypeConstants.OPPORTUNITY, OpportunityDefaultDynaFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleOpportunity> initBeanFormFieldFactory() {
        return new OpportunityReadFormFieldFactory(previewForm);
    }

    public SimpleOpportunity getOpportunity() {
        return beanItem;
    }

    private void displayActivities() {
        ActivitySearchCriteria criteria = new ActivitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        criteria.setType(StringSearchField.and(CrmTypeConstants.OPPORTUNITY));
        criteria.setTypeid(new NumberSearchField(beanItem.getId()));
        associateActivityList.setSearchCriteria(criteria);
    }

    private void displayContacts() {
        associateContactList.displayContacts(beanItem);
    }

    private void displayLeads() {
        associateLeadList.displayLeads(beanItem);
    }

    @Override
    public SimpleOpportunity getItem() {
        return beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleOpportunity> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
        return associateActivityList;
    }

    @Override
    public IRelatedListHandlers<SimpleContactOpportunityRel> getRelatedContactHandlers() {
        return associateContactList;
    }

    @Override
    public IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers() {
        return associateLeadList;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.OPPORTUNITY;
    }
}
