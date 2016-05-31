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

import com.esofthead.mycollab.module.crm.CrmTooltipGenerator;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.esofthead.mycollab.vaadin.web.ui.ButtonLink;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContactSelectionWindow extends Window {
    private static final long serialVersionUID = 1L;

    private ContactTableDisplay tableItem;
    private FieldSelection<Contact> fieldSelection;

    public ContactSelectionWindow(FieldSelection<Contact> fieldSelection) {
        super("Contact Selection");
        this.setWidth("900px");
        this.fieldSelection = fieldSelection;
        this.setModal(true);
        this.setResizable(false);
    }

    public void show() {
        MVerticalLayout layout = new MVerticalLayout();
        ContactSimpleSearchPanel contactSimpleSearchPanel = new ContactSimpleSearchPanel();
        contactSimpleSearchPanel.addSearchHandler(new SearchHandler<ContactSearchCriteria>() {
            @Override
            public void onSearch(ContactSearchCriteria criteria) {
                tableItem.setSearchCriteria(criteria);
            }
        });
        layout.addComponent(contactSimpleSearchPanel);
        layout.addComponent(tableItem);
        createContactList();
        this.setContent(layout);

        tableItem.setSearchCriteria(new ContactSearchCriteria());
        center();
    }

    private void createContactList() {
        tableItem = new ContactTableDisplay(Arrays.asList(ContactTableFieldDef.name(), ContactTableFieldDef.account(),
                ContactTableFieldDef.phoneOffice(), ContactTableFieldDef.email(), ContactTableFieldDef.assignUser()));
        tableItem.setWidth("100%");
        tableItem.setDisplayNumItems(10);

        tableItem.addGeneratedColumn("contactName", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component generateCell(Table source, Object itemId, Object columnId) {
                final SimpleContact contact = tableItem.getBeanByIndex(itemId);

                ButtonLink b = new ButtonLink(contact.getContactName(), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        fieldSelection.fireValueChange(contact);
                        close();
                    }
                });
                b.setDescription(CrmTooltipGenerator.generateToolTipContact(AppContext.getUserLocale(), AppContext.getDateFormat().toPattern(),
                        contact, AppContext.getSiteUrl(), AppContext.getUserTimeZone()));
                return b;
            }
        });
    }
}
