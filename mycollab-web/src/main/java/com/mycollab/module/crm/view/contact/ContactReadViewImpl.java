package com.mycollab.module.crm.view.contact;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
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
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class ContactReadViewImpl extends AbstractPreviewItemComp<SimpleContact> implements ContactReadView {
    private static final long serialVersionUID = 1L;

    private ContactOpportunityListComp associateOpportunityList;
    private ActivityRelatedItemListComp associateActivityList;
    private CrmActivityComponent activityComponent;

    private DateInfoComp dateInfoComp;
    private PeopleInfoComp peopleInfoComp;
    private CrmFollowersComp<SimpleContact> compFollowers;

    public ContactReadViewImpl() {
        super(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleContact> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(previewForm).createButtonControls(RolePermissionCollections.CRM_CONTACT);
    }

    @Override
    public AdvancedPreviewBeanForm<SimpleContact> getPreviewForm() {
        return this.previewForm;
    }

    private void displayActivities() {
        final ActivitySearchCriteria criteria = new ActivitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        criteria.setType(StringSearchField.and(CrmTypeConstants.CONTACT));
        criteria.setTypeid(new NumberSearchField(this.beanItem.getId()));
        this.associateActivityList.setSearchCriteria(criteria);
    }

    private void displayAssociateOpportunityList() {
        OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        criteria.setContactId(new NumberSearchField(beanItem.getId()));
        this.associateOpportunityList.displayOpportunities(beanItem);
    }

    @Override
    protected void onPreviewItem() {
        activityComponent.loadActivities("" + beanItem.getId());
        this.displayActivities();
        this.displayAssociateOpportunityList();

        peopleInfoComp.displayEntryPeople(beanItem);
        dateInfoComp.displayEntryDateTime(beanItem);
        compFollowers.displayFollowers(beanItem);

        tabSheet.selectTab(CrmTypeConstants.DETAIL);
    }

    @Override
    protected String initFormTitle() {
        // check if there is converted lead associates with this contact
        LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
        SimpleLead lead = leadService.findConvertedLeadOfContact(beanItem.getId(), AppUI.getAccountId());
        if (lead != null) {
            return beanItem.getContactName() + "&nbsp;" + UserUIContext.getMessage(
                    LeadI18nEnum.CONVERT_FROM_LEAD_TITLE,
                    CrmAssetsManager.getAsset(CrmTypeConstants.LEAD).getHtml(),
                    CrmLinkGenerator.generateCrmItemLink(CrmTypeConstants.LEAD, lead.getId()),
                    lead.getLeadName());
        } else {
            return beanItem.getContactName();
        }
    }

    @Override
    protected void initRelatedComponents() {
        this.associateOpportunityList = new ContactOpportunityListComp();
        this.associateActivityList = new ActivityRelatedItemListComp(true);
        activityComponent = new CrmActivityComponent(CrmTypeConstants.CONTACT);

        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        compFollowers = new CrmFollowersComp<>(CrmTypeConstants.CONTACT, RolePermissionCollections.CRM_CONTACT);
        addToSideBar(dateInfoComp, peopleInfoComp, compFollowers);

        tabSheet.addTab(previewLayout, CrmTypeConstants.DETAIL, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT),
                CrmAssetsManager.getAsset(CrmTypeConstants.DETAIL));
        tabSheet.addTab(associateOpportunityList, CrmTypeConstants.OPPORTUNITY, UserUIContext.getMessage(OpportunityI18nEnum.LIST),
                CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));
        tabSheet.addTab(associateActivityList, CrmTypeConstants.ACTIVITY, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY),
                CrmAssetsManager.getAsset(CrmTypeConstants.ACTIVITY));
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(CrmTypeConstants.CONTACT, ContactDefaultDynaFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleContact> initBeanFormFieldFactory() {
        return new ContactReadFormFieldFactory(previewForm);
    }

    @Override
    public SimpleContact getItem() {
        return beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleContact> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
        return associateActivityList;
    }

    @Override
    public IRelatedListHandlers<SimpleOpportunity> getRelatedOpportunityHandlers() {
        return associateOpportunityList;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.CONTACT;
    }
}
