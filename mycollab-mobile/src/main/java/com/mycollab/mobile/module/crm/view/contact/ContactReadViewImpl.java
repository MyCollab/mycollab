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
package com.mycollab.mobile.module.crm.view.contact;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.form.view.DynaFormLayout;
import com.mycollab.mobile.module.crm.events.ContactEvent;
import com.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.mycollab.mobile.module.crm.ui.CrmRelatedItemsScreenData;
import com.mycollab.mobile.module.crm.view.activity.ActivityRelatedItemView;
import com.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.mycollab.mobile.ui.IconConstants;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.module.crm.service.LeadService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class ContactReadViewImpl extends AbstractPreviewItemComp<SimpleContact> implements ContactReadView {
    private static final long serialVersionUID = 1L;

    private ContactRelatedOpportunityView associateOpportunityList;
    private ActivityRelatedItemView associateActivityList;

    @Override
    protected ComponentContainer createBottomPanel() {
        MHorizontalLayout toolbarLayout = new MHorizontalLayout();
        toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        Button relatedOpportunities = new Button();
        relatedOpportunities
                .setCaption("<span aria-hidden=\"true\" data-icon=\""
                        + IconConstants.CRM_OPPORTUNITY
                        + "\"></span><div class=\"screen-reader-text\">"
                        + UserUIContext.getMessage(OpportunityI18nEnum.LIST)
                        + "</div>");
        relatedOpportunities.setHtmlContentAllowed(true);
        relatedOpportunities.addClickListener(clickEvent -> EventBusFactory.getInstance().post(new ContactEvent.GotoRelatedItems(this,
                new CrmRelatedItemsScreenData(associateOpportunityList))));

        toolbarLayout.addComponent(relatedOpportunities);

        Button relatedActivities = new Button();
        relatedActivities.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_ACTIVITY
                + "\"></span><div class=\"screen-reader-text\">"
                + UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY)
                + "</div>");
        relatedActivities.setHtmlContentAllowed(true);
        relatedActivities.addClickListener(clickEvent -> EventBusFactory.getInstance().post(new ContactEvent.GotoRelatedItems(this,
                new CrmRelatedItemsScreenData(associateActivityList))));
        toolbarLayout.addComponent(relatedActivities);

        return toolbarLayout;
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

    @Override
    protected void initRelatedComponents() {
        associateActivityList = new ActivityRelatedItemView(CrmTypeConstants.CONTACT);
        associateOpportunityList = new ContactRelatedOpportunityView();
    }

    @Override
    protected void afterPreviewItem() {
        associateActivityList.displayActivity(beanItem.getId());
        associateOpportunityList.displayOpportunities(beanItem);
    }

    @Override
    protected String initFormHeader() {
        // check if there is converted lead associates with this contact
        LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
        SimpleLead lead = leadService.findConvertedLeadOfContact(
                beanItem.getId(), MyCollabUI.getAccountId());
        if (lead != null) {
            return beanItem.getContactName() + "&nbsp;"
                    + UserUIContext.getMessage(LeadI18nEnum.CONVERT_FROM_LEAD_TITLE,
                    CrmAssetsManager.getAsset(CrmTypeConstants.LEAD),
                    CrmLinkGenerator.generateCrmItemLink(CrmTypeConstants.LEAD, lead.getId()),
                    lead.getLeadName());
        } else {
            return beanItem.getContactName();
        }
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
    protected String getType() {
        return CrmTypeConstants.CONTACT;
    }

    @Override
    public HasPreviewFormHandlers<SimpleContact> getPreviewFormHandlers() {
        return previewForm;
    }

}
