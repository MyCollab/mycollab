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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.*;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.*;
import com.esofthead.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.esofthead.mycollab.schedule.email.crm.AccountRelayEmailNotificationAction;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

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
    private AccountHistoryLogList historyLogList;
    private CrmCommentDisplay commentList;

    private DateInfoComp dateInfoComp;
    private PeopleInfoComp peopleInfoComp;
    private CrmFollowersComp<SimpleAccount> compFollowers;

    public AccountReadViewImpl() {
        super(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        TabSheetLazyLoadComponent tabTaskDetail = new TabSheetLazyLoadComponent();
        tabTaskDetail.addTab(commentList, AppContext.getMessage(GenericI18Enum.TAB_COMMENT, 0), FontAwesome.COMMENTS);
        tabTaskDetail.addTab(historyLogList, AppContext.getMessage(GenericI18Enum.TAB_HISTORY), FontAwesome.HISTORY);
        return tabTaskDetail;
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleAccount> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(previewForm).createButtonControls(RolePermissionCollections.CRM_ACCOUNT);
    }

    protected void displayActivities() {
        ActivitySearchCriteria criteria = new ActivitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        criteria.setType(new StringSearchField(SearchField.AND, CrmTypeConstants.ACCOUNT));
        criteria.setTypeid(new NumberSearchField(beanItem.getId()));
        associateActivityList.setSearchCriteria(criteria);
    }

    protected void displayAssociateCaseList() {
        associateCaseList.displayCases(beanItem);
    }

    protected void displayAssociateLeadList() {
        associateLeadList.displayLeads(beanItem);
    }

    protected void displayAssociateOpportunityList() {
        associateOpportunityList.displayOpportunities(beanItem);
    }


    @Override
    public AdvancedPreviewBeanForm<SimpleAccount> getPreviewForm() {
        return previewForm;
    }

    @Override
    protected String initFormTitle() {
        LeadService leadService = ApplicationContextUtil.getSpringBean(LeadService.class);
        SimpleLead lead = leadService.findConvertedLeadOfAccount(beanItem.getId(), AppContext.getAccountId());
        if (lead != null) {
            return new StringBuilder().append(beanItem.getAccountname()).append(AppContext.getMessage(
                    LeadI18nEnum.CONVERT_FROM_LEAD_TITLE, CrmAssetsManager.getAsset(CrmTypeConstants.LEAD).getHtml(),
                    CrmLinkGenerator.generateCrmItemLink(CrmTypeConstants.LEAD, lead.getId()), lead.getLeadName())).toString();
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
        historyLogList = new AccountHistoryLogList();
        commentList = new CrmCommentDisplay(CrmTypeConstants.ACCOUNT, AccountRelayEmailNotificationAction.class);

        CssLayout navigatorWrapper = previewItemContainer.getNavigatorWrapper();
        MVerticalLayout basicInfo = new MVerticalLayout().withWidth("100%").withStyleName("basic-info");

        dateInfoComp = new DateInfoComp();
        basicInfo.addComponent(dateInfoComp);

        peopleInfoComp = new PeopleInfoComp();
        basicInfo.addComponent(peopleInfoComp);

        compFollowers = new CrmFollowersComp<>(CrmTypeConstants.ACCOUNT, RolePermissionCollections.CRM_ACCOUNT);
        basicInfo.addComponent(compFollowers);

        navigatorWrapper.addComponentAsFirst(basicInfo);

        previewItemContainer.addTab(previewContent, CrmTypeConstants.DETAIL,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT));
        previewItemContainer.addTab(associateContactList, CrmTypeConstants.CONTACT,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_CONTACT));
        previewItemContainer.addTab(associateLeadList, CrmTypeConstants.LEAD,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_LEAD));
        previewItemContainer.addTab(associateOpportunityList, CrmTypeConstants.OPPORTUNITY,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_OPPORTUNITY));
        previewItemContainer.addTab(associateCaseList, CrmTypeConstants.CASE,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_CASE));
        previewItemContainer.addTab(associateActivityList, CrmTypeConstants.ACTIVITY,
                AppContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY));

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
        commentList.loadComments("" + beanItem.getId());
        historyLogList.loadHistory(beanItem.getId());
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
