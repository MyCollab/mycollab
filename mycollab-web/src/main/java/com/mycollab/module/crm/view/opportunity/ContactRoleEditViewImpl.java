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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Contact;
import com.mycollab.module.crm.domain.ContactOpportunity;
import com.mycollab.module.crm.domain.SimpleContactOpportunityRel;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.events.AccountEvent;
import com.mycollab.module.crm.events.ContactEvent;
import com.mycollab.module.crm.events.OpportunityEvent;
import com.mycollab.module.crm.service.ContactOpportunityService;
import com.mycollab.module.crm.service.ContactService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.view.contact.ContactSelectionField;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.AddViewLayout2;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.mycollab.vaadin.web.ui.ValueComboBox;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@ViewComponent
public class ContactRoleEditViewImpl extends AbstractPageView implements ContactRoleEditView {
    private static final long serialVersionUID = 1L;

    private ContactOpportunityList contactRoleList;
    private SimpleOpportunity opportunity;

    @Override
    public void display(SimpleOpportunity opportunity) {
        this.opportunity = opportunity;
        this.removeAllComponents();
        this.setMargin(new MarginInfo(false, true, true, true));
        this.addStyleName("oppcontact-role-edit");

        AddViewLayout2 previewLayout = new AddViewLayout2("Add or Edit Contact Roles", CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
        this.addComponent(previewLayout);

        ComponentContainer actionControls = createButtonControls();
        if (actionControls != null) {
            previewLayout.addControlButtons(actionControls);
        }

        contactRoleList = new ContactOpportunityList();
        previewLayout.addBody(contactRoleList);

        MButton addMoreContactRolesBtn = new MButton("Add more contact roles", clickEvent -> {
            SimpleContactOpportunityRel contactRole = new SimpleContactOpportunityRel();
            ContactRoleRowComp row = new ContactRoleRowComp(contactRole);
            contactRoleList.addRow(row);
        }).withStyleName(UIConstants.BUTTON_ACTION);

        HorizontalLayout buttonControls = new HorizontalLayout();
        buttonControls.addComponent(addMoreContactRolesBtn);
        buttonControls.setMargin(new MarginInfo(true, true, true, true));

        previewLayout.addBody(buttonControls);
    }

    private ComponentContainer createButtonControls() {
        MButton updateBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_UPDATE_LABEL), clickEvent ->
                updateContactRoles()).withIcon(FontAwesome.SAVE).withStyleName(UIConstants.BUTTON_ACTION);

