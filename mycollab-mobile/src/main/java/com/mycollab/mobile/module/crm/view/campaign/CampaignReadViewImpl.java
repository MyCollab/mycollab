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
package com.mycollab.mobile.module.crm.view.campaign;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.mobile.form.view.DynaFormLayout;
import com.mycollab.mobile.module.crm.event.CampaignEvent;
import com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.mycollab.mobile.module.crm.view.account.RelatedAccountNavigatorButton;
import com.mycollab.mobile.module.crm.view.activity.RelatedActivityNavigatorButton;
import com.mycollab.mobile.module.crm.view.contact.RelatedContactNavigatorButton;
import com.mycollab.mobile.module.crm.view.lead.RelatedLeadNavigatorButton;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.mobile.ui.FormSectionBuilder;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
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
public class CampaignReadViewImpl extends AbstractPreviewItemComp<SimpleCampaign> implements CampaignReadView {
    private static final long serialVersionUID = 1718241963843463323L;

    private RelatedActivityNavigatorButton associateActivities;
    private RelatedAccountNavigatorButton associateAccounts;
    private RelatedContactNavigatorButton associateContacts;
    private RelatedLeadNavigatorButton associateLeads;

    @Override
    public HasPreviewFormHandlers<SimpleCampaign> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected void initRelatedComponents() {
        associateActivities = new RelatedActivityNavigatorButton();
        associateAccounts = new RelatedAccountNavigatorButton();
        associateContacts = new RelatedContactNavigatorButton();
        associateLeads = new RelatedLeadNavigatorButton();
    }

    @Override
    protected void afterPreviewItem() {
        associateActivities.displayRelatedByCampaign(beanItem.getId());
        associateAccounts.displayRelatedByCampaign(beanItem.getId());
        associateContacts.displayRelatedByCampaign(beanItem.getId());
        associateLeads.displayRelatedByCampaign(beanItem.getId());
    }

    @Override
    protected String initFormHeader() {
        return beanItem.getCampaignname();
    }

    @Override
    protected AdvancedPreviewBeanForm<SimpleCampaign> initPreviewForm() {
        return new AdvancedPreviewBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(CrmTypeConstants.CAMPAIGN, CampaignDefaultDynaFormLayoutFactory.getForm());
    }

    @Override
    protected AbstractBeanFieldGroupViewFieldFactory<SimpleCampaign> initBeanFormFieldFactory() {
        return new CampaignReadFormFieldFactory(this.previewForm);
    }

    @Override
    protected void onBecomingVisible() {
        super.onBecomingVisible();
        AppUI.addFragment(CrmLinkGenerator.generateCampaignPreviewLink(beanItem.getId()),
                UserUIContext.getMessage(GenericI18Enum.BROWSER_PREVIEW_ITEM_TITLE,
                        UserUIContext.getMessage(CampaignI18nEnum.SINGLE), beanItem.getCampaignname()));
    }

    @Override
    protected ComponentContainer createButtonControls() {
        VerticalLayout buttonControls = new CrmPreviewFormControlsGenerator<>(previewForm).
                createButtonControls(CLONE_BTN_PRESENTED | DELETE_BTN_PRESENTED,
                        RolePermissionCollections.CRM_CAMPAIGN);
        MButton editBtn = new MButton("", clickEvent -> EventBusFactory.getInstance().post(new CampaignEvent.GotoEdit(this, beanItem)))
                .withIcon(FontAwesome.EDIT).withStyleName(UIConstants.CIRCLE_BOX)
                .withVisible(UserUIContext.canWrite(RolePermissionCollections.CRM_CAMPAIGN));
        return new MHorizontalLayout(editBtn, new NavigationBarQuickMenu(buttonControls));
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        MVerticalLayout toolbarLayout = new MVerticalLayout().withFullWidth().withSpacing(false).withMargin(false);

        Component accountSection = FormSectionBuilder.build(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT),
                associateAccounts);
        toolbarLayout.addComponent(accountSection);

        Component contactSection = FormSectionBuilder.build(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT),
                associateContacts);
        toolbarLayout.addComponent(contactSection);

        Component leadSection = FormSectionBuilder.build(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD),
                associateLeads);
        toolbarLayout.addComponent(leadSection);

        Component activitySection = FormSectionBuilder.build(CrmAssetsManager.getAsset(CrmTypeConstants.ACTIVITY),
                associateActivities);
        toolbarLayout.addComponent(activitySection);

        return toolbarLayout;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.CAMPAIGN;
    }
}
