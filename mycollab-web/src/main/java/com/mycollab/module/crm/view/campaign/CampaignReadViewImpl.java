package com.mycollab.module.crm.view.campaign;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.*;
import com.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.mycollab.security.RolePermissionCollections;
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

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class CampaignReadViewImpl extends AbstractPreviewItemComp<SimpleCampaign> implements CampaignReadView {
    private static final long serialVersionUID = 1L;

    private CampaignAccountListComp associateAccountList;
    private CampaignContactListComp associateContactList;
    private CampaignLeadListComp associateLeadList;
    private ActivityRelatedItemListComp associateActivityList;
    private CrmActivityComponent activityComponent;

    private PeopleInfoComp peopleInfoComp;
    private DateInfoComp dateInfoComp;
    private CrmFollowersComp<SimpleCampaign> compFollowers;

    public CampaignReadViewImpl() {
        super(CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN));
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleCampaign> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(previewForm).createButtonControls(RolePermissionCollections.CRM_CAMPAIGN);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    protected void initRelatedComponents() {
        associateAccountList = new CampaignAccountListComp();
        associateContactList = new CampaignContactListComp();
        associateLeadList = new CampaignLeadListComp();
        associateActivityList = new ActivityRelatedItemListComp(true);
        activityComponent = new CrmActivityComponent(CrmTypeConstants.CAMPAIGN);

        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        compFollowers = new CrmFollowersComp<>(CrmTypeConstants.CAMPAIGN, RolePermissionCollections.CRM_CAMPAIGN);
        addToSideBar(dateInfoComp, peopleInfoComp, compFollowers);

        tabSheet.addTab(previewLayout, CrmTypeConstants.DETAIL, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT),
                CrmAssetsManager.getAsset(CrmTypeConstants.DETAIL));
        tabSheet.addTab(associateAccountList, CrmTypeConstants.ACCOUNT, UserUIContext.getMessage(AccountI18nEnum.LIST),
                CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
        tabSheet.addTab(associateContactList, CrmTypeConstants.CONTACT, UserUIContext.getMessage(ContactI18nEnum.LIST),
                CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
        tabSheet.addTab(associateLeadList, CrmTypeConstants.LEAD, UserUIContext.getMessage(LeadI18nEnum.LIST),
                CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
        tabSheet.addTab(associateActivityList, CrmTypeConstants.ACTIVITY, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY),
                CrmAssetsManager.getAsset(CrmTypeConstants.ACTIVITY));
    }

    private void displayActivities() {
        ActivitySearchCriteria criteria = new ActivitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        criteria.setType(StringSearchField.and(CrmTypeConstants.CAMPAIGN));
        criteria.setTypeid(new NumberSearchField(beanItem.getId()));
        associateActivityList.setSearchCriteria(criteria);
    }

    private void displayAccounts() {
        associateAccountList.displayAccounts(beanItem);
    }

    private void displayContacts() {
        associateContactList.displayContacts(beanItem);
    }

    private void displayLeads() {
        associateLeadList.displayLeads(beanItem);
    }

    @Override
    public AdvancedPreviewBeanForm<SimpleCampaign> getPreviewForm() {
        return previewForm;
    }

    @Override
    protected void onPreviewItem() {
        displayActivities();
        displayAccounts();
        displayContacts();
        displayLeads();
        activityComponent.loadActivities("" + beanItem.getId());

        dateInfoComp.displayEntryDateTime(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);
        compFollowers.displayFollowers(beanItem);

        tabSheet.selectTab(CrmTypeConstants.DETAIL);

        Date now = new GregorianCalendar().getTime();
        String status = beanItem.getStatus();
        if (!"Completed".equals(status) && (beanItem.getEnddate() != null && beanItem.getEnddate().before(now))) {
            previewLayout.addTitleStyleName(WebThemes.LABEL_OVERDUE);
        }
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getCampaignname();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(CrmTypeConstants.CAMPAIGN, CampaignDefaultDynaFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleCampaign> initBeanFormFieldFactory() {
        return new CampaignReadFormFieldFactory(previewForm);
    }

    @Override
    public SimpleCampaign getItem() {
        return beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleCampaign> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
        return associateActivityList;
    }

    @Override
    public IRelatedListHandlers<SimpleAccount> getRelatedAccountHandlers() {
        return associateAccountList;
    }

    @Override
    public IRelatedListHandlers<SimpleContact> getRelatedContactHandlers() {
        return associateContactList;
    }

    @Override
    public IRelatedListHandlers<SimpleLead> getRelatedLeadHandlers() {
        return associateLeadList;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.CAMPAIGN;
    }
}