        MButton cancelBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL),
                clickEvent -> EventBusFactory.getInstance().post(new ContactEvent.GotoList(this, null)))
                .withIcon(FontAwesome.TIMES).withStyleName(UIConstants.BUTTON_OPTION);

        return new MHorizontalLayout(cancelBtn, updateBtn);
    }

    private void updateContactRoles() {
        Iterator<Component> components = contactRoleList.getBodySubComponents();
        List<ContactOpportunity> contactOpps = new ArrayList<>();

        while (components.hasNext()) {
            Component component = components.next();
            if (component instanceof ContactRoleRowComp) {
                ContactOpportunity contactVal = ((ContactRoleRowComp) component).getContactVal();
                if (contactVal != null) {
                    contactOpps.add(contactVal);
                }
            }
        }

        if (contactOpps.size() > 0) {
            ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
            contactService.saveContactOpportunityRelationship(contactOpps, AppContext.getAccountId());
        }

        // lead user to opportunity view
        EventBusFactory.getInstance().post(new OpportunityEvent.GotoRead(ContactRoleEditViewImpl.this, opportunity.getId()));
    }

    private class ContactOpportunityList extends VerticalLayout {
        private static final long serialVersionUID = 1L;

        private final CssLayout bodyWrapper;

        public ContactOpportunityList() {
            super();
            this.setStyleName("contactopp-list");

            MHorizontalLayout header = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, true))
                    .withFullWidth().withStyleName("contactopp-list-header");

            Label contactLbl = new Label("Contact");
            contactLbl.setWidth("250px");
            header.addComponent(contactLbl);

            Label accountLbl = new Label("Account");
            accountLbl.setWidth("250px");
            header.addComponent(accountLbl);

            Label roleLbl = new Label("Role");
            roleLbl.setWidth("250px");
            header.addComponent(roleLbl);
            header.setExpandRatio(roleLbl, 1.0f);

            this.addComponent(header);

            bodyWrapper = new CssLayout();
            bodyWrapper.setStyleName("contactopp-list-body");
            bodyWrapper.setSizeFull();

            ContactOpportunityService contactOppoService = AppContextUtil.getSpringBean(ContactOpportunityService.class);
            ContactSearchCriteria criteria = new ContactSearchCriteria();
            criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
            criteria.setOpportunityId(new NumberSearchField(SearchField.AND, opportunity.getId()));
            List<SimpleContactOpportunityRel> contactOppoRels = contactOppoService.findPageableListByCriteria(new BasicSearchRequest<>(criteria));
            boolean oddRow = true;
            if (!CollectionUtils.isEmpty(contactOppoRels)) {
                for (SimpleContactOpportunityRel contactOppoRel : contactOppoRels) {
                    ContactRoleRowComp rowComp = new ContactRoleRowComp(contactOppoRel);
                    if (oddRow) {
                        rowComp.addStyleName("odd");
                        oddRow = !oddRow;
                    }

                    bodyWrapper.addComponent(rowComp);
                }
            }

            if (bodyWrapper.getComponentCount() == 0) {
                bodyWrapper.addStyleName("no-child");
            }

            this.addComponent(bodyWrapper);
        }

        public void addRow(Component child) {
            if (bodyWrapper.getComponentCount() % 2 == 0)
                child.addStyleName("odd");

            bodyWrapper.addComponent(child);
            bodyWrapper.removeStyleName("no-child");
        }

        public Iterator<Component> getBodySubComponents() {
            return bodyWrapper.iterator();
        }

    }

    private class ContactRoleRowComp extends MHorizontalLayout {
        private static final long serialVersionUID = 1L;

        private ContactSelectionField contactField;
        private RoleDecisionComboBox roleBox;

        public ContactRoleRowComp(final SimpleContactOpportunityRel contactOpp) {
            this.withMargin(true).withFullWidth().withStyleName("contactrole-row");

            contactField = new ContactSelectionField();
            this.addComponent(contactField);
            contactField.setPropertyDataSource(new AbstractField<SimpleContactOpportunityRel>() {
                private static final long serialVersionUID = 1L;

                @Override
                public SimpleContactOpportunityRel getValue() {
                    return contactOpp;
                }

                @Override
                public Class<? extends SimpleContactOpportunityRel> getType() {
                    return SimpleContactOpportunityRel.class;
                }

            });
            contactField.setWidth("250px");

            MButton accountLink = new MButton(contactOpp.getAccountName(),
                    clickEvent -> EventBusFactory.getInstance().post(new AccountEvent.GotoRead(this, contactOpp.getAccountid())))
                    .withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT)).withStyleName(UIConstants.BUTTON_LINK);
            accountLink.setWidth("250px");
            this.addComponent(accountLink);

            roleBox = new RoleDecisionComboBox();
            if (contactOpp.getDecisionRole() != null) {
                roleBox.setValue(contactOpp.getDecisionRole());
            }
            roleBox.setWidth("250px");
            this.addComponent(roleBox);

            MButton deleteBtn = new MButton("", clickEvent -> {
                ((CssLayout) ContactRoleRowComp.this.getParent()).removeComponent(ContactRoleRowComp.this);

                // The contact opportunity relationship is existed
                if (contactOpp.getId() != null) {
                    ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                    ContactOpportunity associateOpportunity = new ContactOpportunity();
                    associateOpportunity.setContactid(contactOpp.getId());
                    associateOpportunity.setOpportunityid(opportunity.getId());
                    contactService.removeContactOpportunityRelationship(associateOpportunity, AppContext.getAccountId());
                }
            }).withIcon(FontAwesome.TRASH_O).withStyleName(UIConstants.BUTTON_ICON_ONLY);
            this.addComponent(deleteBtn);
            this.setExpandRatio(deleteBtn, 1.0f);
        }

        public ContactOpportunity getContactVal() {
            ContactOpportunity contactOppRel = new ContactOpportunity();
            Contact contact = contactField.getContact();
            if (contact != null && contact.getId() != null) {
                contactOppRel.setContactid(contact.getId());
                contactOppRel.setOpportunityid(opportunity.getId());
                contactOppRel.setDecisionrole((String) roleBox.getValue());
                return contactOppRel;
            } else {
                return null;
            }
        }
    }

    private static class RoleDecisionComboBox extends ValueComboBox {
        private static final long serialVersionUID = 1L;

        public RoleDecisionComboBox() {
            super();
            this.setNullSelectionAllowed(false);
            this.loadData(CrmDataTypeFactory.getOpportunityContactRoleList());
        }
    }
}
