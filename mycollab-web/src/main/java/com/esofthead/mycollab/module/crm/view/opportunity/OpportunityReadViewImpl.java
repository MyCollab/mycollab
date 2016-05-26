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
package com.esofthead.mycollab.module.crm.view.opportunity;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.SimpleContactOpportunityRel;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
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
import com.esofthead.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Date;
import java.util.GregorianCalendar;

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

        previewItemContainer.selectTab(CrmTypeConstants.DETAIL);

        String saleState = this.beanItem.getSalesstage();
        Date closeDate = this.beanItem.getExpectedcloseddate();
        if ((!"Closed Won".equals(saleState) && !"Closed Lost".equals(saleState))
                && closeDate != null && (closeDate.before(new GregorianCalendar().getTime()))) {
            previewLayout.addTitleStyleName(UIConstants.LABEL_OVERDUE);
        }
    }

    @Override
    protected String initFormTitle() {
        // check if there is converted lead associates with this account
        LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
        SimpleLead lead = leadService.findConvertedLeadOfOpportunity(beanItem.getId(), AppContext.getAccountId());
        if (lead != null) {
            return String.format("<h2>%s%s</h2>", beanItem.getOpportunityname(), AppContext
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

        CssLayout navigatorWrapper = previewItemContainer.getNavigatorWrapper();
        MVerticalLayout basicInfo = new MVerticalLayout().withFullWidth().withStyleName("basic-info");

        dateInfoComp = new DateInfoComp();
        basicInfo.addComponent(dateInfoComp);

        peopleInfoComp = new PeopleInfoComp();
        basicInfo.addComponent(peopleInfoComp);

        followersComp = new CrmFollowersComp<>(CrmTypeConstants.OPPORTUNITY, RolePermissionCollections.CRM_OPPORTUNITY);
        basicInfo.addComponent(followersComp);

        navigatorWrapper.addComponentAsFirst(basicInfo);

        previewItemContainer.addTab(previewContent, CrmTypeConstants.DETAIL,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT));
        previewItemContainer.addTab(associateContactList, CrmTypeConstants.CONTACT,
                AppContext.getMessage(ContactI18nEnum.LIST));
        previewItemContainer.addTab(associateLeadList, CrmTypeConstants.LEAD,
                AppContext.getMessage(LeadI18nEnum.LIST));
        previewItemContainer.addTab(associateActivityList, CrmTypeConstants.ACTIVITY,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY));
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

    protected void displayActivities() {
        ActivitySearchCriteria criteria = new ActivitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        criteria.setType(StringSearchField.and(CrmTypeConstants.OPPORTUNITY));
        criteria.setTypeid(new NumberSearchField(beanItem.getId()));
        associateActivityList.setSearchCriteria(criteria);
    }

    protected void displayContacts() {
        associateContactList.displayContacts(beanItem);
    }

    protected void displayLeads() {
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
}
