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
package com.mycollab.module.crm.view.opportunity;

import com.google.common.base.MoreObjects;
import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.ContactOpportunity;
import com.mycollab.module.crm.domain.Opportunity;
import com.mycollab.module.crm.domain.SimpleContactOpportunityRel;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.event.OpportunityEvent;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.i18n.OptionI18nEnum;
import com.mycollab.module.crm.service.ContactOpportunityService;
import com.mycollab.module.crm.service.ContactService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.RelatedListComp2;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class OpportunityContactListComp extends RelatedListComp2<ContactOpportunityService, ContactSearchCriteria, SimpleContactOpportunityRel> {
    private static final long serialVersionUID = 5717208523696358616L;

    private Opportunity opportunity;

    static final Map<String, String> colormap;

    static {
        Map<String, String> tmpMap = new HashMap<>();
        for (int i = 0; i < CrmDataTypeFactory.getOpportunityContactRoleList().length; i++) {
            String roleKeyName = CrmDataTypeFactory.getOpportunityContactRoleList()[i].name();
            if (!tmpMap.containsKey(roleKeyName)) {
                tmpMap.put(roleKeyName, AbstractBeanBlockList.COLOR_STYLENAME_LIST[i]);
            }
        }
        colormap = Collections.unmodifiableMap(tmpMap);
    }

    public OpportunityContactListComp() {
        super(AppContextUtil.getSpringBean(ContactOpportunityService.class), 20);
        setMargin(true);
        this.setBlockDisplayHandler(new OpportunityContactBlockDisplay());
    }

    @Override
    protected Component generateTopControls() {
        MHorizontalLayout controlsBtnWrap = new MHorizontalLayout().withSpacing(false).withFullWidth();

        MHorizontalLayout notesWrap = new MHorizontalLayout().withFullWidth();
        Label noteLbl = new Label(UserUIContext.getMessage(GenericI18Enum.OPT_NOTE));
        noteLbl.setSizeUndefined();
        noteLbl.setStyleName("list-note-lbl");
        notesWrap.addComponent(noteLbl);

        CssLayout noteBlock = new CssLayout();
        noteBlock.setWidth("100%");
        noteBlock.setStyleName("list-note-block");
        for (OptionI18nEnum.OpportunityContactRole role : CrmDataTypeFactory.getOpportunityContactRoleList()) {
            Label note = new Label(UserUIContext.getMessage(role));
            note.setStyleName("note-label");
            note.addStyleName(colormap.get(role.name()));
            note.setSizeUndefined();
            noteBlock.addComponent(note);
        }
        notesWrap.with(noteBlock).expand(noteBlock);
        controlsBtnWrap.addComponent(notesWrap);

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_CONTACT)) {
            final SplitButton controlsBtn = new SplitButton();
            controlsBtn.setSizeUndefined();
            controlsBtn.addStyleName(WebUIConstants.BUTTON_ACTION);
            controlsBtn.setCaption(UserUIContext.getMessage(ContactI18nEnum.OPT_ADD_EDIT_CONTACT_ROLES));
            controlsBtn.setIcon(FontAwesome.PLUS);
            controlsBtn.addClickListener(event -> EventBusFactory.getInstance().post(new OpportunityEvent.GotoContactRoleEdit(this, opportunity)));
            final Button selectBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> {
                OpportunityContactSelectionWindow contactsWindow = new OpportunityContactSelectionWindow(OpportunityContactListComp.this);
                ContactSearchCriteria criteria = new ContactSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                UI.getCurrent().addWindow(contactsWindow);
                contactsWindow.setSearchCriteria(criteria);
                controlsBtn.setPopupVisible(false);
            });
            selectBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
            OptionPopupContent buttonControlLayout = new OptionPopupContent();
            buttonControlLayout.addOption(selectBtn);
            controlsBtn.setContent(buttonControlLayout);

            controlsBtnWrap.with(controlsBtn).withAlign(controlsBtn, Alignment.MIDDLE_RIGHT);
        }

        return controlsBtnWrap;
    }

    public void displayContacts(Opportunity opportunity) {
        this.opportunity = opportunity;
        loadContacts();
    }

    private void loadContacts() {
        final ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        criteria.setOpportunityId(new NumberSearchField(opportunity.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadContacts();
    }

    private class OpportunityContactBlockDisplay implements BlockDisplayHandler<SimpleContactOpportunityRel> {

        @Override
        public Component generateBlock(final SimpleContactOpportunityRel contact, int blockIndex) {
            CssLayout beanBlock = new CssLayout();
            beanBlock.addStyleName("bean-block");
            beanBlock.setWidth("350px");

            VerticalLayout blockContent = new VerticalLayout();
            MHorizontalLayout blockTop = new MHorizontalLayout().withFullWidth();
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            ELabel contactAvatar = ELabel.fontIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
            iconWrap.addComponent(contactAvatar);
            blockTop.addComponent(iconWrap);

            VerticalLayout contactInfo = new VerticalLayout();
            contactInfo.setSpacing(true);

            MButton btnDelete = new MButton("", clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, MyCollabUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                final ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                                ContactOpportunity associateContact = new ContactOpportunity();
                                associateContact.setOpportunityid(opportunity.getId());
                                associateContact.setContactid(contact.getId());
                                contactService.removeContactOpportunityRelationship(associateContact, MyCollabUI.getAccountId());
                                OpportunityContactListComp.this.refresh();
                            }
                        });
            }).withIcon(FontAwesome.TRASH_O).withStyleName(WebUIConstants.BUTTON_ICON_ONLY);

            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            Label contactName = ELabel.html(UserUIContext.getMessage(GenericI18Enum.FORM_NAME) + ": " + new A(CrmLinkGenerator.generateCrmItemLink(
                    CrmTypeConstants.CONTACT, contact.getId())).appendText(contact.getContactName()).write());

            contactInfo.addComponent(contactName);

            Label contactTitle = new Label(UserUIContext.getMessage(ContactI18nEnum.FORM_TITLE) + ": " + MoreObjects.firstNonNull(contact.getTitle(), ""));
            contactInfo.addComponent(contactTitle);

            String email = MoreObjects.firstNonNull(contact.getEmail(), "");
            Label contactEmail = ELabel.html(UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL) + ": " + new A("mailto:" + email).appendText(email).write());
            contactInfo.addComponent(contactEmail);

            Label contactOfficePhone = new Label(UserUIContext.getMessage(ContactI18nEnum.FORM_OFFICE_PHONE) + ": " +
                    MoreObjects.firstNonNull(contact.getOfficephone(), ""));
            contactInfo.addComponent(contactOfficePhone);

            Label contactRole = new Label(UserUIContext.getMessage(ContactI18nEnum.FORM_DECISION_ROLE) + ": " +
                    MoreObjects.firstNonNull(contact.getDecisionRole(), ""));
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
