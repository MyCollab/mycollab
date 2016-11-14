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
package com.mycollab.module.crm.view.contact;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.Contact;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.fielddef.ContactTableFieldDef;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebThemes;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContactSelectionWindow extends MWindow {
    private static final long serialVersionUID = 1L;

    private ContactTableDisplay tableItem;
    private FieldSelection<Contact> fieldSelection;

    public ContactSelectionWindow(FieldSelection<Contact> fieldSelection) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(ContactI18nEnum.SINGLE)));
        this.withWidth("900px").withModal(true).withResizable(false);
        this.fieldSelection = fieldSelection;
    }

    public void show() {
        createContactList();
        ContactSearchPanel searchPanel = new ContactSearchPanel();
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));
        this.setContent(new MVerticalLayout(searchPanel, tableItem));

        tableItem.setSearchCriteria(new ContactSearchCriteria());
        center();
    }

    private void createContactList() {
        tableItem = new ContactTableDisplay(Arrays.asList(ContactTableFieldDef.name(), ContactTableFieldDef.account(),
                ContactTableFieldDef.phoneOffice(), ContactTableFieldDef.email(), ContactTableFieldDef.assignUser()));
        tableItem.setWidth("100%");
        tableItem.setDisplayNumItems(10);

        tableItem.addGeneratedColumn("contactName", (source, itemId, columnId) -> {
            final SimpleContact contact = tableItem.getBeanByIndex(itemId);

            return new MButton(contact.getContactName(), clickEvent -> {
                fieldSelection.fireValueChange(contact);
                close();
            }).withStyleName(WebThemes.BUTTON_LINK).withDescription(CrmTooltipGenerator.generateToolTipContact(UserUIContext.getUserLocale(), MyCollabUI.getDateFormat(),
                    contact, MyCollabUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
        });
    }
}
