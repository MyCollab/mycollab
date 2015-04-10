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
package com.esofthead.mycollab.module.crm.view.opportunity;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmDataTypeFactory;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.ContactOpportunity;
import com.esofthead.mycollab.module.crm.domain.Opportunity;
import com.esofthead.mycollab.module.crm.domain.SimpleContactOpportunityRel;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.service.ContactOpportunityService;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.RelatedListComp2;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class OpportunityContactListComp
        extends
        RelatedListComp2<ContactOpportunityService, ContactSearchCriteria, SimpleContactOpportunityRel> {
    private static final long serialVersionUID = 5717208523696358616L;

    private Opportunity opportunity;

    public static Map<String, String> colormap = new HashMap<>();

    static {
        for (int i = 0; i < CrmDataTypeFactory.getOpportunityContactRoleList().length; i++) {
            String roleKeyName = CrmDataTypeFactory
                    .getOpportunityContactRoleList()[i];
            if (!colormap.containsKey(roleKeyName)) {
                colormap.put(roleKeyName,
                        AbstractBeanBlockList.getColorStyleNameList()[i]);
            }
        }
    }

    public OpportunityContactListComp() {
        super(ApplicationContextUtil
                .getSpringBean(ContactOpportunityService.class), 20);
        this.setBlockDisplayHandler(new OpportunityContactBlockDisplay());
    }

    @Override
    protected Component generateTopControls() {
        MHorizontalLayout controlsBtnWrap = new MHorizontalLayout().withSpacing(false).withWidth("100%");

        MHorizontalLayout notesWrap = new MHorizontalLayout().withWidth("100%");
        Label noteLbl = new Label("Note: ");
        noteLbl.setSizeUndefined();
        noteLbl.setStyleName("list-note-lbl");
        notesWrap.addComponent(noteLbl);

        CssLayout noteBlock = new CssLayout();
        noteBlock.setWidth("100%");
        noteBlock.setStyleName("list-note-block");
        for (int i = 0; i < CrmDataTypeFactory.getOpportunityContactRoleList().length; i++) {
            Label note = new Label(
                    CrmDataTypeFactory.getOpportunityContactRoleList()[i]);
            note.setStyleName("note-label");
            note.addStyleName(colormap.get(CrmDataTypeFactory
                    .getOpportunityContactRoleList()[i]));
            note.setSizeUndefined();

            noteBlock.addComponent(note);
        }
        notesWrap.with(noteBlock).expand(noteBlock);

        controlsBtnWrap.addComponent(notesWrap);

        final SplitButton controlsBtn = new SplitButton();
        controlsBtn.setSizeUndefined();
        controlsBtn.setEnabled(AppContext
                .canWrite(RolePermissionCollections.CRM_CONTACT));
        controlsBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
        controlsBtn.setCaption("Add/Edit Contacts' Role");
        controlsBtn.setIcon(FontAwesome.PLUS);
        controlsBtn
                .addClickListener(new SplitButton.SplitButtonClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void splitButtonClick(
                            final SplitButton.SplitButtonClickEvent event) {
                        EventBusFactory.getInstance().post(
                                new OpportunityEvent.GotoContactRoleEdit(this,
                                        opportunity));
                    }
                });
        final Button selectBtn = new Button("Select from existing contacts",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        OpportunityContactSelectionWindow contactsWindow = new OpportunityContactSelectionWindow(
                                OpportunityContactListComp.this);
                        ContactSearchCriteria criteria = new ContactSearchCriteria();
                        criteria.setSaccountid(new NumberSearchField(AppContext
                                .getAccountId()));
                        UI.getCurrent().addWindow(contactsWindow);
                        contactsWindow.setSearchCriteria(criteria);
                        controlsBtn.setPopupVisible(false);
                    }
                });
        selectBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
        OptionPopupContent buttonControlLayout = new OptionPopupContent();
        buttonControlLayout.addOption(selectBtn);
        controlsBtn.setContent(buttonControlLayout);

        controlsBtnWrap.with(controlsBtn).withAlign(controlsBtn, Alignment.MIDDLE_RIGHT);
        return controlsBtnWrap;
    }

    public void displayContacts(Opportunity opportunity) {
        this.opportunity = opportunity;
        loadContacts();
    }

    private void loadContacts() {
        final ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(SearchField.AND,
                AppContext.getAccountId()));
        criteria.setOpportunityId(new NumberSearchField(SearchField.AND,
                opportunity.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadContacts();
    }

    public class OpportunityContactBlockDisplay implements
            BlockDisplayHandler<SimpleContactOpportunityRel> {

        @Override
        public Component generateBlock(
                final SimpleContactOpportunityRel contact, int blockIndex) {
            CssLayout beanBlock = new CssLayout();
            beanBlock.addStyleName("bean-block");
            beanBlock.setWidth("350px");

            VerticalLayout blockContent = new VerticalLayout();
            MHorizontalLayout blockTop = new MHorizontalLayout().withWidth("100%");
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            FontIconLabel contactAvatar = new FontIconLabel(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
            iconWrap.addComponent(contactAvatar);
            blockTop.addComponent(iconWrap);

            VerticalLayout contactInfo = new VerticalLayout();
            contactInfo.setSpacing(true);

            MButton btnDelete = new MButton(FontAwesome.TRASH_O);
            btnDelete.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
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
                                        final ContactService contactService = ApplicationContextUtil
                                                .getSpringBean(ContactService.class);
                                        ContactOpportunity associateContact = new ContactOpportunity();
                                        associateContact
                                                .setOpportunityid(opportunity
                                                        .getId());
                                        associateContact.setContactid(contact
                                                .getId());
                                        contactService
                                                .removeContactOpportunityRelationship(
                                                        associateContact,
                                                        AppContext
                                                                .getAccountId());
                                        OpportunityContactListComp.this
                                                .refresh();
                                    }
                                }
                            });
                }
            });
            btnDelete.addStyleName(UIConstants.BUTTON_ICON_ONLY);

            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            Label contactName = new Label("Name: <a href='"
                    + SiteConfiguration.getSiteUrl(AppContext.getUser()
                    .getSubdomain())
                    + CrmLinkGenerator.generateCrmItemLink(
                    CrmTypeConstants.CONTACT, contact.getId()) + "'>"
                    + contact.getContactName() + "</a>", ContentMode.HTML);

            contactInfo.addComponent(contactName);

            Label contactTitle = new Label("Title: "
                    + (contact.getTitle() != null ? contact.getTitle() : ""));
            contactInfo.addComponent(contactTitle);

            Label contactEmail = new Label("Email: "
                    + (contact.getEmail() != null ? "<a href='mailto:"
                    + contact.getEmail() + "'>" + contact.getEmail()
                    + "</a>" : ""), ContentMode.HTML);
            contactInfo.addComponent(contactEmail);

            Label contactOfficePhone = new Label(
                    "Office Phone: "
                            + (contact.getOfficephone() != null ? contact
                            .getOfficephone() : ""));
            contactInfo.addComponent(contactOfficePhone);

            Label contactRole = new Label(
                    "Contact Role: "
                            + (contact.getDecisionRole() != null ? contact
                            .getDecisionRole() : ""));
            contactInfo.addComponent(contactRole);

            if (contact.getDecisionRole() != null) {
                beanBlock.addStyleName(colormap.get(contact.getDecisionRole()));
            }

            blockTop.with(contactInfo).expand(contactInfo);
            blockContent.addComponent(blockTop);

            blockContent.setWidth("100%");

            beanBlock.addComponent(blockContent);
            return beanBlock;
        }
    }
}
