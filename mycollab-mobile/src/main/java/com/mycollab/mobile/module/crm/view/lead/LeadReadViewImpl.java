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
package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.form.view.DynaFormLayout;
import com.mycollab.mobile.module.crm.event.LeadEvent;
import com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.mycollab.mobile.module.crm.view.activity.RelatedActivityNavigatorButton;
import com.mycollab.mobile.module.crm.view.campaign.RelatedCampaignNavigatorButton;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.touchkit.NavigationBarQuickMenu;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import static com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator.CLONE_BTN_PRESENTED;
import static com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator.DELETE_BTN_PRESENTED;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@ViewComponent
public class LeadReadViewImpl extends AbstractPreviewItemComp<SimpleLead> implements LeadReadView {
    private static final long serialVersionUID = 5288751461504873888L;

    private RelatedActivityNavigatorButton associateActivities;
    private RelatedCampaignNavigatorButton associateCampaigns;

    @Override
    public HasPreviewFormHandlers<SimpleLead> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected void initRelatedComponents() {
        associateCampaigns = new RelatedCampaignNavigatorButton();
        associateActivities = new RelatedActivityNavigatorButton();
    }

    @Override
    protected void afterPreviewItem() {
        associateCampaigns.displayRelatedByLead(beanItem.getId());
        associateActivities.displayRelatedByLead(beanItem.getId());
    }

    @Override
    protected String initFormHeader() {
        return beanItem.getLeadName();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleLead> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.LEAD, LeadDefaultDynaFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleLead> initBeanFormFieldFactory() {
        return new LeadReadFormFieldFactory(previewForm);
    }

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        AppUI.addFragment(CrmLinkGenerator.generateLeadPreviewLink(beanItem.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(LeadI18nEnum.SINGLE), beanItem.getLeadName()));
    }

    @Override
    protected ComponentContainer createButtonControls() {
        VerticalLayout buttonControls = new CrmPreviewFormControlsGenerator<>(previewForm).
                createButtonControls(CLONE_BTN_PRESENTED | DELETE_BTN_PRESENTED,
                        RolePermissionCollections.CRM_LEAD);
        MButton editBtn = new MButton("", clickEvent -> EventBusFactory.getInstance().post(new LeadEvent.GotoEdit(this, beanItem)))
                .withIcon(FontAwesome.EDIT).withStyleName(UIConstants.CIRCLE_BOX)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_LEAD));
        return new MHorizontalLayout(editBtn, new NavigationBarQuickMenu(buttonControls));
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        MVerticalLayout toolbarLayout = new MVerticalLayout().withFullWidth().withSpacing(false).withMargin(false);

        Component campaignSection = FormSectionBuilder.build(CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN),
                associateCampaigns);
        toolbarLayout.addComponent(campaignSection);

        Component activitySection = FormSectionBuilder.build(CrmAssetsManager.getAsset(CrmTypeConstants.ACTIVITY),
                associateActivities);
        toolbarLayout.addComponent(activitySection);

        return toolbarLayout;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.LEAD;
    }
}
