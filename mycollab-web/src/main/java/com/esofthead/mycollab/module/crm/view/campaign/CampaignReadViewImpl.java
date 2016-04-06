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
package com.esofthead.mycollab.module.crm.view.campaign;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.*;
import com.esofthead.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;
import com.esofthead.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.DynaFormLayout;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

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
        return new CrmPreviewFormControlsGenerator<>(previewForm)
                .createButtonControls(RolePermissionCollections.CRM_CAMPAIGN);
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

        MVerticalLayout basicInfo = new MVerticalLayout().withWidth("100%").withStyleName("basic-info");

        CssLayout navigatorWrapper = previewItemContainer.getNavigatorWrapper();

        dateInfoComp = new DateInfoComp();
        basicInfo.addComponent(dateInfoComp);

        peopleInfoComp = new PeopleInfoComp();
        basicInfo.addComponent(peopleInfoComp);

        compFollowers = new CrmFollowersComp<>(CrmTypeConstants.CAMPAIGN, RolePermissionCollections.CRM_CAMPAIGN);
        basicInfo.addComponent(compFollowers);

        navigatorWrapper.addComponentAsFirst(basicInfo);

        previewItemContainer.addTab(previewContent, CrmTypeConstants.DETAIL,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT));
        previewItemContainer.addTab(associateAccountList, CrmTypeConstants.ACCOUNT,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_ACCOUNT));
        previewItemContainer.addTab(associateContactList, CrmTypeConstants.CONTACT,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_CONTACT));
        previewItemContainer.addTab(associateLeadList, CrmTypeConstants.LEAD,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_LEAD));
        previewItemContainer.addTab(associateActivityList, CrmTypeConstants.ACTIVITY,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY));
    }

    protected void displayActivities() {
        ActivitySearchCriteria criteria = new ActivitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        criteria.setType(StringSearchField.and(CrmTypeConstants.CAMPAIGN));
        criteria.setTypeid(new NumberSearchField(beanItem.getId()));
        associateActivityList.setSearchCriteria(criteria);
    }

    protected void displayAccounts() {
        associateAccountList.displayAccounts(beanItem);
    }

    protected void displayContacts() {
        associateContactList.displayContacts(beanItem);
    }

    protected void displayLeads() {
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

        previewItemContainer.selectTab(CrmTypeConstants.DETAIL);

        Date now = new GregorianCalendar().getTime();
        String status = this.beanItem.getStatus();
        if (!"Completed".equals(status) && (this.beanItem.getEnddate() != null && this.beanItem.getEnddate().before(now))) {
            previewLayout.addTitleStyleName(UIConstants.LABEL_OVERDUE);
        }
    }

    @Override
    protected String initFormTitle() {
        return beanItem.getCampaignname();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.CAMPAIGN, CampaignDefaultDynaFormLayoutFactory.getForm());
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
}
