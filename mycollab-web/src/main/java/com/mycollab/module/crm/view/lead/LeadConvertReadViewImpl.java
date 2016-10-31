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
package com.mycollab.module.crm.view.lead;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.event.AccountEvent;
import com.mycollab.module.crm.event.ContactEvent;
import com.mycollab.module.crm.event.OpportunityEvent;
import com.mycollab.module.crm.i18n.*;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.module.crm.service.ContactService;
import com.mycollab.module.crm.service.OpportunityService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.*;
import com.mycollab.module.crm.view.activity.ActivityRelatedItemListComp;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.IRelatedListHandlers;
import com.mycollab.vaadin.web.ui.AdvancedPreviewBeanForm;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import org.vaadin.viritin.button.MButton;

import static com.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator.BACK_BTN_PRESENTED;
import static com.mycollab.module.crm.ui.components.CrmPreviewFormControlsGenerator.NAVIGATOR_BTN_PRESENTED;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@ViewComponent
public class LeadConvertReadViewImpl extends AbstractPreviewItemComp<SimpleLead> implements LeadConvertReadView {
    private static final long serialVersionUID = 1L;

    private LeadCampaignListComp associateCampaignList;
    private ActivityRelatedItemListComp associateActivityList;
    private CrmActivityComponent activityComponent;

    private PeopleInfoComp peopleInfoComp;
    private DateInfoComp dateInfoComp;
    private CrmFollowersComp<SimpleLead> compFollowers;

    public LeadConvertReadViewImpl() {
        super(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleLead> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected ComponentContainer createButtonControls() {
        CrmPreviewFormControlsGenerator<SimpleLead> controlsButton = new CrmPreviewFormControlsGenerator<>(previewForm);
        return controlsButton.createButtonControls(BACK_BTN_PRESENTED | NAVIGATOR_BTN_PRESENTED, RolePermissionCollections.CRM_LEAD);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
    }

    @Override
    public void previewItem(SimpleLead item) {
        this.beanItem = item;
        displayConvertLeadInfo(item);
        updateTitle(initFormTitle());
        onPreviewItem();
    }

    @Override
    protected void onPreviewItem() {
        activityComponent.loadActivities("" + beanItem.getId());
        displayActivities();
        displayCampaigns();

        dateInfoComp.displayEntryDateTime(beanItem);
        peopleInfoComp.displayEntryPeople(beanItem);
        compFollowers.displayFollowers(beanItem);

        tabSheet.selectTab(CrmTypeConstants.DETAIL);
    }

    @Override
    protected String initFormTitle() {
        return UserUIContext.getMessage(LeadI18nEnum.VIEW_CONVERTED_LEAD_TITLE);
    }

    @Override
    protected void initRelatedComponents() {
        associateCampaignList = new LeadCampaignListComp();

        activityComponent = new CrmActivityComponent(CrmTypeConstants.LEAD);
        associateActivityList = new ActivityRelatedItemListComp(false);

        dateInfoComp = new DateInfoComp();
        peopleInfoComp = new PeopleInfoComp();
        compFollowers = new CrmFollowersComp<>(CrmTypeConstants.LEAD, RolePermissionCollections.CRM_LEAD);
        addToSideBar(dateInfoComp, peopleInfoComp, compFollowers);

        tabSheet.addTab(previewLayout, CrmTypeConstants.DETAIL, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ABOUT),
                CrmAssetsManager.getAsset(CrmTypeConstants.DETAIL));
        tabSheet.addTab(associateCampaignList, CrmTypeConstants.CAMPAIGN, UserUIContext.getMessage(CampaignI18nEnum.LIST),
                CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN));
        tabSheet.addTab(associateActivityList, CrmTypeConstants.ACTIVITY, UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY),
                CrmAssetsManager.getAsset(CrmTypeConstants.ACTIVITY));
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DefaultDynaFormLayout(CrmTypeConstants.LEAD, LeadDefaultDynaFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleLead> initBeanFormFieldFactory() {
        return new LeadReadFormFieldFactory(previewForm);
    }

    private void displayCampaigns() {
        associateCampaignList.displayCampaigns(beanItem);
    }


    private void displayActivities() {
        ActivitySearchCriteria criteria = new ActivitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        criteria.setType(StringSearchField.and(CrmTypeConstants.LEAD));
        criteria.setTypeid(new NumberSearchField(beanItem.getId()));
        associateActivityList.setSearchCriteria(criteria);
    }

    @Override
    public SimpleLead getItem() {
        return beanItem;
    }

    @Override
    public HasPreviewFormHandlers<SimpleLead> getPreviewFormHandlers() {
        return previewForm;
    }

    @Override
    public IRelatedListHandlers<SimpleActivity> getRelatedActivityHandlers() {
        return associateActivityList;
    }

    @Override
    public IRelatedListHandlers<SimpleCampaign> getRelatedCampaignHandlers() {
        return associateCampaignList;
    }

    @Override
    public void displayConvertLeadInfo(final SimpleLead lead) {
        previewForm.removeAllComponents();

        GridFormLayoutHelper layoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);

        AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
        final SimpleAccount account = accountService.findAccountAssoWithConvertedLead(lead.getId(), MyCollabUI.getAccountId());
        if (account != null) {
            MButton accountLink = new MButton(account.getAccountname(),
                    clickEvent -> EventBusFactory.getInstance().post(new AccountEvent.GotoRead(this, account.getId())))
                    .withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT))
                    .withStyleName(WebUIConstants.BUTTON_LINK);
            layoutHelper.addComponent(accountLink, UserUIContext.getMessage(AccountI18nEnum.SINGLE), 0, 0);
        } else {
            layoutHelper.addComponent(new Label(""), UserUIContext.getMessage(AccountI18nEnum.SINGLE), 0, 0);
        }

        ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
        final SimpleContact contact = contactService.findContactAssoWithConvertedLead(lead.getId(), MyCollabUI.getAccountId());
        if (contact != null) {
            MButton contactLink = new MButton(contact.getContactName(),
                    clickEvent -> EventBusFactory.getInstance().post(new ContactEvent.GotoRead(this, contact.getId())))
                    .withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT)).withStyleName(WebUIConstants.BUTTON_LINK);
            layoutHelper.addComponent(contactLink, UserUIContext.getMessage(ContactI18nEnum.SINGLE), 0, 1);
        } else {
            layoutHelper.addComponent(new Label(""), UserUIContext.getMessage(ContactI18nEnum.SINGLE), 0, 1);
        }

        OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
        final SimpleOpportunity opportunity = opportunityService.findOpportunityAssoWithConvertedLead(lead.getId(),
                MyCollabUI.getAccountId());
        if (opportunity != null) {
            MButton opportunityLink = new MButton(opportunity.getOpportunityname(),
                    clickEvent -> EventBusFactory.getInstance().post(new OpportunityEvent.GotoRead(this, opportunity.getId())))
                    .withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY)).withStyleName(WebUIConstants.BUTTON_LINK);
            layoutHelper.addComponent(opportunityLink, UserUIContext.getMessage(OpportunityI18nEnum.SINGLE), 0, 2);
        } else {
            layoutHelper.addComponent(new Label(""), UserUIContext.getMessage(OpportunityI18nEnum.SINGLE), 0, 2);
        }

        previewForm.addComponent(layoutHelper.getLayout());
        this.addComponent(tabSheet);
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.LEAD;
    }
}
