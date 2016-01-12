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
import com.esofthead.mycollab.form.view.builder.*;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.module.crm.domain.Contact;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContactDefaultDynaFormLayoutFactory {
    private static final DynaForm defaultForm;

    static {
        defaultForm = new DynaForm();

        DynaSection contactSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
                .header(AppContext.getMessage(ContactI18nEnum.SECTION_INFORMATION))
                .build();

        contactSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.firstname)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_FIRSTNAME))
                .fieldIndex(0).build());

        contactSection.addField(new PhoneDynaFieldBuilder().fieldName(Contact.Field.officephone)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_OFFICE_PHONE))
                .fieldIndex(1).build());

        contactSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.lastname)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_LASTNAME))
                .fieldIndex(2).mandatory(true).build());

        contactSection.addField(new PhoneDynaFieldBuilder().fieldName(Contact.Field.mobile)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_MOBILE))
                .fieldIndex(3).build());

        contactSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.accountid)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_ACCOUNTS))
                .fieldIndex(4).build());

        contactSection.addField(new PhoneDynaFieldBuilder().fieldName(Contact.Field.homephone)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_HOME_PHONE))
                .fieldIndex(5).build());

        contactSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.title)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_TITLE))
                .fieldIndex(6).build());

        contactSection.addField(new PhoneDynaFieldBuilder().fieldName(Contact.Field.otherphone)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_OTHER_PHONE))
                .fieldIndex(7).build());

        contactSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.department)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_DEPARTMENT))
                .fieldIndex(8).build());

        contactSection.addField(new PhoneDynaFieldBuilder().fieldName(Contact.Field.fax)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_FAX))
                .fieldIndex(9).build());

        contactSection.addField(new EmailDynaFieldBuilder().fieldName(Contact.Field.email)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_EMAIL))
                .fieldIndex(10).build());

        contactSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.birthday)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_BIRTHDAY))
                .fieldIndex(11).build());

        contactSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.assistant)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_ASSISTANT))
                .fieldIndex(12).build());

        contactSection.addField(new BooleanDynaFieldBuilder().fieldName(Contact.Field.iscallable)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_IS_CALLABLE))
                .fieldIndex(13).build());

        contactSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.assistantphone)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_ASSISTANT_PHONE))
                .fieldIndex(14).build());

        contactSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.assignuser)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE))
                .fieldIndex(15).build());

        contactSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.leadsource)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_LEAD_SOURCE))
                .fieldIndex(16).build());

        defaultForm.addSection(contactSection);

        DynaSection addressSection = new DynaSectionBuilder().layoutType(LayoutType.TWO_COLUMN).orderIndex(1)
                .header(AppContext.getMessage(ContactI18nEnum.SECTION_ADDRESS))
                .build();

        addressSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.primaddress)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_PRIMARY_ADDRESS))
                .fieldIndex(0).build());

        addressSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.otheraddress)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_OTHER_ADDRESS))
                .fieldIndex(1).build());

        addressSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.primcity)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_PRIMARY_CITY))
                .fieldIndex(2).build());

        addressSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.othercity)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_OTHER_CITY))
                .fieldIndex(3).build());

        addressSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.primstate)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_PRIMARY_STATE))
                .fieldIndex(4).build());

        addressSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.otherstate)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_OTHER_STATE))
                .fieldIndex(5).build());

        addressSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.primpostalcode)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_PRIMARY_POSTAL_CODE))
                .fieldIndex(6).build());

        addressSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.otherpostalcode)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_OTHER_POSTAL_CODE))
                .fieldIndex(7).build());

        addressSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.primcountry)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_PRIMARY_COUNTRY))
                .fieldIndex(8).build());

        addressSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.othercountry)
                .displayName(AppContext.getMessage(ContactI18nEnum.FORM_OTHER_COUNTRY))
                .fieldIndex(9).build());

        defaultForm.addSection(addressSection);

        DynaSection descSection = new DynaSectionBuilder().layoutType(LayoutType.ONE_COLUMN).orderIndex(2)
                .header(AppContext.getMessage(ContactI18nEnum.SECTION_DESCRIPTION))
                .build();

        descSection.addField(new TextDynaFieldBuilder().fieldName(Contact.Field.description)
                .displayName(AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION))
                .fieldIndex(0).build());

        defaultForm.addSection(descSection);
    }

    public static DynaForm getForm() {
        return defaultForm;
    }
}
