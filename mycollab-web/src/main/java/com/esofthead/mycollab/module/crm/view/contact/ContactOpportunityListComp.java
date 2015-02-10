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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.domain.ContactOpportunity;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.RelatedListComp2;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.maddon.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class ContactOpportunityListComp
        extends
        RelatedListComp2<OpportunityService, OpportunitySearchCriteria, SimpleOpportunity> {
    private static final long serialVersionUID = 8849210168154580096L;

    private Contact contact;

    public ContactOpportunityListComp() {
        super(ApplicationContextUtil.getSpringBean(OpportunityService.class),
                20);
        this.setBlockDisplayHandler(new ContactOpportunityBlockDisplay());
    }

    @Override
    protected Component generateTopControls() {
        VerticalLayout controlsBtnWrap = new VerticalLayout();
        controlsBtnWrap.setWidth("100%");
        final SplitButton controlsBtn = new SplitButton();
        controlsBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
        controlsBtn.setCaption(AppContext
                .getMessage(OpportunityI18nEnum.BUTTON_NEW_OPPORTUNITY));
        controlsBtn.setIcon(FontAwesome.PLUS);
        controlsBtn
                .addClickListener(new SplitButton.SplitButtonClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void splitButtonClick(
                            SplitButton.SplitButtonClickEvent event) {
                        fireNewRelatedItem("");
                    }
                });
        controlsBtn.setSizeUndefined();
        Button selectBtn = new Button("Select from existing opportunities",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        ContactOpportunitySelectionWindow opportunitiesWindow = new ContactOpportunitySelectionWindow(
                                ContactOpportunityListComp.this);
                        OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
                        criteria.setSaccountid(new NumberSearchField(AppContext
                                .getAccountId()));
                        UI.getCurrent().addWindow(opportunitiesWindow);
                        opportunitiesWindow.setSearchCriteria(criteria);
                        controlsBtn.setPopupVisible(false);
                    }
                });
        selectBtn.setIcon(MyCollabResource.newResource(WebResourceIds._16_select));
        selectBtn.setStyleName("link");

        VerticalLayout buttonControlsLayout = new VerticalLayout();
        buttonControlsLayout.addComponent(selectBtn);
        controlsBtn.setContent(buttonControlsLayout);

        controlsBtn.setEnabled(AppContext
                .canWrite(RolePermissionCollections.CRM_OPPORTUNITY));

        controlsBtnWrap.addComponent(controlsBtn);
        controlsBtnWrap.setComponentAlignment(controlsBtn,
                Alignment.MIDDLE_RIGHT);
        return controlsBtnWrap;
    }

    public void displayOpportunities(final Contact contact) {
        this.contact = contact;
        loadOpportunities();
    }

    private void loadOpportunities() {
        final OpportunitySearchCriteria criteria = new OpportunitySearchCriteria();
        criteria.setSaccountid(new NumberSearchField(SearchField.AND,
                AppContext.getAccountId()));
        criteria.setContactId(new NumberSearchField(SearchField.AND, contact
                .getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadOpportunities();
    }

    public class ContactOpportunityBlockDisplay implements
            BlockDisplayHandler<SimpleOpportunity> {

        @Override
        public Component generateBlock(final SimpleOpportunity opportunity,
                                       int blockIndex) {
            CssLayout beanBlock = new CssLayout();
            beanBlock.addStyleName("bean-block");
            beanBlock.setWidth("350px");

            VerticalLayout blockContent = new VerticalLayout();
            HorizontalLayout blockTop = new HorizontalLayout();
            blockTop.setSpacing(true);
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            FontIconLabel opportunityIcon = new FontIconLabel(CrmAssetsManager.getAsset(CrmTypeConstants.OPPORTUNITY));
            iconWrap.addComponent(opportunityIcon);
            blockTop.addComponent(iconWrap);

            VerticalLayout opportunityInfo = new VerticalLayout();
            opportunityInfo.setSpacing(true);

            MButton btnDelete = new MButton(FontAwesome.TRASH_O);
            btnDelete.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    ConfirmDialogExt.show(
                            UI.getCurrent(),
                            AppContext.getMessage(
                                    GenericI18Enum.DIALOG_DELETE_TITLE,
                                    SiteConfiguration.getSiteName()),
                            AppContext
                                    .getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            AppContext
                                    .getMessage(GenericI18Enum.BUTTON_YES),
                            AppContext
                                    .getMessage(GenericI18Enum.BUTTON_NO),
                            new ConfirmDialog.Listener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClose(ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        ContactService contactService = ApplicationContextUtil
                                                .getSpringBean(ContactService.class);
                                        ContactOpportunity associateOpportunity = new ContactOpportunity();
                                        associateOpportunity
                                                .setContactid(contact.getId());
                                        associateOpportunity
                                                .setOpportunityid(opportunity
                                                        .getId());
                                        contactService
                                                .removeContactOpportunityRelationship(
                                                        associateOpportunity,
                                                        AppContext
                                                                .getAccountId());
                                        ContactOpportunityListComp.this
                                                .refresh();
                                    }
                                }
                            });
                }
            });

            btnDelete.addStyleName(UIConstants.BUTTON_ICON_ONLY);

            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            Label opportunityName = new Label("Name: <a href='"
                    + SiteConfiguration.getSiteUrl(AppContext.getSession()
                    .getSubdomain())
                    + CrmLinkGenerator.generateCrmItemLink(
                    CrmTypeConstants.OPPORTUNITY, opportunity.getId())
                    + "'>" + opportunity.getOpportunityname() + "</a>",
                    ContentMode.HTML);

            opportunityInfo.addComponent(opportunityName);

            Label opportunityAmount = new Label(
                    "Amount: "
                            + (opportunity.getAmount() != null ? opportunity
                            .getAmount() : ""));
            if (opportunity.getCurrency() != null
                    && opportunity.getAmount() != null) {
                opportunityAmount.setValue(opportunityAmount.getValue()
                        + opportunity.getCurrency().getSymbol());
            }
            opportunityInfo.addComponent(opportunityAmount);

            Label opportunitySaleStage = new Label(
                    "Sale Stage: "
                            + (opportunity.getSalesstage() != null ? opportunity
                            .getSalesstage() : ""));
            opportunityInfo.addComponent(opportunitySaleStage);

            Label opportunityExpectedCloseDate = new Label(
                    "Expected Close Date: "
                            + (opportunity.getExpectedcloseddate() != null ? AppContext
                            .formatDate(opportunity
                                    .getExpectedcloseddate()) : ""));
            opportunityInfo.addComponent(opportunityExpectedCloseDate);

            blockTop.addComponent(opportunityInfo);
            blockTop.setExpandRatio(opportunityInfo, 1.0f);
            blockTop.setWidth("100%");
            blockContent.addComponent(blockTop);

            blockContent.setWidth("100%");

            beanBlock.addComponent(blockContent);
            return beanBlock;
        }

    }

}
