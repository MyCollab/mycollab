/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.lead;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
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
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class LeadReadViewImpl extends AbstractPreviewItemComp<SimpleLead> implements LeadReadView {
    private static final long serialVersionUID = 1L;

    private LeadCampaignListComp associateCampaignList;
    private ActivityRelatedItemListComp associateActivityList;
    private CrmActivityComponent activityComponent;

    private PeopleInfoComp peopleInfoComp;
    private DateInfoComp dateInfoComp;
    private CrmFollowersComp<SimpleLead> compFollowers;

    public LeadReadViewImpl() {
        super(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleLead> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected ComponentContainer createButtonControls() {
        CrmPreviewFormControlsGenerator<SimpleLead> controlsButton = new CrmPreviewFormControlsGenerator<>(
                previewForm);

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_LEAD)) {
            MButton convertButton = new MButton(UserUIContext.getMessage(LeadI18nEnum.BUTTON_CONVERT_LEAD),
                    clickEvent -> previewForm.fireExtraAction("convert", beanItem))
                    .withIcon(FontAwesome.FLASK).withStyleName(WebThemes.BUTTON_ACTION);
            controlsButton.insertToControlBlock(convertButton);
        }

        return controlsButton.createButtonControls(RolePermissionCollections.CRM_LEAD);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        return activityComponent;
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
        return beanItem.getLeadName();
    }

    @Override
    protected void initRelatedComponents() {
        activityComponent = new CrmActivityComponent(CrmTypeConstants.LEAD);

        associateCampaignList = new LeadCampaignListComp();
        associateActivityList = new ActivityRelatedItemListComp(true);

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
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
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
    protected String getType() {
        return CrmTypeConstants.LEAD;
    }
}
