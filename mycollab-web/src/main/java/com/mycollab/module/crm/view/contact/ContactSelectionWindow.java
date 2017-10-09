package com.mycollab.module.crm.view.contact;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.Contact;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.fielddef.ContactTableFieldDef;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.vaadin.AppUI;
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
        tableItem = new ContactTableDisplay(Arrays.asList(ContactTableFieldDef.name, ContactTableFieldDef.account,
                ContactTableFieldDef.phoneOffice, ContactTableFieldDef.email, ContactTableFieldDef.assignUser));
        tableItem.setWidth("100%");
        tableItem.setDisplayNumItems(10);

        tableItem.addGeneratedColumn("contactName", (source, itemId, columnId) -> {
            final SimpleContact contact = tableItem.getBeanByIndex(itemId);

            return new MButton(contact.getContactName(), clickEvent -> {
                fieldSelection.fireValueChange(contact);
                close();
            }).withStyleName(WebThemes.BUTTON_LINK).withDescription(CrmTooltipGenerator.generateToolTipContact(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
                    contact, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
        });
    }
}
