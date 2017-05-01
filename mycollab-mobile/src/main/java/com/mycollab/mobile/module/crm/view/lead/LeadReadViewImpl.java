/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.form.view.DynaFormLayout;
import com.mycollab.mobile.module.crm.events.LeadEvent;
import com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.mycollab.mobile.module.crm.ui.CrmRelatedItemsScreenData;
import com.mycollab.mobile.module.crm.view.activity.ActivityRelatedItemView;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.mobile.ui.IconConstants;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@ViewComponent
public class LeadReadViewImpl extends AbstractPreviewItemComp<SimpleLead> implements LeadReadView {
    private static final long serialVersionUID = 5288751461504873888L;

    private ActivityRelatedItemView associateActivities;
    private LeadRelatedCampaignView associateCampaigns;

    @Override
    public HasPreviewFormHandlers<SimpleLead> getPreviewFormHandlers() {
        return this.previewForm;
    }

    @Override
    protected void initRelatedComponents() {
        associateCampaigns = new LeadRelatedCampaignView();
        associateActivities = new ActivityRelatedItemView(CrmTypeConstants.CAMPAIGN);
    }

    @Override
    protected void afterPreviewItem() {
        associateCampaigns.displayCampaign(beanItem);
        associateActivities.displayActivity(beanItem.getId());
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
    protected ComponentContainer createButtonControls() {
        return new CrmPreviewFormControlsGenerator<>(this.previewForm).createButtonControls(RolePermissionCollections.CRM_LEAD);
    }

    @Override
    protected ComponentContainer createBottomPanel() {
        HorizontalLayout toolbarLayout = new HorizontalLayout();
        toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        toolbarLayout.setSpacing(true);

        Button relatedCampaigns = new Button();
        relatedCampaigns.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_CAMPAIGN
                + "\"></span><div class=\"screen-reader-text\">"
                + UserUIContext.getMessage(CampaignI18nEnum.LIST)
                + "</div>");
        relatedCampaigns.setHtmlContentAllowed(true);
        relatedCampaigns.addClickListener(clickEvent -> EventBusFactory.getInstance().post(new LeadEvent.GotoRelatedItems(
                LeadReadViewImpl.this, new CrmRelatedItemsScreenData(associateCampaigns))));
        toolbarLayout.addComponent(relatedCampaigns);

        Button relatedActivities = new Button();
        relatedActivities.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_ACTIVITY
                + "\"></span><div class=\"screen-reader-text\">"
                + UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY)
                + "</div>");
        relatedActivities.setHtmlContentAllowed(true);
        relatedActivities.addClickListener(clickEvent -> EventBusFactory.getInstance().post(new LeadEvent.GotoRelatedItems(this,
                new CrmRelatedItemsScreenData(associateActivities))));
        toolbarLayout.addComponent(relatedActivities);

        return toolbarLayout;
    }

    @Override
    protected String getType() {
        return CrmTypeConstants.LEAD;
    }
}
