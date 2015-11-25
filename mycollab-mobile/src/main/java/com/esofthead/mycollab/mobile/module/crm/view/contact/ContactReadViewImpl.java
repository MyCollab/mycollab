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
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.form.view.DynaFormLayout;
import com.esofthead.mycollab.mobile.module.crm.events.ContactEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmPreviewFormControlsGenerator;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmRelatedItemsScreenData;
import com.esofthead.mycollab.mobile.module.crm.ui.NotesList;
import com.esofthead.mycollab.mobile.module.crm.view.activity.ActivityRelatedItemView;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.AbstractPreviewItemComp;
import com.esofthead.mycollab.mobile.ui.AdvancedPreviewBeanForm;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasPreviewFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.IRelatedListHandlers;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
@ViewComponent
public class ContactReadViewImpl extends AbstractPreviewItemComp<SimpleContact> implements ContactReadView {
    private static final long serialVersionUID = 1L;

    protected ContactRelatedOpportunityView associateOpportunityList;
    protected ActivityRelatedItemView associateActivityList;
    protected NotesList noteListItems;

    @Override
    protected ComponentContainer createBottomPanel() {
        HorizontalLayout toolbarLayout = new HorizontalLayout();
        toolbarLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        toolbarLayout.setSpacing(true);

        Button relatedOpportunities = new Button();
        relatedOpportunities
                .setCaption("<span aria-hidden=\"true\" data-icon=\""
                        + IconConstants.CRM_OPPORTUNITY
                        + "\"></span><div class=\"screen-reader-text\">"
                        + AppContext
                        .getMessage(CrmCommonI18nEnum.TAB_OPPORTUNITY)
                        + "</div>");
        relatedOpportunities.setHtmlContentAllowed(true);
        relatedOpportunities.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 7589415773039335559L;

            @Override
            public void buttonClick(ClickEvent arg0) {
                EventBusFactory.getInstance().post(new ContactEvent.GoToRelatedItems(this, new CrmRelatedItemsScreenData(
                        associateOpportunityList)));
            }
        });

        toolbarLayout.addComponent(relatedOpportunities);

        Button relatedNotes = new Button();
        relatedNotes.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_DOCUMENT
                + "\"></span><div class=\"screen-reader-text\">"
                + "Notes" + "</div>");
        relatedNotes.setHtmlContentAllowed(true);
        relatedNotes.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 7589415773039335559L;

            @Override
            public void buttonClick(ClickEvent evt) {
                EventBusFactory.getInstance().post(new ContactEvent.GoToRelatedItems(this, new CrmRelatedItemsScreenData(noteListItems)));
            }
        });
        toolbarLayout.addComponent(relatedNotes);

        Button relatedActivities = new Button();
        relatedActivities.setCaption("<span aria-hidden=\"true\" data-icon=\""
                + IconConstants.CRM_ACTIVITY
                + "\"></span><div class=\"screen-reader-text\">"
                + AppContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY)
                + "</div>");
        relatedActivities.setHtmlContentAllowed(true);
        relatedActivities.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 7589415773039335559L;

            @Override
            public void buttonClick(ClickEvent evt) {
                EventBusFactory.getInstance().post(
                        new ContactEvent.GoToRelatedItems(this, new CrmRelatedItemsScreenData(associateActivityList)));
            }
        });
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
        noteListItems = new NotesList(AppContext.getMessage(CrmCommonI18nEnum.M_TITLE_RELATED_NOTES));
    }

    @Override
    protected void afterPreviewItem() {
        associateActivityList.displayActivity(beanItem.getId());
        associateOpportunityList.displayOpportunities(beanItem);
        noteListItems.showNotes(CrmTypeConstants.CONTACT, beanItem.getId());
    }

    @Override
    protected String initFormTitle() {
        // check if there is converted lead associates with this contact
        LeadService leadService = ApplicationContextUtil.getSpringBean(LeadService.class);
        SimpleLead lead = leadService.findConvertedLeadOfContact(
                beanItem.getId(), AppContext.getAccountId());
        if (lead != null) {
            return beanItem.getContactName() + "&nbsp;"
                    + AppContext.getMessage(LeadI18nEnum.CONVERT_FROM_LEAD_TITLE,
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
