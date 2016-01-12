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
package com.esofthead.mycollab.module.crm.view.contact;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.*;
import com.esofthead.mycollab.module.crm.ui.format.ContactFieldFormatter;
import com.esofthead.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.esofthead.mycollab.schedule.email.crm.ContactRelayEmailNotificationAction;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
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

    protected void displayActivities() {
        final ActivitySearchCriteria criteria = new ActivitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        criteria.setType(new StringSearchField(SearchField.AND, CrmTypeConstants.CONTACT));
        criteria.setTypeid(new NumberSearchField(this.beanItem.getId()));
        this.associateActivityList.setSearchCriteria(criteria);
    }

    protected void displayAssociateOpportunityList() {
        OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
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

        previewItemContainer.selectTab(CrmTypeConstants.DETAIL);
    }

    @Override
    protected String initFormTitle() {
        // check if there is converted lead associates with this contact
        LeadService leadService = ApplicationContextUtil.getSpringBean(LeadService.class);
        SimpleLead lead = leadService.findConvertedLeadOfContact(beanItem.getId(), AppContext.getAccountId());
        if (lead != null) {
            return beanItem.getContactName() + "&nbsp;" + AppContext.getMessage(
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
        activityComponent = new CrmActivityComponent(CrmTypeConstants.CONTACT, ContactFieldFormatter.instance(),
                ContactRelayEmailNotificationAction.class);

        CssLayout navigatorWrapper = previewItemContainer.getNavigatorWrapper();
        MVerticalLayout basicInfo = new MVerticalLayout().withWidth("100%").withStyleName("basic-info");

        dateInfoComp = new DateInfoComp();
        basicInfo.addComponent(dateInfoComp);

        peopleInfoComp = new PeopleInfoComp();
        basicInfo.addComponent(peopleInfoComp);

        compFollowers = new CrmFollowersComp<>(CrmTypeConstants.CONTACT, RolePermissionCollections.CRM_CONTACT);
        basicInfo.addComponent(compFollowers);

        navigatorWrapper.addComponentAsFirst(basicInfo);

        previewItemContainer.addTab(previewContent, CrmTypeConstants.DETAIL,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT));
        previewItemContainer.addTab(associateOpportunityList, CrmTypeConstants.OPPORTUNITY,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_OPPORTUNITY));
        previewItemContainer.addTab(associateActivityList, CrmTypeConstants.ACTIVITY,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY));
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.CONTACT, ContactDefaultDynaFormLayoutFactory.getForm());
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
}
